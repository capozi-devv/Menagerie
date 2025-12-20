package net.capozi.menagerie.server.network;

import net.capozi.menagerie.client.render.FlashOverlayRenderer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class FlashPacket {
    public static final Identifier ID = new Identifier("menagerie", "flash_screen");
    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, sender) -> {
            client.execute(() -> {
                FlashOverlayRenderer.triggerFlash();
            });
        });
    }
    public static void sendToTracking(ServerWorld world, Entity sourcePlayer) {
        PacketByteBuf buf = PacketByteBufs.create();
        for (ServerPlayerEntity targetPlayer : world.getPlayers()) {
            if (world.isChunkLoaded(targetPlayer.getBlockPos())) {
                double distanceSquared = targetPlayer.squaredDistanceTo(sourcePlayer);
                if (distanceSquared < 150 * 150) { // viewing distance
                    ServerPlayNetworking.send(targetPlayer, ID, buf);
                    FlashOverlayRenderer.triggerFlash();
                }
            }
        }
    }
}

