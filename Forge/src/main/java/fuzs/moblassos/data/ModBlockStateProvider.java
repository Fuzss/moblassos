package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator dataGenerator, String modId, ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.itemModels().basicItem(ModRegistry.CONTRACT_ITEM.get());
        this.filledLassoItem(ModRegistry.GOLDEN_LASSO_ITEM.get());
        this.filledLassoItem(ModRegistry.AQUA_LASSO_ITEM.get());
        this.filledLassoItem(ModRegistry.DIAMOND_LASSO_ITEM.get());
        this.filledLassoItem(ModRegistry.EMERALD_LASSO_ITEM.get());
        this.filledLassoItem(ModRegistry.HOSTILE_LASSO_ITEM.get());
        this.filledLassoItem(ModRegistry.CREATIVE_LASSO_ITEM.get());
        this.lassoItem(ModRegistry.GOLDEN_LASSO_ITEM.get());
        this.lassoItem(ModRegistry.AQUA_LASSO_ITEM.get());
        this.lassoItem(ModRegistry.DIAMOND_LASSO_ITEM.get());
        this.lassoItem(ModRegistry.EMERALD_LASSO_ITEM.get());
        this.lassoItem(ModRegistry.HOSTILE_LASSO_ITEM.get());
        this.lassoItem(ModRegistry.CREATIVE_LASSO_ITEM.get());
    }

    private void lassoItem(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        this.itemModels().basicItem(key)
                .override().predicate(this.modLoc("filled"), 1.0F).model(new ModelFile.ExistingModelFile(this.extendWithFolder(this.prefix(key, "filled_")), this.itemModels().existingFileHelper)).end();
    }

    private ResourceLocation extendWithFolder(ResourceLocation resourceLocation) {
        if (resourceLocation.getPath().contains("/")) {
            return resourceLocation;
        }
        return new ResourceLocation(resourceLocation.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + resourceLocation.getPath());
    }

    public ItemModelBuilder filledLassoItem(Item item) {
        return this.filledLassoItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    private ResourceLocation prefix(ResourceLocation resourceLocation, String extension) {
        return new ResourceLocation(resourceLocation.getNamespace(), extension + resourceLocation.getPath());
    }

    public ItemModelBuilder filledLassoItem(ResourceLocation item) {
        return this.itemModels().getBuilder(this.prefix(item, "filled_").toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", this.extendWithFolder(item))
                .texture("layer1", this.extendWithFolder(this.modLoc("lasso_overlay_light")))
                .texture("layer2", this.extendWithFolder(this.modLoc("lasso_overlay_dark")));
    }
}
