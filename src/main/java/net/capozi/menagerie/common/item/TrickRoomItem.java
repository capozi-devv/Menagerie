package net.capozi.menagerie.common.item;

import devv.capozi.zip.common.api.util.MathUtils;
import net.capozi.menagerie.common.entity.object.TrickRoomEntity;
import net.capozi.menagerie.foundation.EffectInit;
import net.capozi.menagerie.foundation.SoundInit;
import net.capozi.menagerie.server.network.packet.clientbound.FlashPacket;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
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
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TrickRoomItem extends Item {
    private static String TEMPORARY = "temporary";
    private static final String TRICK_ROOM_ABILITY_MODE = "TrickRoomAbilityMode";
    private static final String ROOM_UUID_KEY = "MenagerieTrickRoomUuid";
    private static final String ROOM_DIMENSION_KEY = "MenagerieTrickRoomDimension";
    public TrickRoomItem(Settings settings) {
        super(settings);
    }
    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return this.getDefaultStack();
    }
    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = new NbtCompound();
        nbt.putInt(TRICK_ROOM_ABILITY_MODE, 0);
        stack.setNbt(nbt);
        return stack;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        stack.getNbt().putBoolean(TEMPORARY, true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Text text = switch(stack.getOrCreateNbt().getInt("TrickRoomAbilityMode")) {
            case 0 -> Text.literal("§6[Swap]");
            case 1 -> Text.literal("§b[Push]");
            case 2 -> Text.literal("§5[Freeze]");
            default -> Text.literal("§6[Swap]");
        };
        tooltip.add(Text.translatable("tooltip.menagerie.trick_room").append(text));
        if (stack.getNbt().getBoolean(TEMPORARY)) {
            tooltip.add(Text.literal("Fragile").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
        }
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!user.isSneaking()) {
            NbtCompound nbt = stack.getNbt();
            if (nbt == null) return TypedActionResult.pass(stack);
            if (!nbt.isEmpty()) {
                if (!nbt.containsUuid(ROOM_UUID_KEY)) return TypedActionResult.pass(stack);
                UUID roomUuid = nbt.getUuid(ROOM_UUID_KEY);
                if (world instanceof ServerWorld serverWorld) {
                    int bl = stack.getNbt().getInt("TrickRoomAbilityMode");
                    TrickRoomEntity room = serverWorld.getEntity(roomUuid) instanceof TrickRoomEntity ? (TrickRoomEntity)serverWorld.getEntity(roomUuid) : null;
                    List<Entity> entities = serverWorld.getOtherEntities(user, room.getRoomBounds());
                    List<LivingEntity> livingEntities = serverWorld.getEntitiesByClass(LivingEntity.class, room.getRoomBounds().contract(1), entity -> entity.isAlive() && entity != user);
                    switch (bl) {
                        case 0 : {
                            BlockPos pos = user.getBlockPos();
                            Random r = new Random();
                            int i = r.nextInt(livingEntities.size());
                            LivingEntity swapped = livingEntities.get(i);
                            BlockPos target = swapped.getBlockPos();
                            user.teleport(target.getX(), target.getY(), target.getZ(), true);
                            swapped.setPos(pos.getX(), pos.getY(), pos.getZ());
                            break;
                        }
                        case 1 : {
                            for (Entity entity : entities) {
                                if (entity instanceof LivingEntity) {
                                    entity.setVelocity(user.getRotationVector().multiply(5));
                                }
                            }
                            break;
                        }
                        case 2 : {
                            for (Entity entity : entities) {
                                if (entity instanceof PlayerEntity livingEntity) {
                                    livingEntity.addStatusEffect(new StatusEffectInstance(EffectInit.CHAINED_EFFECT, 100));
                                    livingEntity.getWorld().playSound(null, livingEntity.getBlockPos(), SoundEvents.ENTITY_PLAYER_HURT_FREEZE, SoundCategory.PLAYERS);
                                }
                            }
                        }
                    }
                    if (!user.getAbilities().creativeMode) {
                        user.getItemCooldownManager().set(this, 300);
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
                    if (stack.getNbt().getBoolean(TEMPORARY)) {
                        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1f, 1f);

                    }
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
        if (!player.getAbilities().creativeMode) player.getItemCooldownManager().set(this, 20);
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
                    if (stack.getNbt().getBoolean(TEMPORARY)) {
                        stack.decrement(1);
                    }
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
