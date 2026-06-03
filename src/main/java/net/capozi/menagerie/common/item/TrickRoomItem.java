package net.capozi.menagerie.common.item;

import net.capozi.menagerie.common.entity.object.TrickRoomEntity;
import net.capozi.menagerie.foundation.SoundInit;
import net.capozi.menagerie.server.network.packet.clientbound.FlashPacket;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TrickRoomItem extends Item {
    private static final boolean TRICK_ROOM_ABILITY_MODE = false;
    private static final String ROOM_UUID_KEY = "MenagerieTrickRoomUuid";
    private static final String ROOM_DIMENSION_KEY = "MenagerieTrickRoomDimension";

    public TrickRoomItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean("TrickRoomAbilityMode", TRICK_ROOM_ABILITY_MODE);
        stack.setNbt(nbt);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Text text = stack.getOrCreateNbt().getBoolean("TrickRoomAbilityMode") ? Text.literal("§6[Swap]") : Text.literal("§b[Push]");
        tooltip.add(Text.translatable("tooltip.menagerie.trick_room").append(text));
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!user.isSneaking()) {
            NbtCompound nbt = stack.getNbt();
            if (!nbt.isEmpty()) {
                UUID roomUuid = nbt.getUuid(ROOM_UUID_KEY);
                if (world instanceof ServerWorld serverWorld) {
                    boolean bl = stack.getNbt().getBoolean("TrickRoomAbilityMode");
                    TrickRoomEntity room = serverWorld.getEntity(roomUuid) instanceof TrickRoomEntity ? (TrickRoomEntity)serverWorld.getEntity(roomUuid) : null;
                    List<Entity> entities = serverWorld.getOtherEntities(user, room.getRoomBounds());
                    List<LivingEntity> livingEntities = serverWorld.getEntitiesByClass(LivingEntity.class, room.getRoomBounds().contract(1), entity -> entity.isAlive() && entity != user);
                    if (bl) {
                        BlockPos pos = user.getBlockPos();
                        Random r = new Random();
                        LivingEntity swapped = livingEntities.get(r.nextInt(livingEntities.size()));
                        BlockPos target = swapped.getBlockPos();
                        user.teleport(target.getX(), target.getY(), target.getZ(), true);
                        swapped.teleport(pos.getX(), pos.getY(), pos.getZ(), true);
                        return TypedActionResult.success(stack);
                    }
                    for (Entity entity : entities) {
                       if (entity instanceof LivingEntity) {
                           entity.setVelocity(user.getRotationVector().multiply(5));
                           if (!user.getAbilities().creativeMode) {
                               user.getItemCooldownManager().set(this, 300);
                           }
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
                if (!player.getAbilities().creativeMode) {
                    player.getItemCooldownManager().set(this, 6000);
                }
                return;
            }

            TrickRoomEntity room = TrickRoomEntity.create(world, player.getBlockPos());
            if (world.spawnEntity(room)) {
                NbtCompound nbt = stack.getOrCreateNbt();
                nbt.putUuid(ROOM_UUID_KEY, room.getUuid());
                nbt.putString(ROOM_DIMENSION_KEY, world.getRegistryKey().getValue().toString());
            }
            world.playSound(null, room.getBlockPos(), SoundInit.TRICK_ROOM_SPAWN, SoundCategory.PLAYERS, 7f, 1f);
            FlashPacket.sendToTracking((ServerWorld) player.getWorld(), player);
        }
        player.getItemCooldownManager().set(this, 20);
    }

    public static boolean discardBoundRoom(World world, ItemStack stack) {
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
                    world.playSound(null, room.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.PLAYERS, 7f, 1f);
                    clearBoundRoom(stack);
                    return true;
                }
            }
        }

        clearBoundRoom(stack);
        return false;
    }

    private static void clearBoundRoom(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) {
            return;
        }

        nbt.remove(ROOM_UUID_KEY);
        nbt.remove(ROOM_DIMENSION_KEY);
    }
}
