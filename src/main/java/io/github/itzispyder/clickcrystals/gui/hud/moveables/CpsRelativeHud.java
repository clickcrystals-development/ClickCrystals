package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
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
    private Timer timer;

    public CpsRelativeHud() {
        super("cps-hud", 10, 60, 50, 12);
        system.addListener(this);
        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCps();
            }
        }, CPS_UPDATE_INTERVAL, CPS_UPDATE_INTERVAL);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    @Override
    public String getText() {
        String cpsString = String.format("%.2f", cps);
        if (cpsString.endsWith(".00")) {
            cpsString = cpsString.substring(0, cpsString.length() - 3);
        }
        return cpsString + " cps";
    }

    @EventHandler
    private void onMouseClick(MouseClickEvent event) {
        if (!event.isCancelled() && event.getAction() == ClickType.RELEASE) {
            int button = event.getButton();
            if (button == 0) {
                leftClicks++;
            } else if (button == 1) {
                rightClicks++;
            }
        }
    }

    @Override
    public boolean canRender() {
        boolean canRender = super.canRender() && Boolean.TRUE.equals(Module.getFrom(InGameHuds.class, m -> m.hudCps.getVal()));
        if (canRender) {
            if (timer == null) {
                startTimer();
            }
        } else {
            stopTimer();
        }
        return canRender;
    }

    private void updateCps() {
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
