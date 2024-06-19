package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public final class NbtUtils {

    public static int getEnchantLvL(ItemStack stack, RegistryKey<Enchantment> enchant) {
        for (RegistryEntry<Enchantment> entry : stack.getEnchantments().getEnchantments())
            if (entry.getKey().isPresent() && entry.getKey().get() == enchant)
                return stack.getEnchantments().getLevel(entry);
        return 0;
    }
}
