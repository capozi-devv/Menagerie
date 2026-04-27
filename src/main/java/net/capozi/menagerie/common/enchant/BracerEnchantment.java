package net.capozi.menagerie.common.enchant;

import net.capozi.menagerie.common.item.PunchUpStarItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class BracerEnchantment extends Enchantment {
    public BracerEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof PunchUpStarItem;
    }
    @Override
    public boolean isTreasure() {
        return false;
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
    @Override
    protected boolean canAccept(Enchantment other) {
        return other == Enchantments.MENDING || other == Enchantments.UNBREAKING;
    }
}
