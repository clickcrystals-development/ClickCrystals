package io.github.itzispyder.clickcrystals.events;

import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.List;
import java.util.function.Consumer;

public interface PostActionable {

    List<PostAction> getActions();

    default void action() {
        if (PlayerUtils.valid()) {
            ClientPlayerEntity p = PlayerUtils.player();
            MinecraftClient mc = MinecraftClient.getInstance();
            getActions().forEach(action -> action.trigger(mc, p));
        }
    }

    default void post(PostAction action) {
        notNull(list -> list.add(action));
    }

    default void remove(PostAction action) {
        notNull(list -> list.remove(action));
    }

    default boolean contains(PostAction action) {
        return getActions() != null && action != null && getActions().contains(action);
    }

    private void notNull(Consumer<List<PostAction>> action) {
        if (getActions() != null) {
            action.accept(getActions());
        }
    }

    @FunctionalInterface
    interface PostAction {
        void trigger(MinecraftClient client, ClientPlayerEntity player);
    }
}
