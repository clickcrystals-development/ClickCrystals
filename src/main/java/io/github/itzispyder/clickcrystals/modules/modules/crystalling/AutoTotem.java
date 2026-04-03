package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class AutoTotem extends Module implements Listener {

    // ============================================
    // SETTING SECTIONS
    // ============================================

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scSpeed = createSettingSection("speed");

    // ============================================
    // GENERAL SETTINGS
    // ============================================

    private final ModuleSetting<String> mode = scGeneral.add(createStringSetting()
            .name("mode")
            .description("Selection mode: INSTANT, BLATANT, RANDOM, VISUAL")
            .def("INSTANT")
            .build()
    );

    private final ModuleSetting<Boolean> closeAfter = scGeneral.add(createBoolSetting()
            .name("close-after")
            .description("Automatically close inventory after placing totem")
            .def(true)
            .build()
    );

    private final ModuleSetting<Boolean> onlyWhenNeeded = scGeneral.add(createBoolSetting()
            .name("only-when-needed")
            .description("Only activate when offhand is empty of totems")
            .def(true)
            .build()
    );

    // ============================================
    // SPEED SETTINGS
    // ============================================

    private final ModuleSetting<Double> cursorSpeed = scSpeed.add(createDoubleSetting()
            .name("cursor-speed")
            .description("Cursor movement speed for VISUAL mode (pixels per tick)")
            .def(1000.0)
            .min(50.0)
            .max(10000.0)
            .build()
    );

    private final ModuleSetting<Integer> closeDelay = scSpeed.add(createIntSetting()
            .name("close-delay")
            .description("Ticks to wait before closing (0 = instant)")
            .def(1)
            .min(0)
            .max(10)
            .build()
    );

    // ============================================
    // INTERNAL STATE
    // ============================================

    private enum State {
        IDLE,
        OPENING,
        EXECUTING,
        CLOSING
    }

    private State currentState = State.IDLE;
    private int tickCounter = 0;
    private Slot targetSlot = null;
    private double targetX = 0;
    private double targetY = 0;
    private final Random random = new Random();

    // Visual mode cursor tracking
    private boolean movingToTotem = false;
    private boolean movingToOffhand = false;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public AutoTotem() {
        super("auto-totem", Categories.CRYSTAL,
                "Automatically equips totems (instant or visual modes)");
    }

    // ============================================
    // MODULE LIFECYCLE
    // ============================================

    @Override
    protected void onEnable() {
        system.addListener(this);
        currentState = State.IDLE;
        tickCounter = 0;
        targetSlot = null;
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        currentState = State.IDLE;
    }

    // ============================================
    // EVENT HANDLERS
    // ============================================

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (mc.player == null || mc.world == null) return;

        // Check if we need a totem
        boolean hasTotem = mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;

        if (onlyWhenNeeded.getVal() && hasTotem) {
            currentState = State.IDLE;
            return;
        }

        // State machine
        switch (currentState) {
            case IDLE -> handleIdle();
            case OPENING -> handleOpening();
            case EXECUTING -> handleExecuting();
            case CLOSING -> handleClosing();
        }
    }

    // ============================================
    // STATE HANDLERS
    // ============================================

    private void handleIdle() {
        // Check if we need to activate
        if (onlyWhenNeeded.getVal() && mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }

        // Check if we have totems in inventory
        if (!hasTotemInInventory()) {
            return;
        }

        // Start the process
        currentState = State.OPENING;
        tickCounter = 0;
    }

    private void handleOpening() {
        // Open inventory immediately
        if (!(mc.currentScreen instanceof InventoryScreen)) {
            mc.setScreen(new InventoryScreen(mc.player));
        }

        // Find totem slot
        if (targetSlot == null) {
            findTotemSlot();
        }

        if (targetSlot != null) {
            currentState = State.EXECUTING;
            tickCounter = 0;
            movingToTotem = false;
            movingToOffhand = false;
        } else {
            // No totem found, close
            currentState = State.CLOSING;
        }
    }

    private void handleExecuting() {
        if (!(mc.currentScreen instanceof InventoryScreen invScreen)) {
            currentState = State.IDLE;
            return;
        }

        String modeStr = mode.getVal().toUpperCase();

        // INSTANT MODE - No cursor movement, immediate clicks
        if (modeStr.equals("INSTANT") || modeStr.equals("BLATANT") || modeStr.equals("RANDOM")) {
            // Click totem slot
            mc.interactionManager.clickSlot(
                    invScreen.getScreenHandler().syncId,
                    targetSlot.id,
                    0,
                    SlotActionType.PICKUP,
                    mc.player
            );

            // Click offhand slot
            mc.interactionManager.clickSlot(
                    invScreen.getScreenHandler().syncId,
                    45,
                    0,
                    SlotActionType.PICKUP,
                    mc.player
            );

            // Done, move to closing
            currentState = State.CLOSING;
            tickCounter = 0;
            return;
        }

        // VISUAL MODE - Show cursor movement
        if (modeStr.equals("VISUAL")) {
            // Step 1: Move to totem
            if (!movingToTotem) {
                calculateTargetPosition(targetSlot);
                movingToTotem = true;
                return;
            }

            // Step 2: Move cursor to totem
            if (movingToTotem && !movingToOffhand) {
                if (moveCursorToTarget()) {
                    // Reached totem, click it
                    mc.interactionManager.clickSlot(
                            invScreen.getScreenHandler().syncId,
                            targetSlot.id,
                            0,
                            SlotActionType.PICKUP,
                            mc.player
                    );

                    // Now move to offhand
                    Slot offhandSlot = invScreen.getScreenHandler().getSlot(45);
                    calculateTargetPosition(offhandSlot);
                    movingToOffhand = true;
                }
                return;
            }

            // Step 3: Move to offhand and click
            if (movingToOffhand) {
                if (moveCursorToTarget()) {
                    // Reached offhand, click it
                    mc.interactionManager.clickSlot(
                            invScreen.getScreenHandler().syncId,
                            45,
                            0,
                            SlotActionType.PICKUP,
                            mc.player
                    );

                    // Done
                    currentState = State.CLOSING;
                    tickCounter = 0;
                }
            }
        }
    }

    private void handleClosing() {
        tickCounter++;

        // Force close after 1 tick, no checks
        if (tickCounter >= 1) {
            // Force close the screen
            if (mc.currentScreen != null) {
                mc.setScreen(null); // Force close
            }
            
            currentState = State.IDLE;
            tickCounter = 0;
            targetSlot = null;
            movingToTotem = false;
            movingToOffhand = false;
        }
    }

    // ============================================
    // CURSOR MOVEMENT (VISUAL MODE)
    // ============================================

    private boolean moveCursorToTarget() {
        double mouseX = mc.mouse.getX();
        double mouseY = mc.mouse.getY();

        double dx = targetX - mouseX;
        double dy = targetY - mouseY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Check if reached target
        if (distance < 5.0) {
            return true;
        }

        // Move cursor
        double speed = cursorSpeed.getVal();
        double angle = Math.atan2(dy, dx);
        double moveX = Math.cos(angle) * Math.min(speed, distance);
        double moveY = Math.sin(angle) * Math.min(speed, distance);

        GLFW.glfwSetCursorPos(mc.getWindow().getHandle(), mouseX + moveX, mouseY + moveY);
        return false;
    }

    private void calculateTargetPosition(Slot slot) {
        if (!(mc.currentScreen instanceof InventoryScreen invScreen)) {
            return;
        }

        int slotX = getSlotX(invScreen, slot);
        int slotY = getSlotY(invScreen, slot);

        targetX = slotX * mc.getWindow().getScaleFactor();
        targetY = slotY * mc.getWindow().getScaleFactor();
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private boolean hasTotemInInventory() {
        if (mc.player == null) return false;

        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                return true;
            }
        }
        return false;
    }

    private void findTotemSlot() {
        if (!(mc.currentScreen instanceof InventoryScreen invScreen)) {
            targetSlot = null;
            return;
        }

        String modeStr = mode.getVal().toUpperCase();

        switch (modeStr) {
            case "RANDOM" -> targetSlot = findRandomTotemSlot(invScreen);
            default -> targetSlot = findFirstTotemSlot(invScreen);
        }
    }

    private Slot findFirstTotemSlot(InventoryScreen invScreen) {
        for (Slot slot : invScreen.getScreenHandler().slots) {
            if (slot.id >= 9 && slot.id <= 35) {
                if (slot.getStack().getItem() == Items.TOTEM_OF_UNDYING) {
                    return slot;
                }
            }
        }
        return null;
    }

    private Slot findRandomTotemSlot(InventoryScreen invScreen) {
        List<Slot> totemSlots = new ArrayList<>();

        for (Slot slot : invScreen.getScreenHandler().slots) {
            if (slot.id >= 9 && slot.id <= 35) {
                if (slot.getStack().getItem() == Items.TOTEM_OF_UNDYING) {
                    totemSlots.add(slot);
                }
            }
        }

        if (totemSlots.isEmpty()) return null;
        return totemSlots.get(random.nextInt(totemSlots.size()));
    }

    private int getSlotX(InventoryScreen screen, Slot slot) {
        int centerX = mc.getWindow().getScaledWidth() / 2;
        int inventoryLeft = centerX - 88;
        return inventoryLeft + slot.x + 8;
    }

    private int getSlotY(InventoryScreen screen, Slot slot) {
        int centerY = mc.getWindow().getScaledHeight() / 2;
        int inventoryTop = centerY - 83;
        return inventoryTop + slot.y + 8;
    }
}