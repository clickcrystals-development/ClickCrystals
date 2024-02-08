package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;

import java.util.Timer;
import java.util.TimerTask;

public class CpsRelativeHud extends TextHud implements Listener {
    private static final long CPS_UPDATE_INTERVAL = 1000; // 1 second
    private long lastUpdateTime = System.currentTimeMillis();
    private int leftClicks = 0;
    private int rightClicks = 0;
    private double cps = 0.0;

    public CpsRelativeHud() {
        super("cps-hud", 10, 90, 50, 12);
        system.addListener(this);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, CPS_UPDATE_INTERVAL, CPS_UPDATE_INTERVAL);
    }

    @Override
    public String getText() {
        String cpsString = String.format("%.2f", cps);
        if (cpsString.endsWith(".00")) {
            cpsString = cpsString.substring(0, cpsString.length() - 3);
        }
        return cpsString + " CPS";
    }

    @EventHandler
    private void onMouseClick(MouseClickEvent event) {
        if (!event.isCancelled()) {
            // Increment left or right clicks based on the mouse button identifier
            int button = event.getButton();
            if (button == 0) { // Left mouse button
                leftClicks++;
            } else if (button == 1) { // Right mouse button
                rightClicks++;
            }
        }
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Boolean.TRUE.equals(Module.getFrom(InGameHuds.class, m -> m.hudCps.getVal()));
    }

    private void update() {
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastUpdateTime;

        if (timeElapsed >= CPS_UPDATE_INTERVAL) {
            int totalClicks = leftClicks + rightClicks;
            cps = (double) totalClicks / (double) CPS_UPDATE_INTERVAL * 1000.0;

            leftClicks = 0;
            rightClicks = 0;

            lastUpdateTime = currentTime;
        }
    }
}
