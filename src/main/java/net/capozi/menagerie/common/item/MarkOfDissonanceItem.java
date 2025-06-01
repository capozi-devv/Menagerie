package net.capozi.menagerie.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class MarkOfDissonanceItem extends Item {
    public MarkOfDissonanceItem(Item.Settings settings) {
        super(settings);
    }
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player) {
            player.getHungerManager().setFoodLevel(20);
            player.getHungerManager().setSaturationLevel(0.0F);
            player.setFireTicks(0);
            player.extinguish();
        }
    }
}
