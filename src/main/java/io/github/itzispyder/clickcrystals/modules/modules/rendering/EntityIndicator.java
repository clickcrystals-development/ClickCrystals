package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class EntityIndicator extends ListenerModule {

    public static final List<EntityDisplay> entities = new ArrayList<>();
    public static EntityDisplay nearest;
    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> updatePerRender = scGeneral.add(createBoolSetting()
            .name("update-per-render")
            .description("Updates every frame instead of every tick. Turning this on CAN slow down games for low end PC's.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Integer> radarRange = scGeneral.add(createIntSetting()
            .name("radar-range")
            .description("Radar detection range.")
            .max(64)
            .min(16)
            .def(16)
            .build()
    );
    public final ModuleSetting<Integer> hudSize = scGeneral.add(createIntSetting()
            .name("hud-size")
            .description("Sprite display size.")
            .max(45)
            .min(10)
            .def(25)
            .build()
    );
    public final ModuleSetting<Integer> spriteSize = scGeneral.add(createIntSetting()
            .name("sprite-size")
            .description("Sprite display size.")
            .max(16)
            .min(5)
            .def(10)
            .build()
    );
    private final SettingSection scSpecific = createSettingSection("specific");
    public final ModuleSetting<Boolean> onlyMonsters = scSpecific.add(createBoolSetting()
            .name("only-monsters")
            .description("Only render monsters on the hud.")
            .def(true)
            .build()
    );

    public EntityIndicator() {
        super("entity-indicator", Categories.RENDER, "Indicates entities around you. Players are excluded.");
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        entities.clear();
        nearest = null;
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        if (!updatePerRender.getVal()) {
            update();
        }
    }

    public void update() {
        var player = PlayerUtils.player();
        entities.clear();
        nearest = null;

        PlayerUtils.runOnNearestEntity(radarRange.getVal(), entity -> {
            boolean bl = entity instanceof MobEntity && entity.isAlive() && !entity.isInvisibleTo(player);
            boolean bl2 = !(!(entity instanceof Monster) && onlyMonsters.getVal());
            if (bl && bl2) {
                EntityIndicator.entities.add(EntityDisplay.of(player, entity));
            }
            return bl && bl2;
        }, nearest -> {
            EntityIndicator.nearest = EntityDisplay.of(player, nearest);
            EntityIndicator.entities.remove(EntityIndicator.nearest);
        });
    }

    public record EntityDisplay(Entity entity, float rotToYaw) {
        public static EntityDisplay of(ClientPlayerEntity player, Entity entity) {
            Vec3d vec = entity.getPos().subtract(player.getPos()).normalize();
            float[] rot = MathUtils.toPolar(vec.x, vec.y, vec.z);
            return new EntityDisplay(entity, rot[1] - player.getYaw());
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof EntityDisplay dis)) {
                return false;
            }
            return this.entity.getId() == dis.entity.getId() && this.rotToYaw == dis.rotToYaw;
        }
    }
}
