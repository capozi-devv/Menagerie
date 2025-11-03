package net.capozi.menagerie.common.enchant;

import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class PogoEnchantment extends Enchantment {
    public PogoEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isOf(ItemInit.HEAVYIRON_LONGSPOON);
    }
    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
    @Override
    public boolean isAvailableForRandomSelection() {
        return true;
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
