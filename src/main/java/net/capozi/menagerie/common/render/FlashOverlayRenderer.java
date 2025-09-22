package net.capozi.menagerie.common.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;

public class FlashOverlayRenderer {
    private static long flashStartTime = 0;
    private static final long FLASH_DURATION_MS = 5000;
    public static void init() {
        HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
            if (!isFlashing()) return;
            float progress = (System.currentTimeMillis() - flashStartTime) / (float) FLASH_DURATION_MS;
            float alpha = 1.0f - progress;
            if (alpha < 0f) alpha = 0f;
            // White-goldish color overlay (RGBA)
            drawContext.fill(
                    0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight(),
                    ((int)(alpha * 190) << 24) | 0xFFF09A // Gold color with alpha
            );
        });
    }
    public static void triggerFlash() {
        flashStartTime = System.currentTimeMillis();
    }
    private static boolean isFlashing() {
        return System.currentTimeMillis() - flashStartTime < 5000;
    }
}
