package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.mixininterfaces.AccessorKeyboard;
import io.github.itzispyder.clickcrystals.mixininterfaces.AccessorMouse;
import io.github.itzispyder.clickcrystals.mixins.AccessorMinecraftClient;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.modules.misc.GuiCursor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.function.Predicate;

public final class InteractionUtils implements Global {

    public static void inputAttack() {
        ((AccessorMinecraftClient) mc).inputAttack();
    }

    public static void inputAttack(long ms) {
        TickEventListener.attack(ms);
    }

    public static void inputUse() {
        ((AccessorMinecraftClient) mc).inputUse();
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
        mc.options.toggleSprint().set(true);
        if (!mc.options.keySprint.isDown()) {
            mc.options.keySprint.setDown(true);
        }
    }

    public static void inputInventory() {
        if (PlayerUtils.invalid()) {
            return;
        }

        if (mc.screen instanceof InventoryScreen inv) {
            mc.execute(inv::onClose);
        }
        else {
            mc.execute(() -> mc.setScreen(new InventoryScreen(PlayerUtils.player())));
        }
    }

    public static boolean canBreakCrystals() {
        if (PlayerUtils.invalid())
            return false;
        var p = PlayerUtils.player();

        MobEffectInstance s = p.getEffect(MobEffects.STRENGTH);
        MobEffectInstance w = p.getEffect(MobEffects.WEAKNESS);

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
        return new Point((int)GuiCursor.getCursorX(mc.mouseHandler.xpos()), (int)GuiCursor.getCursorY(mc.mouseHandler.ypos()));
    }

    public static void leftClick() {
        ((AccessorMouse) mc.mouseHandler).clickCrystals$leftClick();
    }

    public static void rightClick() {
        ((AccessorMouse) mc.mouseHandler).clickCrystals$rightClick();
    }

    public static void middleClick() {
        ((AccessorMouse) mc.mouseHandler).clickCrystals$middleClick();
    }

    public static void mouseScroll(double amount) {
        ((AccessorMouse) mc.mouseHandler).clickCrystals$scroll(amount);
    }

    public static void pressKey(int key, int scan) {
        ((AccessorKeyboard) mc.keyboardHandler).clickCrystals$pressKey(key, scan);
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
