package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.interfaces.KeyboardAccessor;
import io.github.itzispyder.clickcrystals.interfaces.MinecraftClientAccessor;
import io.github.itzispyder.clickcrystals.interfaces.MouseAccessor;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.modules.misc.GuiCursor;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.function.Predicate;

public final class InteractionUtils implements Global {

    public static void inputAttack() {
        ((MinecraftClientAccessor) mc).inputAttack();
    }

    public static void inputAttack(long ms) {
        TickEventListener.attack(ms);
    }

    public static void inputUse() {
        ((MinecraftClientAccessor) mc).inputUse();
    }

    public static void inputUse(long ms) {
        TickEventListener.use(ms);
    }

    public static void inputJump() {
        TickEventListener.jump(100);
    }

    public static void inputJump(long ms) {
        TickEventListener.jump(ms);
    }

    public static void inputForward() {
        TickEventListener.forward(500);
    }

    public static void inputForward(long ms) {
        TickEventListener.forward(ms);
    }

    public static void inputBackward() {
        TickEventListener.backward(500);
    }

    public static void inputBackward(long ms) {
        TickEventListener.backward(ms);
    }

    public static void inputStrafeLeft() {
        TickEventListener.strafeLeft(500);
    }

    public static void inputStrafeLeft(long ms) {
        TickEventListener.strafeLeft(ms);
    }

    public static void inputStrafeRight() {
        TickEventListener.strafeRight(500);
    }

    public static void inputStrafeRight(long ms) {
        TickEventListener.strafeRight(ms);
    }

    public static void inputSneak() {
        TickEventListener.sneak(500);
    }

    public static void inputSneak(long ms) {
        TickEventListener.sneak(ms);
    }

    public static void inputSwap() {
        InvUtils.swapOffhand(InvUtils.selected());
    }

    public static void inputDrop() {
        InvUtils.dropSlot(InvUtils.selected(), false);
    }

    public static void inputDropFull() {
        InvUtils.dropSlot(InvUtils.selected(), true);
    }

    public static void inputToggleSprint() {
        mc.options.getSprintToggled().setValue(true);
        if (!mc.options.sprintKey.isPressed()) {
            mc.options.sprintKey.setPressed(true);
        }
    }

    public static void inputInventory() {
        if (PlayerUtils.invalid()) {
            return;
        }

        if (mc.currentScreen instanceof InventoryScreen inv) {
            mc.execute(inv::close);
        }
        else {
            mc.execute(() -> mc.setScreen(new InventoryScreen(PlayerUtils.player())));
        }
    }

    public static boolean canBreakCrystals() {
        if (PlayerUtils.invalid())
            return false;
        var p = PlayerUtils.player();

        StatusEffectInstance s = p.getStatusEffect(StatusEffects.STRENGTH);
        StatusEffectInstance w = p.getStatusEffect(StatusEffects.WEAKNESS);

        if (s == null && w == null) {
            return true;
        }
        if (w == null) {
            return true;
        }
        if (s != null && s.getAmplifier() > w.getAmplifier()) {
            return true;
        }

        return HotbarUtils.isHoldingTool();
    }

    public static void searchGuiItem(Predicate<ItemStack> item) {
        UserInputListener.queueGuiItemSearch(item);
    }

    public static void setCursor(Point p) {
        GuiCursor.setCursor(p.x, p.y);
    }

    public static void setCursor(int x, int y) {
        setCursor(new Point(x, y));
    }

    public static Point getCursor() {
        return new Point((int)GuiCursor.getCursorX(mc.mouse.getX()), (int)GuiCursor.getCursorY(mc.mouse.getY()));
    }

    public static void leftClick() {
        ((MouseAccessor) mc.mouse).leftClick();
    }

    public static void rightClick() {
        ((MouseAccessor) mc.mouse).rightClick();
    }

    public static void middleClick() {
        ((MouseAccessor) mc.mouse).middleClick();
    }

    public static void pressKey(int key, int scan) {
        ((KeyboardAccessor) mc.keyboard).pressKey(key, scan);
    }

    public static void pressKeyExtendedName(String name) {
        int key = Keybind.fromExtendedKeyName(name);
        if (key != -1)
            pressKey(key, 42);
    }

    public static boolean isKeyPressed(int key) {
        return UserInputListener.isKeyPressed(key);
    }

    public static boolean isKeyExtendedNamePressed(String name) {
        return isKeyPressed(Keybind.fromExtendedKeyName(name));
    }
}
