package net.capozi.menagerie.mixin;

import net.capozi.menagerie.Menagerie;
import net.capozi.menagerie.foundation.ItemInit;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    private static final ModelIdentifier CAMERA_ACTIVE;
    private static final ModelIdentifier MARK_ACTIVE;
    private static final ModelIdentifier HEAVYIRON_LONGSPOON_HANDHELD;
    @Shadow @Final private ItemModels models;
    @Shadow public abstract BakedModel getModel(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed);
    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"), argsOnly = true)
    public BakedModel onRenderItem(BakedModel value, ItemStack stack, ModelTransformationMode mode) {
        if(stack.isOf(ItemInit.CAMERA_OF_THE_OTHERSIDE) && !mode.equals(ModelTransformationMode.GUI))
            return models.getModelManager().getModel(CAMERA_ACTIVE);
//        if(stack.isOf(ItemInit.MARK_OF_DISSONANCE) && !mode.equals(ModelTransformationMode.GUI))
//            return models.getModelManager().getModel(MARK_ACTIVE);
        if (stack.isOf(ItemInit.HEAVYIRON_LONGSPOON) && !mode.equals(ModelTransformationMode.GUI))
            return models.getModelManager().getModel(HEAVYIRON_LONGSPOON_HANDHELD);
        return value;
    }
    static {
        CAMERA_ACTIVE = new ModelIdentifier(Menagerie.identifier("camera_active"), "inventory");
        MARK_ACTIVE = new ModelIdentifier(Menagerie.identifier("mark_active"), "inventory");
        HEAVYIRON_LONGSPOON_HANDHELD = new ModelIdentifier(Menagerie.identifier("heavyiron_longspoon_handheld"), "inventory");
    }
}
