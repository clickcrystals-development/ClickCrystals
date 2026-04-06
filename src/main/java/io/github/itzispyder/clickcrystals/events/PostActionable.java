package io.github.itzispyder.clickcrystals.events;

import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public interface PostActionable {

    List<PostAction> getActions();

    default void action() {
        if (PlayerUtils.valid()) {
            LocalPlayer p = PlayerUtils.player();
            Minecraft mc = Minecraft.getInstance();
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
        void trigger(Minecraft client, LocalPlayer player);
    }
}
