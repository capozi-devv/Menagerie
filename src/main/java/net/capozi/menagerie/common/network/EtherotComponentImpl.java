package net.capozi.menagerie.common.network;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.foundation.EffectInit;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EtherotComponentImpl implements EtherotComponent{
    private boolean hasEtherot = false;
    private static final int CHECK_INTERVAL_TICKS = 30 * 20; // 30 seconds
    private static final Map<UUID, Integer> playerTimers = new HashMap<>();
    private static final int UPGRADE_INTERVAL_TICKS = 20 * 60 * 20; // 20 minutes = 24000 ticks
    private static final Map<UUID, Integer> etherotTimers = new HashMap<>();
    private static boolean consumeAmethystShard(PlayerEntity player) {
        DefaultedList<ItemStack> inv = player.getInventory().main;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.get(i);
            if (stack.getItem() == Items.AMETHYST_SHARD && stack.getCount() > 0) {
                stack.decrement(1);
                return true;
            }
        }
        return false;
    }
    private static void applyEtherotEffect(PlayerEntity player) {
        StatusEffectInstance etherot = new StatusEffectInstance(
                EffectInit.ETHEROT_EFFECT.value(),
                200, // Duration in ticks (10 sec)
                0,   // Amplifier
                false, // ambient
                false,
                true   // showParticles
        );
        player.addStatusEffect(etherot);
    }
    @Override
    public boolean hasEtherot() {
        return hasEtherot;
    }
    @Override
    public void setHasEtherot(boolean value) {
        this.hasEtherot = value;
    }
    @Override
    public void tickEtherot(PlayerEntity player) {
        UUID uuid = player.getUuid();
        BoundAccursedComponent accursed = Menagerie.getBoundAccursed().get(player);
        BoundArtifactComponent artifact = Menagerie.getBoundArtifact().get(player);
        if (hasEtherot() && !player.hasStatusEffect(EffectInit.ETHEROT_EFFECT.value())) {
            player.addStatusEffect(new StatusEffectInstance(
                    EffectInit.ETHEROT_EFFECT.value(),
                    200, // 10 seconds
                    0,
                    false,
                    false,
                    true
            ));
        }
        if (!accursed.hasAccursed() && !artifact.hasArtifact()) {
            return;
        }
        int time = playerTimers.getOrDefault(uuid, 0);
        if (time <= 0) {
            if (!consumeAmethystShard(player)) {
                applyEtherotEffect(player);
                setHasEtherot(true);
            } else {
                player.removeStatusEffect(EffectInit.ETHEROT);
                setHasEtherot(false);
            }
            playerTimers.put(uuid, CHECK_INTERVAL_TICKS);
        } else {
            playerTimers.put(uuid, time - 1);
        }
        // Upgrade logic
        StatusEffectInstance current = player.getStatusEffect(EffectInit.ETHEROT_EFFECT.value());
        if (current != null) {
            int timer = etherotTimers.getOrDefault(uuid, 0);
            if (timer >= UPGRADE_INTERVAL_TICKS) {
                int currentAmplifier = current.getAmplifier();
                if (currentAmplifier < 4) {
                    player.removeStatusEffect(EffectInit.ETHEROT_EFFECT.value());
                    player.addStatusEffect(new StatusEffectInstance(
                            EffectInit.ETHEROT_EFFECT.value(),
                            200,
                            currentAmplifier + 1,
                            false,
                            false,
                            true
                    ));
                }
                etherotTimers.put(uuid, 0);
            } else {
                etherotTimers.put(uuid, timer + 1);
            }
        } else {
            etherotTimers.remove(uuid);
        }
    }
    @Override
    public void readFromNbt(NbtCompound tag) {
        this.hasEtherot = tag.getBoolean("HasEtherot");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("HasEtherot", hasEtherot);
    }
}
