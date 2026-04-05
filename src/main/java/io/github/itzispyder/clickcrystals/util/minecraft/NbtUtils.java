package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public final class NbtUtils {

    public static int getEnchantLvL(ItemStack stack, Holder<Enchantment> enchant) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchant, stack);
    }

    public static boolean hasEnchant(ItemStack item, String enchantmentId) {
        for (Holder<Enchantment> holder : item.getEnchantments().keySet())
            if (holder.getRegisteredName().equalsIgnoreCase(enchantmentId))
                return true;
        return false;
    }

    public static boolean hasPotion(ItemStack item, String potionId) {
        PotionContents potion = item.get(DataComponents.POTION_CONTENTS);
        if (potion == null || potion.potion().isEmpty())
            return false;
        return potion.potion().get().getRegisteredName().toLowerCase()
                .contains(potionId.toLowerCase());
    }

    public static boolean hasPotionOrEnchant(ItemStack item, String tag) {
        return hasEnchant(item, tag) || hasPotion(item, tag);
    }
}
