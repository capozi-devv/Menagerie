package net.capozi.menagerie.common.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;


public class TrickRoomItem extends Item {
    public TrickRoomItem(Settings settings) {
        super(settings);
    }
    public static class CubeRenderHandler {
        public static boolean renderCube = false;
        public static Box renderedBox;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking() && world.isClient()) {
            MinecraftClient client = MinecraftClient.getInstance();
            CubeRenderHandler.renderCube = true;
            CubeRenderHandler.renderedBox = new Box(user.getX() - 15, user.getY() - 15, user.getZ() - 15, user.getX() + 15, user.getY() + 15, user.getZ() + 15);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
