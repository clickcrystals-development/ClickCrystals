package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;

public class SpectatorSight extends DummyModule {

    public SpectatorSight() {
        super("spectator-sight", Categories.RENDER, "Renders invisible entities the way you seem them in spectator mode. Never get sneak attacked again!");
    }
}