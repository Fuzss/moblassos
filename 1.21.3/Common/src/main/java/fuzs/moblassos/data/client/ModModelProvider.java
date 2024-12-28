package fuzs.moblassos.data.client;

import com.google.gson.JsonElement;
import fuzs.moblassos.MobLassos;
import fuzs.moblassos.client.MobLassosClient;
import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.client.data.v2.ItemModelProperties;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addItemModels(ItemModelGenerators builder) {
        builder.generateFlatItem(ModRegistry.CONTRACT_ITEM.value(), ModelTemplates.FLAT_ITEM);
        createLassoItem(ModRegistry.GOLDEN_LASSO_ITEM.value(), builder.output);
        createLassoItem(ModRegistry.AQUA_LASSO_ITEM.value(), builder.output);
        createLassoItem(ModRegistry.DIAMOND_LASSO_ITEM.value(), builder.output);
        createLassoItem(ModRegistry.EMERALD_LASSO_ITEM.value(), builder.output);
        createLassoItem(ModRegistry.HOSTILE_LASSO_ITEM.value(), builder.output);
        createLassoItem(ModRegistry.CREATIVE_LASSO_ITEM.value(), builder.output);
        createFilledLassoItem(ModRegistry.GOLDEN_LASSO_ITEM.value(), builder.output);
        createFilledLassoItem(ModRegistry.AQUA_LASSO_ITEM.value(), builder.output);
        createFilledLassoItem(ModRegistry.DIAMOND_LASSO_ITEM.value(), builder.output);
        createFilledLassoItem(ModRegistry.EMERALD_LASSO_ITEM.value(), builder.output);
        createFilledLassoItem(ModRegistry.HOSTILE_LASSO_ITEM.value(), builder.output);
        createFilledLassoItem(ModRegistry.CREATIVE_LASSO_ITEM.value(), builder.output);
    }

    public static ResourceLocation createLassoItem(Item item, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
        return generateFlatItem(item, ModelTemplates.FLAT_ITEM, modelOutput,
                ItemModelProperties.overridesFactory(ModelTemplates.FLAT_ITEM,
                        ItemModelProperties.singleOverride(ModelLocationUtils.getModelLocation(item, "_filled"),
                                MobLassosClient.ITEM_PROPERTY_FILLED, 1.0F
                        )
                )
        );
    }

    public static ResourceLocation createFilledLassoItem(Item item, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
        return ModelTemplates.THREE_LAYERED_ITEM.create(ModelLocationUtils.getModelLocation(item, "_filled"),
                TextureMapping.layered(ModelLocationUtils.getModelLocation(item),
                        decorateItemModelLocation(MobLassos.id("lasso_overlay_light")),
                        decorateItemModelLocation(MobLassos.id("lasso_overlay_dark"))
                ), modelOutput
        );
    }
}
