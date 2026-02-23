package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public final class NbtUtils {

    public static int getEnchantLvL(ItemStack stack, RegistryKey<Enchantment> enchant) {
        for (RegistryEntry<Enchantment> entry : stack.getEnchantments().getEnchantments())
            if (entry.getKey().isPresent() && entry.getKey().get() == enchant)
                return stack.getEnchantments().getLevel(entry);
        return 0;
    }

    public static boolean hasEnchant(ItemStack item, String enchantmentId) {
        Identifier id = Identifier.ofVanilla(enchantmentId);
        for (RegistryEntry<Enchantment> enchant : item.getEnchantments().getEnchantments())
            if (enchant.getIdAsString().equals(id.toString()))
                return true;
        return false;
    }

    public static boolean hasPotion(ItemStack item, String potionId) {
        PotionContentsComponent potion = item.get(DataComponentTypes.POTION_CONTENTS);
        if (potion == null || potion.potion().isEmpty())
            return false;
        return potion.potion().get().getIdAsString().toLowerCase()
                .contains(potionId.toLowerCase());
    }

    public static boolean hasPotionOrEnchant(ItemStack item, String tag) {
        return hasEnchant(item, tag) || hasPotion(item, tag);
    }
}
