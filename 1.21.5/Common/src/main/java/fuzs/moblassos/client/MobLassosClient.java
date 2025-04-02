package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.client.color.item.Lasso;
import fuzs.moblassos.client.renderer.item.properties.conditional.LassoFilled;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelsContext;

public class MobLassosClient implements ClientModConstructor {

    @Override
    public void onRegisterItemModels(ItemModelsContext context) {
        context.registerConditionalItemModelProperty(MobLassos.id("lasso/filled"), LassoFilled.MAP_CODEC);
        context.registerItemTintSource(MobLassos.id("lasso"), Lasso.MAP_CODEC);
    }
}
