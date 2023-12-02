package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.world.BlockBreakEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.gui.screens.ModuleEditScreen;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.block.Block;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MouseTaper extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Button> button = scGeneral.add(createEnumSetting(Button.class)
            .name("button")
            .description("Button to tape down.")
            .def(Button.Left)
            .build()
    );
    public final ModuleSetting<Mode> mode = scGeneral.add(createEnumSetting(Mode.class)
            .name("tape-mode")
            .description("Tape button mode.")
            .def(Mode.Forever)
            .build()
    );
    public final ModuleSetting<Boolean> shouldLockCursor = scGeneral.add(createBoolSetting()
            .name("should-lock-cursor")
            .description("Locks cursor when the module is enabled. (Re-enable module to apply changes)")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> reopenOnDisable = scGeneral.add(createBoolSetting()
            .name("reopen-on-disable")
            .description("Come back to this screen when module is disabled.")
            .def(true)
            .build()
    );
    private final SettingSection scExclude = createSettingSection("to-exclude");
    public final ModuleSetting<Boolean> excludeBelow = scExclude.add(createBoolSetting()
            .name("exclude-below")
            .description("Do not mine blocks below your feet.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> excludeAbove = scExclude.add(createBoolSetting()
            .name("exclude-above")
            .description("Do not mine blocks above your head.")
            .def(false)
            .build()
    );
    public Block targetType;
    public BlockPos targetPos;

    public MouseTaper() {
        super("mouse-taper", Categories.MISC, "\"I taped a piece of tape on my mouse button, now I cannot use that button anymore but it is still taped down\"");
        targetPos = null;
        targetType = null;
    }

    @Override
    protected void onEnable() {
        super.onEnable();

        // checks (disable module if fail)
        Mode mode = this.mode.getVal();
        if (mode.requiresTarget()) {
            if (mc.crosshairTarget instanceof BlockHitResult hit && mc.crosshairTarget.getType() != HitResult.Type.MISS) {
                if (mode == Mode.One_Pos && targetPos == null) {
                    targetPos = hit.getBlockPos();
                    ChatUtils.sendPrefixMessage("Target set to §7" + targetPos.toShortString());
                }
                if (mode == Mode.One_Type && targetType == null) {
                    targetType = PlayerUtils.getWorld().getBlockState(hit.getBlockPos()).getBlock();
                    ChatUtils.sendPrefixMessage("Target set to §7" + targetType.getName().getString());
                }
            }
            else {
                ChatUtils.sendPrefixMessage("§cThe mode you selected requires you to select a target block, but you are not looking at a block!");
                this.setEnabled(false);
                return;
            }
        }

        // locks cursor
        if (shouldLockCursor.getVal()) {
            CameraRotator.lockCursor();
            ChatUtils.sendPrefixMessage("Cursor locked! (configure in settings)");
        }
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (mc != null && mc.options != null) {
            this.unpressAll();
            targetType = null; // reset because punch-to-set-target only triggers once
            targetPos = null;
            CameraRotator.unlockCursor();
            ChatUtils.sendPrefixMessage("Tape removed from §7%s§r button".formatted(button.getVal().name()));

            if (reopenOnDisable.getVal() && mc.currentScreen == null) {
                mc.setScreen(new ModuleEditScreen(this));
            }
        }
    }

    @EventHandler
    public void onTick(ClientTickStartEvent e) {
        if (shouldLockCursor.getVal()) {
            CameraRotator.lockCursor();
        }
        if (!(mc.crosshairTarget instanceof BlockHitResult hit)) {
            this.unpressAll();
            return;
        }

        Mode mode = this.mode.getVal();
        World w = PlayerUtils.getWorld();

        // checks (skip if fail)
        if ((mode == Mode.One_Pos && targetPos == null) || (mode == Mode.One_Type && targetType == null)) {
            this.unpressAll();
            return;
        }
        if (mode == Mode.One_Type && w.getBlockState(hit.getBlockPos()).getBlock() != targetType) {
            this.unpressAll();
            return;
        }
        if (mode == Mode.One_Pos && !hit.getBlockPos().equals(targetPos)) {
            this.unpressAll();
            return;
        }

        if (excludeBelow.getVal() && hit.getBlockPos().getY() < PlayerUtils.player().getBlockY()) {
            this.unpressAll();
            return;
        }
        if (excludeAbove.getVal() && hit.getBlockPos().getY() > PlayerUtils.player().getBlockY() + 1) {
            this.unpressAll();
            return;
        }

        // finally, do this
        if (!mc.options.attackKey.isPressed()) {
            this.press();
        }
    }

    @EventHandler
    public void onMouseClick(MouseClickEvent e) {
        if (e.isScreenNull() && e.getButton() == button.getVal().getButton()) {
            e.cancel();
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (mode.getVal() == Mode.Once) {
            setEnabled(false);
        }
    }

    public void unpressAll() {
        mc.options.attackKey.setPressed(false);
        mc.options.useKey.setPressed(false);
    }

    public void press() {
        KeyBinding bind = button.getVal() == Button.Left ? mc.options.attackKey : mc.options.useKey;
        bind.setPressed(true);
    }

    public enum Button {
        Left(0),
        Right(1);

        private final int button;

        Button(int button) {
            this.button = button;
        }

        public int getButton() {
            return button;
        }
    }

    public enum Mode {
        One_Pos("One-Position", true),
        One_Type("One-Blocktype", true),
        Once("Tape-Once", false),
        Forever("Forever", false);

        private final String name;
        private final boolean requiresTarget;

        Mode(String name, boolean requiresTarget) {
            this.requiresTarget = requiresTarget;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean requiresTarget() {
            return requiresTarget;
        }
    }
}
