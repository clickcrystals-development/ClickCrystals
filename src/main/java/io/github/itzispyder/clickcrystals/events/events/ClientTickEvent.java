package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Event;

/**
 * Client tick event.
 * Called every 50 milliseconds when client ticks.
 */
public class ClientTickEvent extends Event {

    /**
     * Pre tick
     */
    public static class Pre extends ClientTickEvent {

        public Pre() {

        }
    }

    /**
     * Post tick
     */
    public static class End extends ClientTickEvent {

        public End() {

        }
    }
}
