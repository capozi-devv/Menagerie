package net.capozi.menagerie.common.item;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.capozi.menagerie.common.entity.object.ChainsEntity;
import net.capozi.menagerie.common.network.FlashPacket;
import net.capozi.menagerie.common.render.FlashOverlayRenderer;
import net.capozi.menagerie.foundation.EffectInit;
import net.capozi.menagerie.foundation.SoundInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class CameraOfTheOthersideItem extends Item {
    public CameraOfTheOthersideItem(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity target, Hand hand) {
        if (target instanceof ServerPlayerEntity targetPlayer && target instanceof TrappedState trappedState) {
            BlockPos playerPos = target.getBlockPos();
            if (trappedState.isTrapped()) {
                if (!user.getWorld().isClient && user instanceof ServerPlayerEntity serverPlayer) {
                    FlashPacket.sendToTracking((ServerWorld) user.getWorld(), serverPlayer);
                    user.getWorld().playSound(null, playerPos, SoundInit.BUTTON_CLICK,
                            SoundCategory.MASTER, 5f, 1f);
                    user.getWorld().playSound(null, playerPos, SoundInit.FLASH,
                            SoundCategory.MASTER, 10f, 1f);
                    user.getWorld().playSound(null, playerPos, SoundInit.FILM_ADVANCE_LAST,
                            SoundCategory.MASTER, 1f, 1f);
                }
                // Find and discard nearby ChainsEntity BEFORE the ban
                // Kick and ban player
                target.removeStatusEffect(EffectInit.CHAINED_EFFECT);
                ban(user, target);
                List<ChainsEntity> chainsNearby = user.getWorld().getEntitiesByClass(
                        ChainsEntity.class,
                        targetPlayer.getBoundingBox().expand(10.0), // Adjust radius as needed
                        chains -> chains.isAlive()
                );
                for (ChainsEntity chains : chainsNearby) {
                    chains.discard();
                }
                // Broadcast message and reset trapped state
                MinecraftServer server = ((ServerWorld) user.getWorld()).getServer();
                GameProfile profile = targetPlayer.getGameProfile();
                Text deathMessage = Text.literal(profile.getName() + " was banished to the Otherside");
                server.getPlayerManager().broadcast(deathMessage, false);
                trappedState.setTrapped(false);
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }
    private void ban(PlayerEntity user, LivingEntity entity){
        ServerPlayerEntity target = (ServerPlayerEntity) entity;
        ServerPlayerEntity source = (ServerPlayerEntity) user;
        MinecraftServer server = source.getServer();
        if (server != null) {
            GameProfile profile = target.getGameProfile();
            BannedPlayerList banList = server.getPlayerManager().getUserBanList();
            if (!banList.contains(profile)) {
                BannedPlayerEntry entry = new BannedPlayerEntry(
                        profile,
                        null,
                        source.getName().toString(),
                        null,
                        "Camera"
                );
                banList.add(entry);
                target.networkHandler.disconnect(Text.literal("You have been sentenced to an eternity in the Otherside").formatted(Formatting.AQUA));
            }
        }
    }
}