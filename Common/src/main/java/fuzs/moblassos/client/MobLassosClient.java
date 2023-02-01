package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import net.minecraft.world.item.Item;

public class MobLassosClient implements ClientModConstructor {

    @Override
    public void onRegisterItemModelProperties(ItemModelPropertiesContext context) {
        registerLassoModelProperty(context, ModRegistry.GOLDEN_LASSO_ITEM.get());
        registerLassoModelProperty(context, ModRegistry.AQUA_LASSO_ITEM.get());
        registerLassoModelProperty(context, ModRegistry.DIAMOND_LASSO_ITEM.get());
        registerLassoModelProperty(context, ModRegistry.EMERALD_LASSO_ITEM.get());
        registerLassoModelProperty(context, ModRegistry.HOSTILE_LASSO_ITEM.get());
        registerLassoModelProperty(context, ModRegistry.CREATIVE_LASSO_ITEM.get());
    }

    private static void registerLassoModelProperty(ItemModelPropertiesContext context, Item item) {
        context.registerItem(item, MobLassos.id("filled"), (itemStack, clientLevel, livingEntity, i) -> {
            return ((LassoItem) itemStack.getItem()).hasStoredEntity(itemStack) ? 1.0F : 0.0F;
        });
    }
}
