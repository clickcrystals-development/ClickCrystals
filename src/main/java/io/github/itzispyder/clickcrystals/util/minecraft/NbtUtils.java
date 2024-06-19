package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public final class NbtUtils {

    public static int getEnchantLvL(ItemStack stack, RegistryKey<Enchantment> enchant) {
        return stack.getEnchantments().getLevel((RegistryEntry<Enchantment>) enchant);
    }
}
