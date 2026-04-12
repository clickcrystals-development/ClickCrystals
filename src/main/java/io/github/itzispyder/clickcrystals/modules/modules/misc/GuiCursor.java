package io.github.itzispyder.clickcrystals.modules.modules.misc;

import com.mojang.blaze3d.platform.Window;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.client.RenderInventorySlotEvent;
import io.github.itzispyder.clickcrystals.events.events.client.ScreenInitEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.mixininterfaces.AccessorMouse;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuiCursor extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Mode> cursorAction = scGeneral.add(createEnumSetting(Mode.class)
            .name("cursor-action")
            .description("Action to execute when opening inventory.")
            .def(Mode.CENTER_FIX)
            .build()
    );
    public final ModuleSetting<Boolean> totemShiftHolder = scGeneral.add(createBoolSetting()
            .name("totem-shift-holder")
            .description("Holds down shift if the item you clicked on is a totem.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> closestTotemSlot = scGeneral.add(createBoolSetting()
            .name("closest-totem-slot")
            .description("Picks the totem slot closest to the center of the inventory instead of the first one found.")
            .def(true)
            .build()
    );

    private final List<int[]> totemSlots = new ArrayList<>();
    private final List<int[]> allSlots = new ArrayList<>();
    private int collectState = -1; // -1 idle, 0 waiting, 1 collecting
    private boolean shiftKeyDown;

    public GuiCursor() {
        super("gui-cursor", Categories.MISC, "What to do with your cursor when you open inventory gui");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
        this.collectState = -1;
        this.totemSlots.clear();
        this.allSlots.clear();
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        this.collectState = -1;
        this.totemSlots.clear();
        this.allSlots.clear();
    }

    public static void setCursor(int x, int y) {
        Window win = mc.getWindow();
        int w1 = win.getScreenWidth();
        int w2 = win.getGuiScaledWidth();
        int h1 = win.getScreenHeight();
        int h2 = win.getGuiScaledHeight();
        double ratW = (double) w2 / (double) w1;
        double ratH = (double) h2 / (double) h1;
        double rawX = x / ratW;
        double rawY = y / ratH;
        GLFW.glfwSetCursorPos(win.handle(), rawX, rawY);
        // Update internal mouse state directly for Wayland compatibility,
        // where glfwSetCursorPos silently fails in normal cursor mode
        ((AccessorMouse) mc.mouseHandler).setCursorPos(rawX, rawY);
    }

    public static double getCursorX(double x) {
        Window win = mc.getWindow();
        int w1 = win.getScreenWidth();
        int w2 = win.getGuiScaledWidth();
        double ratW = (double) w2 / (double) w1;
        return x * ratW;
    }

    public static double getCursorY(double y) {
        Window win = mc.getWindow();
        int h1 = win.getScreenHeight();
        int h2 = win.getGuiScaledHeight();
        double ratH = (double) h2 / (double) h1;
        return y * ratH;
    }

    public void centerFix() {
        Window win = mc.getWindow();
        setCursor(win.getGuiScaledWidth() / 2, win.getGuiScaledHeight() / 2 + 10);
    }

    public void hoverTotem() {
        totemSlots.clear();
        allSlots.clear();
        collectState = 0;
        system.scheduler.runDelayedTask(() -> {
            pickAndMoveCursor();
            totemSlots.clear();
            allSlots.clear();
            collectState = -1;
        }, 50);
    }

    private void pickAndMoveCursor() {
        if (totemSlots.isEmpty()) return;

        if (!closestTotemSlot.getVal() || allSlots.isEmpty()) {
            setCursor(totemSlots.getFirst()[0], totemSlots.getFirst()[1]);
            return;
        }

        // average all slot centers to get the inv center without touching protected fields
        double cx = allSlots.stream().mapToInt(s -> s[0]).average().orElse(0);
        double cy = allSlots.stream().mapToInt(s -> s[1]).average().orElse(0);

        int[] chosen = totemSlots.stream()
                .min(Comparator.comparingDouble(a -> Math.hypot(a[0] - cx, a[1] - cy)))
                .orElse(totemSlots.getFirst());

        setCursor(chosen[0], chosen[1]);
    }

    @EventHandler
    private void onKey(KeyPressEvent e) {
        if (e.getKeycode() == GLFW.GLFW_KEY_LEFT_SHIFT)
            shiftKeyDown = e.getAction().isDown();
    }

    @EventHandler
    private void renderInventoryItem(RenderInventorySlotEvent e) {
        if (collectState == -1) return;

        int sx = e.getX() + 8;
        int sy = e.getY() + 8;

        if (collectState == 0) {
            totemSlots.clear();
            allSlots.clear();
            collectState = 1;
        }

        allSlots.add(new int[]{sx, sy});

        if (e.getItem().is(Items.TOTEM_OF_UNDYING))
            totemSlots.add(new int[]{sx, sy});
    }

    @EventHandler
    private void onOpenScreen(ScreenInitEvent e) {
        if (e.getScreen() instanceof InventoryScreen) {
            switch (cursorAction.getVal()) {
                case CENTER_FIX -> this.centerFix();
                case HOVER_TOTEM -> this.hoverTotem();
            }
        }
    }

    @EventHandler
    private void onClick(PacketSendEvent e) {
        if (e.getPacket() instanceof ServerboundContainerClickPacket packet) {
            ItemStack stack = InvUtils.inv().getItem(packet.slotNum());
            boolean clickedTotem = mc.screen instanceof InventoryScreen && stack.is(Items.TOTEM_OF_UNDYING) && totemShiftHolder.getVal();
            boolean actionMatches = !shiftKeyDown && packet.containerInput() == ContainerInput.PICKUP;

            if (clickedTotem && actionMatches) {
                int slot = packet.slotNum();
                boolean offEmpty = HotbarUtils.getHand(InteractionHand.OFF_HAND).isEmpty();
                boolean mainEmpty = !HotbarUtils.has(Items.TOTEM_OF_UNDYING);
                boolean slotValid = !InvUtils.isOffhand(slot) && !InvUtils.isHotbar(slot);

                if (offEmpty && slotValid) {
                    e.cancel();
                    InvUtils.swapOffhand(slot);
                    InvUtils.inv().tick();

                    if (mainEmpty) {
                        system.scheduler.runDelayedTask(this::hoverTotem, 50);
                    }
                } else if (slotValid) {
                    e.cancel();
                    InvUtils.quickMove(slot);
                    InvUtils.inv().tick();
                }
            }
        }
    }

    public enum Mode {
        CENTER_FIX,
        HOVER_TOTEM
    }
}