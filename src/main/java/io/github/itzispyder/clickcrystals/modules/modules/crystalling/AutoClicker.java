package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.interfaces.MinecraftClientAccessor;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class AutoClicker extends ListenerModule {

    private final SettingSection scLeft = createSettingSection("left");
    public final ModuleSetting<Boolean> left = scLeft.add(createBoolSetting()
            .name("left")
            .description("Enabled")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> leftSpam = scLeft.add(createBoolSetting()
            .name("left-spam")
            .description("Spam")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> leftOnlyHold = scLeft.add(createBoolSetting()
            .name("left-only-hold")
            .description("Spam only when held")
            .def(true)
            .build()
    );
    public final ModuleSetting<Integer> leftCps = scLeft.add(createIntSetting()
            .name("left-cps")
            .description("Clicks per second")
            .max(50)
            .min(1)
            .def(10)
            .build()
    );
    public final ModuleSetting<Double> leftChance = scLeft.add(createDoubleSetting()
            .name("left-chance")
            .description("Random")
            .max(1.0)
            .min(0.0)
            .def(1.0)
            .build()
    );
    private final SettingSection scRight = createSettingSection("right");
    public final ModuleSetting<Boolean> right = scRight.add(createBoolSetting()
            .name("right")
            .description("Enabled")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> rightSpam = scRight.add(createBoolSetting()
            .name("right-spam")
            .description("Spam")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> rightOnlyHold = scRight.add(createBoolSetting()
            .name("right-only-hold")
            .description("Spam only when held")
            .def(true)
            .build()
    );
    public final ModuleSetting<Integer> rightCps = scRight.add(createIntSetting()
            .name("right-cps")
            .description("Clicks per second")
            .max(50)
            .min(1)
            .def(10)
            .build()
    );
    public final ModuleSetting<Double> rightChance = scRight.add(createDoubleSetting()
            .name("right-chance")
            .description("Random")
            .max(1.0)
            .min(0.0)
            .def(1.0)
            .build()
    );
    private final SettingSection scShared = createSettingSection("shared-settings");
    public final ModuleSetting<Boolean> onlyWhenTarget = scShared.add(createBoolSetting()
            .name("only-when-target")
            .description("Only when targeting an entity")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> noBabies = scShared.add(createBoolSetting()
            .name("no-babies")
            .description("Stop when targeting baby entity")
            .def(true)
            .build()
    );
    public final ModuleSetting<Double> maxAttackCooldown = scShared.add(createDoubleSetting()
            .name("max-attack-cooldown")
            .description("Max attack cooldown")
            .max(1.0)
            .min(0.0)
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Boolean> stopWhenMove = scShared.add(createBoolSetting()
            .name("stop-when-move")
            .description("Stop when player move")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> stopWhenDamage = scShared.add(createBoolSetting()
            .name("stop-when-damage")
            .description("Stop when take damage")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> stopWhenTarget = scShared.add(createBoolSetting()
            .name("stop-when-target")
            .description("Stop when targeting entity")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> debug = scShared.add(createBoolSetting()
            .name("debug")
            .description("Send debug messages")
            .def(false)
            .build()
    );

    private boolean leftToggle, rightToggle;
    private Vec3d prevPos;
    private float prevHp;
    private int tickLeft, tickRight;
    private final int[] noiseMapLeft = new int[20];
    private final int[] noiseMapRight = new int[20];

    public AutoClicker() {
        super("auto-clicker", Categories.CRYSTAL, "Auto clicker duh");
    }

    /*
    I hope this is how noise works
     */
    private void distributeNoise(int cps, double clickChance, int[] noiseMap) {
        float chance = cps / 20.0F /* maxClicksPerTick */ / 20.0F /* averageAcrossEachTick */;
        int distributions = 0;

        for (int i = 0; i < 20; i++)
            noiseMap[i] = 0;

        while (distributions < cps) {
            int randomIndex = MathUtils.clamp((int)(Math.random() * 20),0, 19);
            if (distributions < 20 && noiseMap[randomIndex] > 0)
                continue;
            if (Math.random() > chance * clickChance) // simulates click
                continue;
            noiseMap[randomIndex]++;
            distributions++;
        }
    }

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (!canClick())
            return;

        if (left.getVal()) {
            leftToggle = true;
            clickLeft();
        }
        else if (leftToggle) {
            leftToggle = false;
            mc.options.attackKey.setPressed(false);
        }

        if (right.getVal()) {
            rightToggle = true;
            clickRight();
        }
        else if (rightToggle) {
            rightToggle = false;
            mc.options.useKey.setPressed(false);
        }
    }

    private void clickLeft() {
        if (!leftSpam.getVal()) {
            mc.options.attackKey.setPressed(true);
            return;
        }
        if (leftOnlyHold.getVal() && !mc.options.attackKey.isPressed())
            return;

        for (int i = 0; i < noiseMapLeft[tickLeft]; i++)
            if (Math.random() <= leftChance.getVal())
                getInput().inputAttack();

        if (++tickLeft >= 20) {
            tickLeft = 0;
            distributeNoise(leftCps.getVal(), leftChance.getVal(), noiseMapLeft);
            if (debug.getVal())
                ChatUtils.sendPrefixMessage("Left noise: " + Arrays.toString(noiseMapLeft));
        }
    }

    private void clickRight() {
        if (!rightSpam.getVal()) {
            mc.options.useKey.setPressed(true);
            return;
        }
        if (rightOnlyHold.getVal() && !mc.options.useKey.isPressed())
            return;

        for (int i = 0; i < noiseMapRight[tickRight]; i++)
            if (Math.random() <= rightChance.getVal())
                getInput().inputUse();

        if (++tickRight >= 20) {
            tickRight = 0;
            distributeNoise(rightCps.getVal(), rightChance.getVal(), noiseMapRight);
            if (debug.getVal())
                ChatUtils.sendPrefixMessage("Right noise: " + Arrays.toString(noiseMapRight));
        }
    }

    private boolean canClick() {
        if (PlayerUtils.invalid())
            return false;

        var p = mc.player;
        boolean noTarget = true;
        boolean isBaby = false;
        float hp = prevHp;
        Vec3d pos = prevPos;
        prevHp = p.getHealth();
        prevPos = p.getPos();

        if (mc.crosshairTarget instanceof EntityHitResult hit) {
            noTarget = false;
            isBaby = hit.getEntity() instanceof LivingEntity liv && liv.isBaby();
        }

        if (onlyWhenTarget.getVal() && noTarget)
            return false;
        if (noBabies.getVal() && isBaby)
            return false;
        if (maxAttackCooldown.getVal() > 0 && p.getAttackCooldownProgress(1.0F) < maxAttackCooldown.getVal())
            return false;
        if (stopWhenDamage.getVal() && p.getHealth() < hp) {
            if (left.getVal() || right.getVal()) {
                left.setVal(false);
                right.setVal(false);
                ChatUtils.sendPrefixMessage("Damage taken, clickers disabled");
            }
            return false;
        }
        if (stopWhenMove.getVal() && p.getPos().distanceTo(pos) > 0.1) {
            if (left.getVal() || right.getVal()) {
                left.setVal(false);
                right.setVal(false);
                ChatUtils.sendPrefixMessage("Player moved, clickers disabled");
            }
            return false;
        }
        return !stopWhenTarget.getVal() || noTarget;
    }

    public static MinecraftClientAccessor getInput() {
        return (MinecraftClientAccessor)mc;
    }
}
