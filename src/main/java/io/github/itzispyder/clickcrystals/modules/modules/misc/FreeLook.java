package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.MathHelper;

public class FreeLook extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<POV> perspective = scGeneral.add(EnumSetting.create(POV.class)
            .name("camera-perspective")
            .description("The perspective which lock the camera.")
            .def(POV.THIRD_PERSON_FOV)
            .build()
    );
    public final ModuleSetting<Integer> freeLookPerspective = scGeneral.add(createIntSetting()
            .name("change-perspective-angle")
            .description("Change the pitch of the module perspective.")
            .def(90)
            .min(-90)
            .max(90)
            .build()
    );
    public final ModuleSetting<Boolean> changeToPov = scGeneral.add(createBoolSetting()
            .name("change-to-camera-perspective-on-enable")
            .description("Change to the selected camera perspective on module enable.")
            .def(false)
            .build()
    );

    public FreeLook() {
        super("free-look", Categories.MISC, "lock your camera perspective and let you move around it");
    }

    public float cY;
    public float cP;

    @Override
    public void onEnable() {
        if (PlayerUtils.invalid()) return;
        cY = PlayerUtils.player().getYaw();
        cP = PlayerUtils.player().getPitch();
        if (changeToPov.getVal()) mc.options.setPerspective(perspective.getVal().getPerspective());
    }

    @Override
    public void onDisable() {
        if (PlayerUtils.invalid()) return;
        if (changeToPov.getVal()) mc.options.setPerspective(Perspective.FIRST_PERSON);
    }


    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        PlayerUtils.player().setPitch(MathHelper.clamp(PlayerUtils.player().getPitch(), freeLookPerspective.getVal() - 180, freeLookPerspective.getVal()));
        cP = MathHelper.clamp(cP, freeLookPerspective.getVal() - 180, freeLookPerspective.getVal());
    }

    public enum POV {
        FIRST_PERSON_FOV(Perspective.FIRST_PERSON),
        SECOND_PERSON_FOV(Perspective.THIRD_PERSON_BACK),
        THIRD_PERSON_FOV(Perspective.THIRD_PERSON_FRONT);

        private final Perspective perspective;

        POV(Perspective perspective) {
            this.perspective = perspective;
        }

        public Perspective getPerspective() {
            return perspective;
        }
    }
}