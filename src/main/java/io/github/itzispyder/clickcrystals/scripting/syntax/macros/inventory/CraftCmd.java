package io.github.itzispyder.clickcrystals.scripting.syntax.macros.inventory;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.*;

import java.util.function.Predicate;

// @Format craft <identifier> <num>? <num>?
public class CraftCmd extends ScriptCommand implements Global {

    private static final int MIN_DELAY = 60, RAND_DELAY = 90;
    private boolean crafting;

    public CraftCmd() {
        super("craft");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (PlayerUtils.invalid() || mc.level == null || crafting)
            return;

        // require an actually-open crafting screen (2x2 inventory or a 3x3 table)
        if (!(mc.gui.screen() instanceof AbstractContainerScreen<?>) || !(mc.player.containerMenu instanceof AbstractCraftingMenu menu))
            return;

        String id = args.get(0).toString();
        Predicate<ItemStack> target = ScriptParser.parseItemPredicate(id);
        long delay = (long) (args.getSize() > 1 ? args.get(1).toDouble() * 1000L : 0L);
        boolean all = args.getSize() <= 2; // amount omitted -> craft everything
        int amount = all ? 0 : args.get(2).toInt();
        if (!all && amount <= 0)
            return;

        int side = (int) Math.sqrt(menu.getInputGridSlots().size());
        ContextMap ctx = SlotDisplayContext.fromLevel(mc.level);
        Search found = search(target, ctx, side);

        if (found.entry == null) {
            if (!found.matchedAny)
                throw new IllegalArgumentException("No crafting recipe produces an item matching \"" + id + "\".");
            if (!found.anyFits)
                throw new IllegalArgumentException("Recipe for \"" + id + "\" doesn't fit the current " + side + "x" + side + " grid - open a crafting table.");
            throw new IllegalArgumentException("Not enough materials in your inventory to craft \"" + id + "\".");
        }

        ItemStack result = found.entry.resultItems(ctx).get(0);
        if (noRoomFor(result))
            throw new IllegalArgumentException("Your inventory is full - no room for the crafted \"" + id + "\".");

        // "all": repeatedly max-fill and bulk-craft until materials run out.
        // amount: craft one recipe set per cycle, enough cycles to yield the requested amount.
        crafting = true;
        int cycles = all ? Integer.MAX_VALUE : (amount + result.getCount() - 1) / result.getCount();
        craftCycle(menu, found.entry.id(), all, cycles, delay);
    }

    // One fill-then-take cycle; reschedules itself until done, materials run out, or the inventory fills up.
    private void craftCycle(AbstractCraftingMenu menu, RecipeDisplayId recipe, boolean all, int cyclesLeft, long delay) {
        step(menu, delay, () -> {
            mc.gameMode.handlePlaceRecipe(menu.containerId, recipe, all);

            step(menu, delay, () -> {
                Slot result = menu.getResultSlot();
                if (!result.hasItem() || noRoomFor(result.getItem())) {
                    crafting = false;
                    return;
                }
                mc.gameMode.handleContainerInput(menu.containerId, result.index, 0, ContainerInput.QUICK_MOVE, mc.player);

                int left = all ? cyclesLeft : cyclesLeft - 1;
                if (left > 0) craftCycle(menu, recipe, all, left, delay);
                else crafting = false;
            });
        });
    }

    // Runs a craft step on the main thread after a human-like delay. Aborts if the menu closed and
    // always clears the busy flag on failure, so a dropped/throwing step can never wedge the command.
    private void step(AbstractCraftingMenu menu, long delay, Runnable body) {
        system.scheduler.runDelayedTask(self -> mc.execute(() -> {
            try {
                if (PlayerUtils.invalid() || mc.player.containerMenu != menu) {
                    crafting = false;
                    return;
                }
                body.run();
            }
            catch (Exception e) {
                crafting = false;
            }
        }), delay + MIN_DELAY + (long) (Math.random() * RAND_DELAY));
    }

    // Scans the recipe book for a craftable crafting recipe whose result matches, tracking why a match may be unusable.
    private Search search(Predicate<ItemStack> target, ContextMap ctx, int side) {
        StackedItemContents contents = new StackedItemContents();
        for (ItemStack stack : InvUtils.inv().getNonEquipmentItems())
            contents.accountStack(stack);

        boolean matchedAny = false, anyFits = false;
        ClientRecipeBook book = mc.player.getRecipeBook();

        for (RecipeCollection collection : book.getCollections()) {
            for (RecipeDisplayEntry entry : collection.getRecipes()) {
                if (!isCrafting(entry.display()) || entry.resultItems(ctx).stream().noneMatch(target))
                    continue;

                matchedAny = true;
                boolean fits = fitsGrid(entry.display(), side);
                anyFits |= fits;

                if (fits && entry.canCraft(contents))
                    return new Search(entry, true, true);
            }
        }
        return new Search(null, matchedAny, anyFits);
    }

    // True when the result can't be stored: no empty slot and no matching stack with room.
    private static boolean noRoomFor(ItemStack result) {
        Inventory inv = InvUtils.inv();
        return inv.getFreeSlot() == -1 && inv.getSlotWithRemainingSpace(result) == -1;
    }

    private static boolean isCrafting(RecipeDisplay display) {
        return display instanceof ShapedCraftingRecipeDisplay || display instanceof ShapelessCraftingRecipeDisplay;
    }

    private static boolean fitsGrid(RecipeDisplay display, int side) {
        if (display instanceof ShapedCraftingRecipeDisplay shaped)
            return shaped.width() <= side && shaped.height() <= side;
        if (display instanceof ShapelessCraftingRecipeDisplay shapeless)
            return shapeless.ingredients().size() <= side * side;
        return false;
    }

    private record Search(RecipeDisplayEntry entry, boolean matchedAny, boolean anyFits) {}
}
