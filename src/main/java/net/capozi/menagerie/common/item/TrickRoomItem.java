package net.capozi.menagerie.common.item;

import net.capozi.menagerie.common.entity.object.TrickRoomEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class TrickRoomItem extends Item {
    private static final String ROOM_UUID_KEY = "MenagerieTrickRoomUuid";
    private static final String ROOM_DIMENSION_KEY = "MenagerieTrickRoomDimension";

    public TrickRoomItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        toggleRoom(world, user, stack);
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) {
            return ActionResult.PASS;
        }

        toggleRoom(context.getWorld(), player, context.getStack());
        return ActionResult.success(context.getWorld().isClient());
    }

    private void toggleRoom(World world, PlayerEntity player, ItemStack stack) {
        if (!world.isClient()) {
            if (discardBoundRoom(world, stack)) {
                player.getItemCooldownManager().set(this, 10);
                return;
            }

            TrickRoomEntity room = TrickRoomEntity.create(world, player.getBlockPos());
            if (world.spawnEntity(room)) {
                NbtCompound nbt = stack.getOrCreateNbt();
                nbt.putUuid(ROOM_UUID_KEY, room.getUuid());
                nbt.putString(ROOM_DIMENSION_KEY, world.getRegistryKey().getValue().toString());
            }
        }

        player.getItemCooldownManager().set(this, 20);
    }

    private boolean discardBoundRoom(World world, ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.containsUuid(ROOM_UUID_KEY)) {
            return false;
        }

        UUID roomUuid = nbt.getUuid(ROOM_UUID_KEY);
        if (world instanceof ServerWorld serverWorld) {
            MinecraftServer server = serverWorld.getServer();
            for (ServerWorld candidateWorld : server.getWorlds()) {
                Entity entity = candidateWorld.getEntity(roomUuid);
                if (entity instanceof TrickRoomEntity room) {
                    room.beginFadeOut();
                    clearBoundRoom(stack);
                    return true;
                }
            }
        }

        clearBoundRoom(stack);
        return false;
    }

    private void clearBoundRoom(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) {
            return;
        }

        nbt.remove(ROOM_UUID_KEY);
        nbt.remove(ROOM_DIMENSION_KEY);
        if (nbt.isEmpty()) {
            stack.setNbt(null);
        }
    }
}
