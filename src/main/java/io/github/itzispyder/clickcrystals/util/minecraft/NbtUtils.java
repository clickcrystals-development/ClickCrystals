package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public final class NbtUtils {

    public static int getEnchantLvL(ItemStack stack, Enchantment enchant) {
        return stack.getEnchantments().getLevel(enchant);
    }
}
