package net.capozi.menagerie.common.item;

import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.*;
import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ClickType;
import net.minecraft.world.event.GameEvent;

public class DecryptorsEyeTrinketItem extends TrinketItem {
    public DecryptorsEyeTrinketItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);
        entity.getWorld().playSound(null, entity.getBlockPos(), SoundInit.DECRYPTORS_EYE, SoundCategory.PLAYERS, 1f, 1f);
    }
}
