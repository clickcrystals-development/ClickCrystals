package io.github.itzispyder.clickcrystals.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class NbtUtils {

    public static int getEnchantLvL(ItemStack stack, Enchantment enchant) {
        NbtList list = stack.getEnchantments();
        for (int i = 0; i < list.size(); i++) {
            try {
                NbtCompound nbt = list.getCompound(i);
                Identifier id = Identifier.tryParse(nbt.getString("id"));
                Identifier id2 = Registries.ENCHANTMENT.getId(enchant);
                if (id.toTranslationKey().equalsIgnoreCase(id2.toTranslationKey())) {
                    return nbt.getInt("lvl");
                }
            }
            catch (Exception ignore) {}
        }
        return 0;
    }
}
