package net.capozi.menagerie;

import net.capozi.menagerie.common.network.FlashPacket;
import net.capozi.menagerie.common.render.FlashOverlayRenderer;
import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.common.entity.client.ChainsEntityModel;
import net.capozi.menagerie.common.render.ModModelLayers;
import net.capozi.menagerie.common.entity.client.ChainsRenderer;
import net.capozi.menagerie.foundation.ItemInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import static net.capozi.menagerie.foundation.BlockInit.*;

public class MenagerieClient implements ClientModInitializer {
    public static void registerModelPredicateProviders() {
        ModelPredicateProviderRegistry.register(ItemInit.HEAVYIRON_LONGSPOON, new Identifier("pull"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
        });
        ModelPredicateProviderRegistry.register(ItemInit.HEAVYIRON_LONGSPOON,  new Identifier("pulling"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
        });
    }
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CAPOZI_PLUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(EYA_PLUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(COSMO_PLUSH, RenderLayer.getCutout());
        EntityRendererRegistry.register(EntityInit.ABYSSAL_CHAINS, ChainsRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CHAINS, ChainsEntityModel::getTexturedModelData);
        FlashPacket.registerClientReceiver();
        FlashOverlayRenderer.init();
        registerModelPredicateProviders();
    }
}
