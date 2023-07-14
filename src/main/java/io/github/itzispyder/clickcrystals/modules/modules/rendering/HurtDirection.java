package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PlayerWasAttackedEvent;
import io.github.itzispyder.clickcrystals.gui.hud.HurtDirectionHud;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;

public class HurtDirection extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Integer> displayTime = scGeneral.add(IntegerSetting.create()
            .max(60)
            .min(10)
            .name("display-time")
            .description("The display time of each direction.")
            .def(10)
            .build()
    );
    public final ModuleSetting<Double> displayScale = scGeneral.add(DoubleSetting.create()
            .max(2.0)
            .min(0.5)
            .decimalPlaces(1)
            .name("display-scale")
            .description("The texture scale of each display.")
            .def(1.0)
            .build()
    );

    public HurtDirection() {
        super("hurt-direction", Categories.RENDERING, "Displays the direction from which you were hurt from.");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onAttacked(PlayerWasAttackedEvent e) {
        if (PlayerUtils.playerNull()) return;

        ClientPlayerEntity p = PlayerUtils.player();
        Entity attacker = e.getAttacker();
        PlayerEntity player = e.getPlayer();

        if (attacker != null && player != null && p.getId() == player.getId()) {
            double rot = player.getYaw() - attacker.getYaw();
            Direction dir = Direction.fromRotation(rot);

            HurtDirectionHud.DisplayRequest request = new HurtDirectionHud.DisplayRequest(dir, displayTime.getVal());
            HurtDirectionHud hud = (HurtDirectionHud)system.huds().get(HurtDirectionHud.class);
            hud.receiveRequest(request);
        }
    }
}
