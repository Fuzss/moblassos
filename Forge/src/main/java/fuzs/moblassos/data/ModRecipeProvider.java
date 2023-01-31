package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator dataGenerator, String modId) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(ModRegistry.GOLDEN_LASSO_ITEM.get())
                .define('#', Items.STRING)
                .define('X', Items.GOLD_NUGGET)
                .define('&', Items.ENDER_PEARL)
                .define('+', Items.LEATHER)
                .pattern("X#X")
                .pattern("#&#")
                .pattern("X+X")
                .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(ModRegistry.AQUA_LASSO_ITEM.get())
                .define('#', Ingredient.of(Items.COD, Items.SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH))
                .define('X', Items.LAPIS_LAZULI)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.get())
                .pattern(" X ")
                .pattern("#&#")
                .pattern(" X ")
                .unlockedBy(getHasName(ModRegistry.GOLDEN_LASSO_ITEM.get()), has(ModRegistry.GOLDEN_LASSO_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(ModRegistry.DIAMOND_LASSO_ITEM.get())
                .define('#', Items.DIAMOND)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.get())
                .define('X', ModRegistry.AQUA_LASSO_ITEM.get())
                .pattern(" # ")
                .pattern("X#&")
                .pattern(" # ")
                .unlockedBy(getHasName(ModRegistry.AQUA_LASSO_ITEM.get()), has(ModRegistry.AQUA_LASSO_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(ModRegistry.EMERALD_LASSO_ITEM.get())
                .define('#', Items.EMERALD)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.get())
                .pattern(" # ")
                .pattern("#&#")
                .pattern(" # ")
                .unlockedBy(getHasName(ModRegistry.GOLDEN_LASSO_ITEM.get()), has(ModRegistry.GOLDEN_LASSO_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(ModRegistry.HOSTILE_LASSO_ITEM.get())
                .define('#', Items.IRON_INGOT)
                .define('+', Items.BLAZE_ROD)
                .define('X', Items.LEATHER)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.get())
                .pattern("X+X")
                .pattern("#&#")
                .pattern("X+X")
                .unlockedBy(getHasName(ModRegistry.GOLDEN_LASSO_ITEM.get()), has(ModRegistry.GOLDEN_LASSO_ITEM.get()))
                .save(recipeConsumer);
        ShapelessRecipeBuilder.shapeless(ModRegistry.CONTRACT_ITEM.get())
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.FEATHER)
                .requires(Items.INK_SAC)
                .requires(Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(recipeConsumer);
    }
}
