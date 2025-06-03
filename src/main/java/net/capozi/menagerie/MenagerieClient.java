package net.capozi.menagerie;

import net.capozi.menagerie.common.network.FlashPacket;
import net.capozi.menagerie.common.render.FlashOverlayRenderer;
import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.common.entity.client.ChainsEntityModel;
import net.capozi.menagerie.common.render.ModModelLayers;
import net.capozi.menagerie.common.entity.client.ChainsRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import static net.capozi.menagerie.foundation.BlockInit.CAPOZI_PLUSH;

public class MenagerieClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CAPOZI_PLUSH, RenderLayer.getTranslucent());
        EntityRendererRegistry.register(EntityInit.ABYSSAL_CHAINS, ChainsRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CHAINS, ChainsEntityModel::getTexturedModelData);
        FlashPacket.registerClientReceiver();
        FlashOverlayRenderer.init();
    }
}
