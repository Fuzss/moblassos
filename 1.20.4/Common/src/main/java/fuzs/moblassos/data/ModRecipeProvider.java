package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.GOLDEN_LASSO_ITEM.value())
                .define('#', Items.STRING)
                .define('X', Items.GOLD_NUGGET)
                .define('&', Items.ENDER_PEARL)
                .pattern("X#X")
                .pattern("#&#")
                .pattern("X#X")
                .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.AQUA_LASSO_ITEM.value())
                .define('#', Ingredient.of(Items.COD, Items.SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH))
                .define('X', Items.LAPIS_LAZULI)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.value())
                .pattern(" X ")
                .pattern("#&#")
                .pattern(" X ")
                .unlockedBy(getHasName(ModRegistry.GOLDEN_LASSO_ITEM.value()),
                        has(ModRegistry.GOLDEN_LASSO_ITEM.value())
                )
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.DIAMOND_LASSO_ITEM.value())
                .define('#', Items.DIAMOND)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.value())
                .define('X', ModRegistry.AQUA_LASSO_ITEM.value())
                .pattern(" # ")
                .pattern("X#&")
                .pattern(" # ")
                .unlockedBy(getHasName(ModRegistry.AQUA_LASSO_ITEM.value()), has(ModRegistry.AQUA_LASSO_ITEM.value()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.EMERALD_LASSO_ITEM.value())
                .define('#', Items.EMERALD)
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.value())
                .pattern(" # ")
                .pattern("#&#")
                .pattern(" # ")
                .unlockedBy(getHasName(ModRegistry.GOLDEN_LASSO_ITEM.value()),
                        has(ModRegistry.GOLDEN_LASSO_ITEM.value())
                )
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.HOSTILE_LASSO_ITEM.value())
                .define('#', Items.IRON_NUGGET)
                .define('+', Items.BLAZE_ROD)
                .define('X', Ingredient.of(Items.LEATHER, Items.RABBIT_HIDE))
                .define('&', ModRegistry.GOLDEN_LASSO_ITEM.value())
                .pattern("X+X")
                .pattern("#&#")
                .pattern("X+X")
                .unlockedBy(getHasName(ModRegistry.GOLDEN_LASSO_ITEM.value()),
                        has(ModRegistry.GOLDEN_LASSO_ITEM.value())
                )
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModRegistry.CONTRACT_ITEM.value())
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.FEATHER)
                .requires(Items.INK_SAC)
                .requires(Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(recipeOutput);
    }
}
