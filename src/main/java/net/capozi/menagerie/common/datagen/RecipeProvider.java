package net.capozi.menagerie.common.datagen;

import net.capozi.menagerie.foundation.BlockInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
    private static final List<ItemConvertible> SMELTABLES_AND_BLASTABLES = List.of(Blocks.SOUL_SAND);
    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Blocks.GILDED_BLACKSTONE, 1)
                .pattern(" S ")
                .pattern("SRS")
                .pattern(" S ")
                .input('S', Items.GOLD_NUGGET)
                .input('R', Items.BLACKSTONE)
                .criterion(hasItem(Items.BLACKSTONE), conditionsFromItem(Items.BLACKSTONE))
                .criterion(hasItem(Items.GOLD_NUGGET), conditionsFromItem(Items.GOLD_NUGGET))
                .offerTo(exporter, new Identifier(getRecipeName(Blocks.GILDED_BLACKSTONE)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemInit.MARK_OF_DISSONANCE, 1)
                .pattern("SAS")
                .pattern("ALA")
                .pattern("SNS")
                .input('S', Items.SOUL_SAND)
                .input('A', Items.NETHERITE_SCRAP)
                .input('L', Items.LAPIS_LAZULI)
                .input('N', Items.NETHERITE_INGOT)
                .criterion(hasItem(Items.NETHERITE_SCRAP), conditionsFromItem(Items.NETHERITE_SCRAP))
                .offerTo(exporter, new Identifier(getRecipeName(ItemInit.MARK_OF_DISSONANCE)));
    }
}
