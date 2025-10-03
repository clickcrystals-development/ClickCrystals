package io.github.itzispyder.clickcrystals.util.misc.camera;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.interfaces.AccessorCamera;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

import java.util.Stack;

public class CameraRotator implements Global {

    private final CameraDataTracker tracker;
    private final Stack<CameraTicket> tickets;
    private float prevPitch, prevYaw;
    private CameraFinalizerCallback endCallback;
    private boolean lockCursor;

    public CameraRotator() {
        tracker = new CameraDataTracker();
        tickets = new Stack<>();
    }

    public boolean isCursorLocked() {
        return lockCursor;
    }

    public CameraRotator unlockCursor() {
        lockCursor = false;
        return this;
    }

    public CameraRotator lockCursor() {
        lockCursor = true;
        return this;
    }

    public void closeAllTickets() {
        if (tickets.isEmpty())
            return;

        tickets.forEach(CameraTicket::close);
        onFinish();
        tickets.clear();
    }

    public CameraRotator ready() {
        this.closeAllTickets();
        endCallback = null;
        return this;
    }

    public void closeCurrentTicket() {
        if (tickets.isEmpty())
            return;

        CameraTicket ticket = tickets.pop();
        ticket.close();
        onFinish();
        openCurrentTicket(); // reopen previous ticket
    }

    public CameraRotator setFinishCallback(CameraFinalizerCallback callback) {
        this.endCallback = callback;
        return this;
    }

    public void openCurrentTicket() {
        if (!tickets.isEmpty())
            tickets.peek().open();
    }

    public boolean isRunningTicket() {
        return !tickets.isEmpty() && !tickets.peek().isExpired();
    }

    public void onRenderTick() {
        if (tickets.isEmpty())
            return;

        if (tickets.peek().isExpired()) {
            closeCurrentTicket();
            unlockCursor();
            return;
        }
        if (!tickets.peek().isOpen()) {
            unlockCursor();
            return;
        }

        setPlayerRotation(
                tickets.peek().getProgressivePitch(),
                tickets.peek().getProgressiveYaw()
        );
    }

    public void onGameTick() {
        tracker.onTick();
    }

    private void onFinish() {
        if (endCallback != null)
            endCallback.onFinish(getPitch(), getYaw(), this);
        unlockCursor();
    }

    public CameraRotator addTicket(CameraTicket ticket) {
        if (!tickets.isEmpty())
            tickets.peek().close(); // pause current ticket for next
        tickets.push(ticket);
        return this;
    }

    public CameraRotator addTicket(float pitch, float yaw) {
        this.addTicket(pitch, yaw, 5);
        return this;
    }

    public CameraRotator addTicket(float pitch, float yaw, float speed) {
        return this.addTicket(pitch, yaw, speed, speed, false);
    }

    public CameraRotator addTicket(float pitch, float yaw, float pitchSpeed, float yawSpeed, boolean lockCursor) {
        return this.addTicket(new CameraTicket(this, Animations.FADE_IN_AND_OUT_SLIGHT,
                pitch, yaw, yawSpeed, pitchSpeed, lockCursor));
    }

    public CameraRotator addTicket(Vec3d dir) {
        return this.addTicket(dir, 5);
    }

    public CameraRotator addTicket(Vec3d dir, float speed) {
        return this.addTicket(dir, speed, speed, false);
    }

    public CameraRotator addTicket(Vec3d dir, float pitchSpeed, float yawSpeed, boolean lockCursor) {
        float[] rot = MathUtils.toPolar(dir.x, dir.y, dir.z);
        return this.addTicket(rot[0], rot[1], pitchSpeed, yawSpeed, lockCursor);
    }

    public CameraDataTracker getTracker() {
        return tracker;
    }

    public float getPitch() {
        return PlayerUtils.invalid() ? 0 : PlayerUtils.player().getPitch();
    }

    public float getYaw() {
        return PlayerUtils.invalid() ? 0 : PlayerUtils.player().getYaw();
    }

    private void setPlayerRotation(float pitch, float yaw) {
        if (PlayerUtils.invalid())
            return;
        ClientPlayerEntity p = PlayerUtils.player();

        // ceptea told me that setting same rotation in the
        // same tick would flag AC
        if (prevPitch != pitch) {
            prevPitch = pitch;
            p.setPitch(pitch);
        }
        if (prevYaw != yaw) {
            prevYaw = pitch;
            p.setYaw(yaw);
        }
    }

    private void setCameraRotation(float pitch, float yaw) {
        if (PlayerUtils.invalid())
            return;
        Camera c = mc.gameRenderer.getCamera();
        AccessorCamera cam = (AccessorCamera) c;
        cam.setCameraRotation(pitch, yaw);
    }
}
