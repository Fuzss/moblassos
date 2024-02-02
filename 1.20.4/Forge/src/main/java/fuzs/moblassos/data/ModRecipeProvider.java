package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.GOLDEN_LASSO_ITEM.get())
                .define('#', Items.STRING)
                .define('X', Items.GOLD_NUGGET)
                .define('&', Items.ENDER_PEARL)
                .pattern("X#X")
                .pattern("#&#")
                .pattern("X#X")
                .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.AQUA_LASSO_ITEM.get())
                .define('#', Ingredient.of(Items.COD, Items.SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH))
                .define('X', Items.LAPIS_LAZULI)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.get())
                .pattern(" X ")
                .pattern("#&#")
                .pattern(" X ")
                .unlockedBy(getHasName(ModRegistry.GOLDEN_LASSO_ITEM.get()), has(ModRegistry.GOLDEN_LASSO_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.DIAMOND_LASSO_ITEM.get())
                .define('#', Items.DIAMOND)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.get())
                .define('X', ModRegistry.AQUA_LASSO_ITEM.get())
                .pattern(" # ")
                .pattern("X#&")
                .pattern(" # ")
                .unlockedBy(getHasName(ModRegistry.AQUA_LASSO_ITEM.get()), has(ModRegistry.AQUA_LASSO_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.EMERALD_LASSO_ITEM.get())
                .define('#', Items.EMERALD)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.get())
                .pattern(" # ")
                .pattern("#&#")
                .pattern(" # ")
                .unlockedBy(getHasName(ModRegistry.GOLDEN_LASSO_ITEM.get()), has(ModRegistry.GOLDEN_LASSO_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.HOSTILE_LASSO_ITEM.get())
                .define('#', Items.IRON_NUGGET)
                .define('+', Items.BLAZE_ROD)
                .define('X', Ingredient.of(Items.LEATHER, Items.RABBIT_HIDE))
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.get())
                .pattern("X+X")
                .pattern("#&#")
                .pattern("X+X")
                .unlockedBy(getHasName(ModRegistry.GOLDEN_LASSO_ITEM.get()), has(ModRegistry.GOLDEN_LASSO_ITEM.get()))
                .save(recipeConsumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModRegistry.CONTRACT_ITEM.get())
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.FEATHER)
                .requires(Items.INK_SAC)
                .requires(Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(recipeConsumer);
    }
}
