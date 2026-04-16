package net.capozi.menagerie.common.item.material;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ReachOftheVoidMaterial implements ToolMaterial {
    @Override
    public int getDurability() {
        return 25;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 0;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }
}
