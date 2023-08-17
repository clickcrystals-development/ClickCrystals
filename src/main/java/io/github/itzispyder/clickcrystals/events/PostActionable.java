package io.github.itzispyder.clickcrystals.events;

import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.List;

public interface PostActionable {

    List<PostAction> getActions();

    default void action() {
        if (PlayerUtils.playerNotNull()) {
            ClientPlayerEntity p = PlayerUtils.player();
            MinecraftClient mc = MinecraftClient.getInstance();
            getActions().forEach(action -> action.trigger(mc, p));
        }
    }

    default void post(PostAction action) {
        if (getActions() != null) {
            getActions().add(action);
        }
    }

    default void remove(PostAction action) {
        if (getActions() != null) {
            getActions().remove(action);
        }
    }

    default boolean contains(PostAction action) {
        return getActions() != null && action != null && getActions().contains(action);
    }

    @FunctionalInterface
    interface PostAction {
        void trigger(MinecraftClient client, ClientPlayerEntity player);
    }
}
