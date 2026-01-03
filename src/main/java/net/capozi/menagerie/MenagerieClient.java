package net.capozi.menagerie;

import net.capozi.menagerie.common.entity.client.CircleBeamRenderer;
import net.capozi.menagerie.client.lodestone.vfx.AllVFX;
import net.capozi.menagerie.foundation.ParticleInit;
import net.capozi.menagerie.server.network.FlashPacket;
import net.capozi.menagerie.client.render.FlashOverlayRenderer;
import net.capozi.menagerie.foundation.EnchantInit;
import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.common.entity.client.ChainsEntityModel;
import net.capozi.menagerie.client.render.ModModelLayers;
import net.capozi.menagerie.common.entity.client.ChainsRenderer;
import net.capozi.menagerie.foundation.ItemInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.text.DecimalFormat;
import java.util.List;

import static net.capozi.menagerie.foundation.BlockInit.*;

public class MenagerieClient implements ClientModInitializer {
    MinecraftClient minecraftClient = MinecraftClient.getInstance();
    public static void registerModelPredicateProviders() {
        ModelPredicateProviderRegistry.register(ItemInit.HEAVYIRON_LONGSPOON, new Identifier("pull"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null || EnchantmentHelper.getLevel(EnchantInit.POGO, itemStack) > 0) {
                return 0.0F;
            }
            return livingEntity.getActiveItem() != itemStack || EnchantmentHelper.getLevel(EnchantInit.POGO, itemStack) != 0 ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
        });
        ModelPredicateProviderRegistry.register(ItemInit.HEAVYIRON_LONGSPOON,  new Identifier("pulling"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null || EnchantmentHelper.getLevel(EnchantInit.POGO, itemStack) > 0) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack && EnchantmentHelper.getLevel(EnchantInit.POGO, itemStack) == 0 ? 1.0F : 0.0F;
        });
    }
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CAPOZI_PLUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(EYA_PLUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(COSMO_PLUSH, RenderLayer.getCutout());
        EntityRendererRegistry.register(EntityInit.BLUE_CHAINS, ChainsRenderer::new);
        EntityRendererRegistry.register(EntityInit.CIRCLE, CircleBeamRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CHAINS, ChainsEntityModel::getTexturedModelData);
        FlashPacket.registerClientReceiver();
        FlashOverlayRenderer.init();
        registerModelPredicateProviders();
        ParticleInit.init();
        ItemTooltipCallback.EVENT.register((ItemStack stack, TooltipContext context, List<Text> lines) -> {
            int level = EnchantmentHelper.getLevel(EnchantInit.ARCANE_DAMAGE, stack);
            if (level > 0) {
                float f = stack.getItem() instanceof SwordItem sword ? (sword.getAttackDamage() / 5) : 0f;
                DecimalFormat df = new DecimalFormat("#.##");
                String totl = " " + df.format(f) + " Magic Damage";
                if (FabricLoader.getInstance().getModContainer("legendarytooltips").isPresent()) {
                    lines.add(6, Text.literal(totl).formatted(Formatting.DARK_GREEN));
                } else {
                    lines.add(5, Text.literal(totl).formatted(Formatting.DARK_GREEN));
                }
            }
        });
        FabricLoader.getInstance().getModContainer(Menagerie.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(Menagerie.MOD_ID, "spoon_accessibility"), modContainer, ResourcePackActivationType.NORMAL);
        });
        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            if (AllVFX.shouldRenderCube) {
                MatrixStack matrices = context.matrixStack();
                VertexConsumerProvider.Immediate vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
                matrices.push();
                AllVFX.renderSolidPurpleCube(matrices, vertexConsumers, AllVFX.cubePosition, 1.0f);
                matrices.pop();
                vertexConsumers.draw();
            }
        });
    }
}
