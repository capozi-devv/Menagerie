package net.capozi.menagerie;

import net.capozi.menagerie.common.network.FlashPacket;
import net.capozi.menagerie.common.render.FlashOverlayRenderer;
import net.capozi.menagerie.foundation.EntityInit;
import net.capozi.menagerie.common.entity.client.ChainsEntityModel;
import net.capozi.menagerie.common.render.ModModelLayers;
import net.capozi.menagerie.common.entity.client.ChainsRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MenagerieClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityInit.ABYSSAL_CHAINS, ChainsRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CHAINS, ChainsEntityModel::getTexturedModelData);
        FlashPacket.registerClientReceiver();
        FlashOverlayRenderer.init();
    }
}
