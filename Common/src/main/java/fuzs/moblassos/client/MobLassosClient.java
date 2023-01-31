package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.client.core.ClientModConstructor;

public class MobLassosClient implements ClientModConstructor {

    @Override
    public void onRegisterItemModelProperties(ItemModelPropertiesContext context) {
        context.registerItem(ModRegistry.GOLDEN_LASSO_ITEM.get(), MobLassos.id("filled"), (itemStack, clientLevel, livingEntity, i) -> {
            return ((LassoItem) itemStack.getItem()).hasStoredEntity(itemStack) ? 1.0F : 0.0F;
        });
    }
}
