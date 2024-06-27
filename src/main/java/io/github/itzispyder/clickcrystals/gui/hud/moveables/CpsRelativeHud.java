package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.misc.Pair;

import java.util.ArrayList;

public class CpsRelativeHud extends TextHud implements Listener {

    private final Pair<Integer, Integer> clicks;
    private final Pair<ClickQueue, ClickQueue> clickQueues;
    private final Pair<Double, Double> clickSpeeds;
    private int ticks;
    private boolean enabled;

    public CpsRelativeHud() {
        super("cps-hud", 10, 60, 50, 16);
        this.clicks = Pair.of(0, 0);
        this.clickQueues = Pair.of(new ClickQueue(), new ClickQueue());
        this.clickSpeeds = Pair.of(0.0, 0.0);
    }

    @Override
    public String getText() {
        return clickSpeeds.left + " • " + clickSpeeds.right;
    }

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        int updateSpeed = 7;

        if (ticks++ < 20 / updateSpeed) {
            return;
        }
        ticks = 0;

        clickQueues.left.push(clicks.left);
        clickQueues.right.push(clicks.right);

        if (clickQueues.left.size() > 15) {
            clickQueues.left.pop();
        }
        if (clickQueues.right.size() > 15) {
            clickQueues.right.pop();
        }

        clickSpeeds.left = MathUtils.round(MathUtils.avg(clickQueues.left) * updateSpeed, 10);
        clickSpeeds.right = MathUtils.round(MathUtils.avg(clickQueues.right) * updateSpeed, 10);
        clicks.left = 0;
        clicks.right = 0;
    }

    @EventHandler
    private void onMouseClick(MouseClickEvent e) {
        if (e.getAction().isDown() && e.isScreenNull()) {
            int b = e.getButton();

            if (b == 0) {
                clicks.left++;
            }
            else if (b == 1) {
                clicks.right++;
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public boolean canRender() {
        boolean bl = super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudCps.getVal());

        if (bl && !enabled) {
            enabled = true;
            system.addListener(this);
        }
        else if (!bl && enabled) {
            enabled = false;
            system.removeListener(this);
        }

        return bl;
    }

    static class ClickQueue extends ArrayList<Integer> {
        public void pop() {
            if (!isEmpty()) {
                remove(size() - 1);
            }
        }

        public void push(int i) {
            add(0, i);
        }
    }
}
