package net.capozi.menagerie.server.network.packet.serverbound;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.foundation.ItemInit;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class TrickRoomNbtSyncCS2Packet {
    public static final Identifier ID = new Identifier(Menagerie.MOD_ID, "trick_room_nbt");
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        if (player.getMainHandStack().isOf(ItemInit.TRICK_ROOM)) {
            if (player.getItemCooldownManager().isCoolingDown(ItemInit.TRICK_ROOM)) return;
            ItemStack stack = player.getMainHandStack();
            NbtCompound nbt = stack.getNbt();
            int i = nbt.getInt("TrickRoomAbilityMode");
            switch (i) {
                case 0 -> {
                    nbt.putInt("TrickRoomAbilityMode", 1);
                    player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 1f, 1f);
                }
                case 1 -> {
                    nbt.putInt("TrickRoomAbilityMode", 2);
                    player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 1f, 1f);
                }
                case 2 -> {
                    nbt.putInt("TrickRoomAbilityMode", 0);
                    player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 1f, 1f);
                }
            }
            if (!player.getAbilities().creativeMode) {
                player.getItemCooldownManager().set(ItemInit.TRICK_ROOM, 60);
            }
        }
    }
}
