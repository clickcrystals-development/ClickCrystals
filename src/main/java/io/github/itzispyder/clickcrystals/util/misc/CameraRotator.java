package io.github.itzispyder.clickcrystals.util.misc;

import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.Randomizer;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CameraRotator {

    private static final AtomicBoolean running = new AtomicBoolean(false);
    private final EndAction onFinish;
    private final List<Goal> goals;
    private final AtomicReference<Goal> currentGoal;
    private final AtomicBoolean skipRequestReceived, wasCancelled;
    protected boolean debugMessages;

    private CameraRotator(List<Goal> goals, EndAction onFinish) {
        this.onFinish = onFinish;
        this.goals = goals;
        this.currentGoal = new AtomicReference<>();
        skipRequestReceived = wasCancelled = new AtomicBoolean(false);
    }

    public boolean isRunning() {
        return running.get();
    }

    public AtomicReference<Goal> getCurrentGoal() {
        return currentGoal;
    }

    public void cancel() {
        running.set(false);
        wasCancelled.set(true);
    }

    public void skip() {
        skipRequestReceived.set(true);
        if (goals.size() <= 1) {
            wasCancelled.set(true);
        }
    }

    public void start() {
        if (isRunning()) {
            if (debugMessages) {
                ChatUtils.sendPrefixMessage("§cA camera rotator is already active!");
            }
            return;
        }
        wasCancelled.set(false);
        running.set(true);

        CompletableFuture.runAsync(() -> {
            for (Goal goal : goals) {
                if (!isRunning()) break;

                setGoalAndTarget(goal);

                try {
                    Thread.sleep(50);
                }
                catch (Exception ignore) {}
            }

            ClientPlayerEntity p = PlayerUtils.player();
            onFinish.accept(MathUtils.wrapDegrees(p.getPitch()), MathUtils.wrapDegrees(p.getYaw()), wasCancelled.get());
            running.set(false);
        });
    }

    private synchronized void setGoalAndTarget(Goal goal) {
        if (PlayerUtils.playerNotNull()) {
            currentGoal.set(goal);
            Goal rot = getRotation();

            if (rot.pitch == goal.pitch && rot.yaw == goal.yaw) {
                if (debugMessages) {
                    ChatUtils.sendPrefixMessage( rot.pitch + " is already " + goal.pitch + ", and " +  rot.yaw + " is already " + goal.yaw);
                }
                skipRequestReceived.set(false);
                return;
            }

            int deltaPitch = (int)MathUtils.angleBetween(rot.pitch, goal.pitch); // vertical
            int deltaYaw = (int)MathUtils.angleBetween(rot.yaw, goal.yaw); // plane
            boolean shouldPitchAdd = goal.pitch > rot.pitch;
            boolean shouldYawAdd = goal.yaw > rot.yaw;
            int progressPitch = 0;
            int progressYaw = 0;

            if (debugMessages) {
                ChatUtils.sendPrefixMessage("Targeting goal §7(" + goal.pitch + ", " + goal.yaw + ")§f from §7(" + rot.pitch + ", " + rot.yaw + ")§f, distance §7(" + deltaPitch + ", " + deltaYaw + ")");
            }

            while (isRunning() && !skipRequestReceived.get() && (progressPitch < deltaPitch || progressYaw < deltaYaw)) {
                if (progressPitch < deltaPitch) {
                    progressPitch++;
                    rot.pitch = rot.pitch + (shouldPitchAdd ? 1 : -1);
                    PlayerUtils.player().setPitch(rot.pitch);
                }
                if (progressYaw < deltaYaw) {
                    progressYaw++;
                    rot.yaw = rot.yaw + (shouldYawAdd ? 1 : -1);
                    PlayerUtils.player().setYaw(rot.yaw);
                }

                try {
                    Thread.sleep(Randomizer.rand(1, 6));
                }
                catch (Exception ignore) {}
            }

            if (debugMessages && !skipRequestReceived.get()) {
                ChatUtils.sendPrefixMessage("Done! Set your rotation to §7pitch=" + goal.pitch + ", yaw=" + goal.yaw);
            }
            skipRequestReceived.set(false);
        }
    }

    private synchronized Goal getRotation() {
        if (PlayerUtils.playerNotNull()) {
            float pitch = PlayerUtils.player().getPitch();
            float yaw = PlayerUtils.player().getYaw();
            return new Goal(pitch, yaw);
        }
        return new Goal(0, 0);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private final List<Goal> goals;
        private EndAction onFinish;
        private boolean debugMessages;

        public Builder() {
            this.goals = new ArrayList<>();
            this.onFinish = (pitch, yaw, wasCancelled) -> {};
            this.debugMessages = false;
        }

        public Builder enableDebug() {
            debugMessages = true;
            return this;
        }

        public Builder addGoal(Goal goal){
            if (goal != null) {
                goals.add(goal);
            }
            return this;
        }

        public Builder onFinish(EndAction onFinish) {
            this.onFinish = onFinish;
            return this;
        }

        public CameraRotator build() {
            boolean bl = debugMessages;
            return new CameraRotator(goals, onFinish) {{
                this.debugMessages = bl;
            }};
        }
    }

    public static class Goal {
        private int pitch, yaw;

        public Goal(float pitch, float yaw) {
            this.pitch = (int)MathUtils.wrapDegrees(pitch);
            this.yaw = (int)MathUtils.wrapDegrees(yaw);
        }

        public final int getPitch() {
            return pitch;
        }

        public final int getYaw() {
            return yaw;
        }
    }
    
    @FunctionalInterface
    public interface EndAction {
        void accept(float pitch, float yaw, boolean wasCancelled);
    }
}
