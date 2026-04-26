package net.capozi.menagerie.common.item;

import net.capozi.menagerie.foundation.DamageTypeInit;
import net.capozi.menagerie.server.cca.PunchUpComboComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;

public class PunchUpStarItem extends SwordItem {
    public PunchUpStarItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            PunchUpComboComponent component = PunchUpComboComponent.KEY.get(player);
            target.damage(new DamageSource(player.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypeInit.PUNCH_COMBO)), (float)component.getCombo());
            component.increment();
        }
        return super.postHit(stack, target, attacker);
    }
}
