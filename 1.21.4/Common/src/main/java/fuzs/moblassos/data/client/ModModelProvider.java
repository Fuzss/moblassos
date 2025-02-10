package fuzs.moblassos.data.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.client.color.item.Lasso;
import fuzs.moblassos.client.renderer.item.properties.conditional.LassoFilled;
import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModRegistry.CONTRACT_ITEM.value(), ModelTemplates.FLAT_ITEM);
        this.generateLasso(ModRegistry.GOLDEN_LASSO_ITEM.value(), itemModelGenerators);
        this.generateLasso(ModRegistry.AQUA_LASSO_ITEM.value(), itemModelGenerators);
        this.generateLasso(ModRegistry.DIAMOND_LASSO_ITEM.value(), itemModelGenerators);
        this.generateLasso(ModRegistry.EMERALD_LASSO_ITEM.value(), itemModelGenerators);
        this.generateLasso(ModRegistry.HOSTILE_LASSO_ITEM.value(), itemModelGenerators);
        this.generateLasso(ModRegistry.CREATIVE_LASSO_ITEM.value(), itemModelGenerators);
    }

    public final void generateLasso(Item item, ItemModelGenerators itemModelGenerators) {
        ItemModel.Unbaked falseModel = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(item,
                ModelTemplates.FLAT_ITEM));
        ResourceLocation resourceLocation = ModelLocationHelper.getItemModel(item, "_filled");
        itemModelGenerators.generateLayeredItem(resourceLocation,
                ModelLocationHelper.getItemTexture(MobLassos.id("lasso_overlay_light")),
                ModelLocationHelper.getItemTexture(MobLassos.id("lasso_overlay_dark")),
                ModelLocationHelper.getItemTexture(item));
        ItemModel.Unbaked trueModel = ItemModelUtils.tintedModel(resourceLocation, new Lasso(0), new Lasso(1));
        itemModelGenerators.generateBooleanDispatch(item, new LassoFilled(), trueModel, falseModel);
    }
}
