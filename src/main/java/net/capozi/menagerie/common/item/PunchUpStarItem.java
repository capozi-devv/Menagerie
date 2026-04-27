package net.capozi.menagerie.common.item;

import net.capozi.menagerie.foundation.DamageTypeInit;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.server.cca.PunchUpComboComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PunchUpStarItem extends SwordItem {
    public PunchUpStarItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Text combo_text = Text.of("0");
        if (stack.getHolder() instanceof PlayerEntity player) {
            PunchUpComboComponent combo = PunchUpComboComponent.KEY.get(player);
            combo_text = Text.of(String.valueOf(combo.getCombo()));
        }
        tooltip.add(1, Text.translatable("tooltip.menagerie.punch_up_combo").append(combo_text));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        int bracer = EnchantmentHelper.getLevel(EnchantInit.BRACER, stack);
        if (bracer > 0) {
            return UseAction.BLOCK;
        }
        return super.getUseAction(stack);
    }
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            PunchUpComboComponent component = PunchUpComboComponent.KEY.get(player);
            target.damage(new DamageSource(player.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypeInit.PUNCH_COMBO)), (float)component.getCombo());
            if (component.getCombo() < component.max_combo) {
                component.increment();
            }
        }
        return super.postHit(stack, target, attacker);
    }
}
