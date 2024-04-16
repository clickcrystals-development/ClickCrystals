package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class NoInteractions extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> allowChests = scGeneral.add(createBoolSetting()
            .name("allow-chests")
            .description("Allow interactions of chests, trapped chests, ender chets.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> allowShulker = scGeneral.add(createBoolSetting()
            .name("allow-shulker")
            .description("Allow interactions of shulker boxes.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> allowRedstone = scGeneral.add(createBoolSetting()
            .name("allow-redstone")
            .description("Allow interactions of hoppers, droppers, dispensers.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> allowDoors = scGeneral.add(createBoolSetting()
            .name("allow-doors")
            .description("Allow interactions of doors, trapdoors, fence gates.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> allowDecor = scGeneral.add(createBoolSetting()
            .name("allow-decor")
            .description("Allow interactions of crafting tables, anvils, note blocks, jukeboxes, furnaces, blast furnaces, smokers.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> allowSign = scGeneral.add(createBoolSetting()
            .name("allow-sign-editing")
            .description("Allow sign interactions or editing.")
            .def(false)
            .build()
    );

    public NoInteractions() {
        super("no-interactions", Categories.MISC, "Prevents opening certain containers(e-chests,chests,etc)");
    }

    public boolean canInteract(Block b) {
        String key = b.getTranslationKey();

        if (key.contains("chest")) {
            return allowChests.getVal();
        }
        else if (key.contains("shulker_box")) {
            return allowShulker.getVal();
        }
        else if (b == Blocks.DISPENSER || b == Blocks.HOPPER || b == Blocks.DROPPER) {
            return allowRedstone.getVal();
        }
        else if (key.contains("door") || key.contains("fence")) {
            return allowDoors.getVal();
        }
        else if (b == Blocks.CRAFTING_TABLE || b == Blocks.ANVIL || b == Blocks.NOTE_BLOCK || b == Blocks.JUKEBOX || b == Blocks.SMOKER || key.contains("furnace")) {
            return allowDecor.getVal();
        }
        else if (key.contains("sign")) {
            return allowSign.getVal();
        }
        return true;
    }
}