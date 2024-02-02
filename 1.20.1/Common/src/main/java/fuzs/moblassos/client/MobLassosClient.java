package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ColorProvidersContext;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelPropertiesContext;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MobLassosClient implements ClientModConstructor {

    @Override
    public void onRegisterItemModelProperties(ItemModelPropertiesContext context) {
        context.registerItemProperty(MobLassos.id("filled"), (itemStack, clientLevel, livingEntity, i) -> {
            return ((LassoItem) itemStack.getItem()).hasStoredEntity(itemStack) ? 1.0F : 0.0F;
        }, ModRegistry.GOLDEN_LASSO_ITEM.get(), ModRegistry.AQUA_LASSO_ITEM.get(), ModRegistry.DIAMOND_LASSO_ITEM.get(), ModRegistry.EMERALD_LASSO_ITEM.get(), ModRegistry.HOSTILE_LASSO_ITEM.get(), ModRegistry.CREATIVE_LASSO_ITEM.get());
    }

    @Override
    public void onRegisterItemColorProviders(ColorProvidersContext<Item, ItemColor> context) {
        context.registerColorProvider((ItemStack stack, int tintIndex) -> {
            return ((LassoItem) stack.getItem()).getColor(stack, tintIndex);
        }, ModRegistry.GOLDEN_LASSO_ITEM.get(), ModRegistry.AQUA_LASSO_ITEM.get(), ModRegistry.DIAMOND_LASSO_ITEM.get(), ModRegistry.EMERALD_LASSO_ITEM.get(), ModRegistry.HOSTILE_LASSO_ITEM.get(), ModRegistry.CREATIVE_LASSO_ITEM.get());
    }
}
