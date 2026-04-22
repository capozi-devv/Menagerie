package net.capozi.menagerie.server.network;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.common.entity.TrinketsHelper;
import net.capozi.menagerie.foundation.ItemInit;
import net.capozi.menagerie.server.cca.DecryptorsEyeSenseAbilityComponent;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class DecryptorsEyeSensesCS2Packet {
    public static final Identifier ID = new Identifier(Menagerie.MOD_ID, "decryptor_sense");
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        DecryptorsEyeSenseAbilityComponent component = DecryptorsEyeSenseAbilityComponent.KEY.get(player);
        TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(player).isPresent() ? TrinketsApi.getTrinketComponent(player).get() : null;
        if (trinketComponent.isEquipped(ItemInit.DECRYPTORS_EYE)) {
            if (component.getCooldownTicks() <= 0) {
                component.setCooldownTicks(600);
                component.setSenseEnabled(true);
                component.sync();
                System.out.println(player);
            }
            player.sendMessage(Text.translatable("message.menagerie.ability_cooldown"));
        }
    }
}
