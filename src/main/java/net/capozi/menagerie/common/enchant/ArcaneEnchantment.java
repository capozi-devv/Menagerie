package net.capozi.menagerie.common.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;

public class ArcaneEnchantment extends Enchantment {
    public ArcaneEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }
    @Override
    public boolean isTreasure() {
        return true;
    }
    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }
    @Override
    public boolean isAvailableForRandomSelection() {
        return false; // include in loot table/random selection
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }
    @Override
    protected boolean canAccept(Enchantment other) {
        return other == Enchantments.MENDING || other == Enchantments.UNBREAKING;
    }
}

