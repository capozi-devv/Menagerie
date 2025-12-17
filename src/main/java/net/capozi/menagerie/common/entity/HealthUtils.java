package net.capozi.menagerie.common.entity;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import java.util.UUID;

public class HealthUtils {
    public static final UUID EXTRA_HEARTS_UUID = UUID.fromString("5a1b9c9e-63ea-46e1-9d13-ecb6a20f125d");
    public static final UUID REDUCED_HEALTH_UUID = UUID.fromString("deadbeef-dead-beef-dead-beefdeadbeef");
    public static void addExtraHearts(PlayerEntity player, double extraHearts) {
        var attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attribute != null && attribute.getModifier(EXTRA_HEARTS_UUID) == null) {
            attribute.addPersistentModifier(new EntityAttributeModifier(
                    EXTRA_HEARTS_UUID,
                    "Extra Hearts Modifier",
                    extraHearts, // e.g. 2.0 = 1 heart, 10.0 = 5 hearts
                    EntityAttributeModifier.Operation.ADDITION
            ));
        }
    }
    public static void removeExtraHearts(PlayerEntity player) {
        var attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attribute != null){
            if (attribute.getModifier(EXTRA_HEARTS_UUID)!=null){
                attribute.removeModifier(EXTRA_HEARTS_UUID);
            }
            if (attribute.getModifier(REDUCED_HEALTH_UUID)!=null){
                attribute.removeModifier(REDUCED_HEALTH_UUID);
            }
        }
    }
    public static void reduceMaxHealth(PlayerEntity player, float heartsToRemove) {
        double amount = heartsToRemove * 2.0; // 1 heart = 2 HP
        EntityAttributeInstance attr = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attr == null) return;
        EntityAttributeModifier existing = attr.getModifier(REDUCED_HEALTH_UUID);
        if (existing != null) {
            attr.removeModifier(REDUCED_HEALTH_UUID);
        }
        EntityAttributeModifier modifier = new EntityAttributeModifier(
                REDUCED_HEALTH_UUID,
                "Reduced max health",
                -amount,
                EntityAttributeModifier.Operation.ADDITION
        );
        attr.addPersistentModifier(modifier);
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }
}
