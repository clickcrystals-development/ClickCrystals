package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;

public final class NbtUtils {

    public static int getEnchantLvL(ItemStack stack, ResourceKey<Enchantment> enchant) {
        for (Holder<Enchantment> entry : stack.getEnchantments().keySet())
            if (entry.unwrapKey().isPresent() && entry.unwrapKey().get() == enchant)
                return stack.getEnchantments().getLevel(entry);
        return 0;
    }

    public static boolean hasEnchant(ItemStack item, String enchantmentId) {
        Identifier id = Identifier.withDefaultNamespace(enchantmentId);
        for (Holder<Enchantment> enchant : item.getEnchantments().keySet())
            if (enchant.getRegisteredName().equals(id.toString()))
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
