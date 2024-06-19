package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import com.mojang.authlib.GameProfile;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static com.mojang.blaze3d.systems.RenderSystem.*;
import static io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils.*;

public class HealthTags extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> onlyPlayers = scGeneral.add(createBoolSetting()
            .name("only-players")
            .description("Only show health tags of players.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> scaleWithDistance = scGeneral.add(createBoolSetting()
            .name("scale-with-distance")
            .description("Scales the card larger when farther away.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> noNPC = scGeneral.add(createBoolSetting()
            .name("no-npc")
            .description("Not render health tags of fake players.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> noUnliving = scGeneral.add(createBoolSetting()
            .name("no-unliving")
            .description("Not render health tags of unliving entities.")
            .def(true)
            .build()
    );
    
    public HealthTags() {
        super("health-tags", Categories.CLIENT, "Renders a health tag card above selected entities");
    }

    public void render(MatrixStack matrices, LivingEntity entity, VertexConsumerProvider vertexConsumers) {
        if (onlyPlayers.getVal() && !(entity instanceof PlayerEntity))
            return;
        if (noUnliving.getVal() && !(entity instanceof LivingEntity))
            return;
        if (entity instanceof PlayerEntity player && noNPC.getVal() && !PlayerUtils.playerValid(player))
            return;

        var dispatcher = mc.getEntityRenderDispatcher();
        double dist = dispatcher.getSquaredDistanceToCamera(entity);
        float scale = !scaleWithDistance.getVal() ? 0.025F : (float)MathUtils.clamp(0.00015 * (dist + 10), 0.025, 0.25);

        Text text = entity.getName();
        float height = PlayerUtils.getEntityNameLabelHeight(entity, 1.0F);
        var tr = mc.textRenderer;
        float width = Math.max(100, tr.getWidth(text)) + 10;
        float x = -width / 2;
        float y = -50;

        matrices.push();
        matrices.translate(0.0F, height, 0.0F);
        matrices.multiply(dispatcher.getRotation());
        matrices.scale(scale, -scale, scale);

        render(matrices, entity, vertexConsumers, text, x, y, width);

        matrices.pop();
    }

    private void render(MatrixStack matrices, LivingEntity entity, VertexConsumerProvider vertexConsumers, Text text, float x, float y, float width) {
        Matrix4f mat = matrices.peek().getPositionMatrix();
        var type = TextRenderer.TextLayerType.SEE_THROUGH;
        int bg = 0x00000000;
        int light = 15;
        int color = 0xFFFFFFFF;
        var net = PlayerUtils.player().networkHandler;

        int maxDamage = 0;
        int damage = 0;
        for (var item : entity.getArmorItems()) {
            maxDamage += item.getMaxDamage();
            damage += (item.getMaxDamage() - item.getDamage());
        }
        boolean hasArmor = damage > 0 && maxDamage > 0;
        boolean hasHealth = (int)entity.getHealth() > 0;
        boolean hasPing = entity instanceof PlayerEntity p && PlayerUtils.playerValid(p);
        int height = 50;
        height = height + (hasPing ? 0 : -10);
        height = height + (hasArmor ? 0 : -10);
        height = height + (hasHealth ? 0 : -10);
        y = y + (hasPing ? 0 : 10);
        y = y + (hasArmor ? 0 : 10);
        y = y + (hasHealth ? 0 : 10);

        fillRoundRect(matrices, x, y, width, height, 3, 0x80000000);

        float margin = x + 5;
        float caret = y + 7;

        mc.textRenderer.draw(text, margin, caret, color, false, mat, vertexConsumers, type, bg, light);

        if (hasPing) {
            PlayerEntity player = (PlayerEntity) entity;
            GameProfile profile = player.getGameProfile();
            PlayerListEntry entry = net.getPlayerListEntry(profile.getId());
            caret += 10;
            String display = "%s ms    %s b".formatted(entry.getLatency(), MathUtils.round(PlayerUtils.player().distanceTo(entity), 10));
            mc.textRenderer.draw(Text.of(display), margin, caret, color, false, mat, vertexConsumers, type, bg, light);
        }
        if (hasArmor) {
            caret += 10;
            renderArmorBar(matrices, margin, caret, width - 10, damage, maxDamage);
        }
        if (hasHealth) {
            var living = (LivingEntity)entity;
            caret += 10;
            int health = (int)living.getHealth();
            int absorp = (int)living.getAbsorptionAmount();
            renderHealthBar(matrices, margin, caret, width - 10, living.getMaxHealth(), health, health, absorp);
        }
    }

    private void renderHealthBar(MatrixStack matrices, float x, float y, float width, float maxHealth, int lastHealth, int health, int absorption) {
        float height = 8;

        float ratioPrev = health / (maxHealth + absorption);
        float ratioCurr = lastHealth / (maxHealth + absorption);
        float ratioAbs = absorption / (maxHealth + absorption);

        float widthPrev = (int)(ratioPrev * width);
        float widthNormal = (int)(ratioCurr * width);
        float widthAbsorp = (int)(ratioAbs * width);

        fillRect(matrices, x, y, width, height, 0xAA000000); // base
        fillRect(matrices, x, y, widthPrev, height, 0x90D93F24); // last hp

        fillRect(matrices, x, y, widthNormal, height, 0xFFD93F24); // current hp
        fillRect(matrices, x, y, widthNormal, (int)(height * 0.33), 0xAAF18B78); // current hp glint

        fillRect(matrices, x + widthNormal, y, widthAbsorp, height, 0xFFFEDA00); // current abs
        fillRect(matrices, x + widthNormal, y, widthAbsorp, (int)(height * 0.33), 0xAAFEDA00); // current abs glint
        drawRect(matrices, x, y, width, height, 0xAA000000); // border
    }

    private void renderArmorBar(MatrixStack matrices, float x, float y, float width, int damage, int maxDamage) {
        float height = 8;

        float ratioPrev = (float)MathUtils.clamp((float)damage / (float)maxDamage, 0, 1);
        float widthPrev = (int)(ratioPrev * width);

        fillRect(matrices, x, y, width, height, 0xAA000000); // base
        fillRect(matrices, x, y, widthPrev, height, 0xAAD9D9D9); // armor bar
        fillRect(matrices, x, y, widthPrev, (int)(height * 0.33), 0xAAEBEBEB); // armor bar glint
        drawRect(matrices, x, y, width, height, 0xAA000000); // border
    }

    public static void fillRoundRect(MatrixStack matrices, float x, float y, float w, float h, float r, int color) {
        BufferBuilder buf = getBuffer(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        Matrix4f mat = matrices.peek().getPositionMatrix();

        buf.vertex(mat, x + w / 2F, y + h / 2F, 0).color(color);

        float[][] corners = {
                { x + w - r, y + r },
                { x + w - r, y + h - r},
                { x + r, y + h - r },
                { x + r, y + r }
        };

        for (int corner = 0; corner < 4; corner++) {
            int cornerStart = (corner - 1) * 90;
            int cornerEnd = cornerStart + 90;
            for (int i = cornerStart; i <= cornerEnd; i += 10) {
                float angle = (float)Math.toRadians(i);
                float rx = corners[corner][0] + (float)(Math.cos(angle) * r);
                float ry = corners[corner][1] + (float)(Math.sin(angle) * r);
                buf.vertex(mat, rx, ry, 0).color(color);
            }
        }

        buf.vertex(mat, corners[0][0], y, 0).color(color); // connect last to first vertex

        beginRendering();
        enableDepthTest();
        depthFunc(GL11.GL_ALWAYS);
        
        drawBuffer(buf);

        depthFunc(GL11.GL_LEQUAL);
        disableDepthTest();
        finishRendering();
    }

    private void fillRect(MatrixStack matrices, float x, float y, float w, float h, int color) {
        BufferBuilder buf = getBuffer(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f mat = matrices.peek().getPositionMatrix();

        buf.vertex(mat, x, y, 0).color(color);
        buf.vertex(mat, x + w, y, 0).color(color);
        buf.vertex(mat, x + w, y + h, 0).color(color);
        buf.vertex(mat, x, y + h, 0).color(color);

        beginRendering();
        enableDepthTest();
        depthFunc(GL11.GL_ALWAYS);
        
        drawBuffer(buf);
        
        depthFunc(GL11.GL_LEQUAL);
        disableDepthTest();
        finishRendering();
    }

    private void drawRect(MatrixStack matrices, float x, float y, float w, float h, int color) {
        drawHorLine(matrices, x, y, w, color);
        drawVerLine(matrices, x, y + 1, h - 2, color);
        drawVerLine(matrices, x + w - 1, y + 1, h - 2, color);
        drawHorLine(matrices, x, y + h - 1, w, color);
    }

    private void drawHorLine(MatrixStack matrices, float x, float y, float length, int color) {
        fillRect(matrices, x, y, length, 1, color);
    }

    private void drawVerLine(MatrixStack matrices, float x, float y, float length, int color) {
        fillRect(matrices, x, y, 1, length, color);
    }
}
