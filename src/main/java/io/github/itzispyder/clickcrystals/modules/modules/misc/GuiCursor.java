package io.github.itzispyder.clickcrystals.modules.modules.misc;

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
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.Window;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

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
    private boolean listeningForNextDraw, shiftKeyDown;

    public GuiCursor() {
        super("gui-cursor", Categories.MISC, "What to do with your cursor when you open inventory gui");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
        this.listeningForNextDraw = false;
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        this.listeningForNextDraw = false;
    }

    public static void setCursor(int x, int y) {
        Window win = mc.getWindow();
        int w1 = win.getWidth();
        int w2 = win.getScaledWidth();
        int h1 = win.getHeight();
        int h2 = win.getScaledHeight();
        double ratW = (double)w2 / (double)w1;
        double ratH = (double)h2 / (double)h1;
        GLFW.glfwSetCursorPos(win.getHandle(), x / ratW, y / ratH);
    }

    public static double getCursorX(double x) {
        Window win = mc.getWindow();
        int w1 = win.getWidth();
        int w2 = win.getScaledWidth();
        double ratW = (double)w2 / (double)w1;
        return x * ratW;
    }

    public static double getCursorY(double y) {
        Window win = mc.getWindow();
        int h1 = win.getHeight();
        int h2 = win.getScaledHeight();
        double ratH = (double)h2 / (double)h1;
        return y * ratH;
    }

    public void centerFix() {
        Window win = mc.getWindow();
        setCursor(win.getScaledWidth() / 2, win.getScaledHeight() / 2 + 10);
    }

    public void hoverTotem() {
        listeningForNextDraw = true;
        system.scheduler.runDelayedTask(() -> listeningForNextDraw = false, 50);
    }

    @EventHandler
    private void onKey(KeyPressEvent e) {
        if (e.getKeycode() == GLFW.GLFW_KEY_LEFT_SHIFT) {
            shiftKeyDown = e.getAction().isDown();
        }
    }

    @EventHandler
    private void renderInventoryItem(RenderInventorySlotEvent e) {
        if (e.getItem().isOf(Items.TOTEM_OF_UNDYING) && listeningForNextDraw) {
            setCursor(e.getX() + 8, e.getY() + 8);
            listeningForNextDraw = false;
        }
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
        if (e.getPacket() instanceof ClickSlotC2SPacket packet) {
            ItemStack stack = InvUtils.inv().getStack(packet.slot());
            boolean clickedTotem = mc.currentScreen instanceof InventoryScreen && stack.isOf(Items.TOTEM_OF_UNDYING) && totemShiftHolder.getVal();
            boolean actionMatches = !shiftKeyDown && packet.actionType() == SlotActionType.PICKUP;

            if (clickedTotem && actionMatches) {
                int slot = packet.slot();
                boolean offEmpty = HotbarUtils.getHand(Hand.OFF_HAND).isEmpty();
                boolean mainEmpty = !HotbarUtils.has(Items.TOTEM_OF_UNDYING);
                boolean slotValid = !InvUtils.isOffhand(slot) && !InvUtils.isHotbar(slot);

                if (offEmpty && slotValid) {
                    e.cancel();
                    InvUtils.swapOffhand(slot);
                    InvUtils.inv().updateItems();

                    if (mainEmpty) {
                        system.scheduler.runDelayedTask(this::hoverTotem, 50);
                    }
                }
                else if (slotValid) {
                    e.cancel();
                    InvUtils.quickMove(slot);
                    InvUtils.inv().updateItems();
                }
            }
        }
    }

    public enum Mode {
        CENTER_FIX,
        HOVER_TOTEM
    }
}
