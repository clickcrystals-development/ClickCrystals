package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;

public final class NbtUtils {

    public static int getEnchantLvL(ItemStack stack, RegistryKey<Enchantment> enchant) {
        return stack.getEnchantments().getLevel(enchant);
    }
}
