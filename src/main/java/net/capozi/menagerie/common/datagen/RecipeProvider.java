package net.capozi.menagerie.common.datagen;

import net.capozi.menagerie.foundation.BlockInit;
import net.capozi.menagerie.foundation.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemInit.MARK_OF_THE_ACCURSED, 1)
                .pattern("BAB")
                .pattern("ALA")
                .pattern("BNB")
                .input('B', Items.BONE_BLOCK)
                .input('A', Items.NETHERITE_SCRAP)
                .input('L', Items.LAPIS_LAZULI)
                .input('N', Items.NETHER_STAR)
                .criterion(hasItem(Items.NETHER_STAR), conditionsFromItem(Items.NETHER_STAR))
                .offerTo(exporter, new Identifier(getRecipeName(ItemInit.MARK_OF_THE_ACCURSED)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, BlockInit.CAPOZI_PLUSH, 1)
                .pattern("RGR")
                .pattern("GFG")
                .pattern("RBR")
                .input('G', Items.GRAY_WOOL)
                .input('R', Items.RED_WOOL)
                .input('F', Items.FEATHER)
                .input('B', Items.BLACK_WOOL)
                .criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
                .offerTo(exporter, new Identifier(getRecipeName(BlockInit.CAPOZI_PLUSH)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, BlockInit.EYA_PLUSH, 1)
                .pattern("RGR")
                .pattern("GFG")
                .pattern("RBR")
                .input('G', Items.BASALT)
                .input('R', Items.MAGMA_BLOCK)
                .input('F', Items.MAGMA_CREAM)
                .input('B', Items.RED_WOOL)
                .criterion(hasItem(Items.MAGMA_CREAM), conditionsFromItem(Items.MAGMA_CREAM))
                .offerTo(exporter, new Identifier(getRecipeName(BlockInit.EYA_PLUSH)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, BlockInit.COSMO_PLUSH, 1)
                .pattern("RGR")
                .pattern("GFG")
                .pattern("RBR")
                .input('G', Items.LIGHT_GRAY_WOOL)
                .input('R', Items.GRAY_WOOL)
                .input('F', Items.FEATHER)
                .input('B', Items.RED_WOOL)
                .criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
                .offerTo(exporter, new Identifier(getRecipeName(BlockInit.COSMO_PLUSH)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemInit.SIMULACRUM_VESSEL, 1)
                .pattern("SNS")
                .pattern("LLL")
                .pattern("SNS")
                .input('S', Items.SLIME_BALL)
                .input('L', Items.LAPIS_LAZULI)
                .input('N', Items.QUARTZ)
                .criterion(hasItem(Items.NETHERITE_SCRAP), conditionsFromItem(Items.NETHERITE_SCRAP))
                .offerTo(exporter, new Identifier(getRecipeName(ItemInit.SIMULACRUM_VESSEL)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemInit.HEAVYIRON_LONGSPOON, 1)
                .pattern(" HB")
                .pattern("NBH")
                .pattern("SN ")
                .input('H', Items.DEEPSLATE)
                .input('B', Items.IRON_BLOCK)
                .input('N', Items.IRON_NUGGET)
                .input('S', Items.NETHERITE_INGOT)
                .criterion(hasItem(ItemInit.HEAVYIRON_INGOT), conditionsFromItem(ItemInit.HEAVYIRON_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ItemInit.HEAVYIRON_LONGSPOON)));
    }
}