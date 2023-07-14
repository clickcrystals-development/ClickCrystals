package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.HurtDirection;
import io.github.itzispyder.clickcrystals.scheduler.ArrayQueue;
import io.github.itzispyder.clickcrystals.scheduler.Queue;
import io.github.itzispyder.clickcrystals.scheduler.Scheduler;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class HurtDirectionHud implements HudRenderCallback {

    public static final int TEXTURE_WIDTH = 80;
    private final Queue<DisplayRequest> requests = new ArrayQueue<>(2);

    public HurtDirectionHud() {
        Scheduler.runTaskRepeating(() -> {
            for (DisplayRequest request : requests.getElements()) {
                if (request.isActive() && request.isAccepted()) {
                    request.tick();
                }
            }
        }, 1);
    }

    @Override
    public void onHudRender(DrawContext context, float delta) {
        for (DisplayRequest request : requests.getElements()) {
            if (request.isActive() && request.isAccepted()) {
                renderDirection(context, request.getDirection());
            }
        }
    }

    public void receiveRequest(DisplayRequest request) {
        for (DisplayRequest remaining : requests.getElements()) {
            if (!remaining.isActive()) {
                requests.dequeue(remaining);
            }
        }

        request.setAccepted(requests.getElements().stream().noneMatch(r -> r.getId().equals(request.getId())));
        if (request.isAccepted()) {
            requests.enqueue(request);
        }
    }

    public void renderDirection(DrawContext context, Direction dir) {
        Identifier texture;
        switch (dir) {
            case NORTH -> texture = GuiTextures.HURT_DIRECTION_NORTH;
            case EAST -> texture = GuiTextures.HURT_DIRECTION_EAST;
            case WEST -> texture = GuiTextures.HURT_DIRECTION_WEST;
            default -> texture = GuiTextures.HURT_DIRECTION_SOUTH;
        }

        int scale = (int)(TEXTURE_WIDTH * getDisplayScale());
        int w = context.getScaledWindowWidth();
        int h = context.getScaledWindowHeight();
        int x = w / 2 - scale / 2;
        int y = h / 2 - scale / 2;

        context.drawTexture(texture, x, y, 0, 0, scale, scale, scale, scale);
    }

    public double getDisplayScale() {
        return Module.get(HurtDirection.class).displayScale.getVal();
    }

    public static class DisplayRequest {

        private final Direction direction;
        private int remainingTime;
        private boolean accepted, active;

        public DisplayRequest(Direction direction, int displayTime) {
            this.direction = direction;
            this.remainingTime = displayTime;
            this.accepted = false;
            this.active = true;
        }

        public void tick() {
            if (remainingTime >= 1) {
                remainingTime -= 1;
            }
            else {
                active = false;
            }
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isAccepted() {
            return accepted;
        }

        public void setAccepted(boolean accepted) {
            this.accepted = accepted;
        }

        public Direction getDirection() {
            return direction;
        }

        public int getRemainingTime() {
            return remainingTime;
        }

        public void setRemainingTime(int remainingTime) {
            this.remainingTime = remainingTime;
        }

        public String getId() {
            return "direction:" + direction.name().toLowerCase();
        }
    }
}
