package net.capozi.menagerie.common.item;

import net.capozi.menagerie.common.entity.object.TrickRoomEntity;
import net.capozi.menagerie.server.network.packet.clientbound.FlashPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
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
        if (!user.isSneaking()) {
            NbtCompound nbt = stack.getNbt();
            if (!nbt.isEmpty()) {
                UUID roomUuid = nbt.getUuid(ROOM_UUID_KEY);
                if (world instanceof ServerWorld serverWorld) {
                    TrickRoomEntity room = serverWorld.getEntity(roomUuid) instanceof TrickRoomEntity ? (TrickRoomEntity)serverWorld.getEntity(roomUuid) : null;
                    List<Entity> entities = serverWorld.getOtherEntities(user, room.getRoomBounds());
                    for (Entity entity : entities) {
                       if (entity instanceof LivingEntity) {
                           entity.setVelocity(user.getRotationVector().multiply(3));
                       }
                    }
                }
            }
        } else {
            toggleRoom(world, user, stack);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) {
            return ActionResult.PASS;
        }
        if (player.isSneaking()) {
            toggleRoom(context.getWorld(), player, context.getStack());
        }
        return ActionResult.success(context.getWorld().isClient());
    }

    private void toggleRoom(World world, PlayerEntity player, ItemStack stack) {
        if (!world.isClient()) {
            if (discardBoundRoom(world, stack)) {
                //player.getItemCooldownManager().set(this, 6000);
                world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.PLAYERS, 1f, 1f);
                return;
            }

            TrickRoomEntity room = TrickRoomEntity.create(world, player.getBlockPos());
            if (world.spawnEntity(room)) {
                NbtCompound nbt = stack.getOrCreateNbt();
                nbt.putUuid(ROOM_UUID_KEY, room.getUuid());
                nbt.putString(ROOM_DIMENSION_KEY, world.getRegistryKey().getValue().toString());
            }
            FlashPacket.sendToTracking((ServerWorld) player.getWorld(), player);
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
    }
}
