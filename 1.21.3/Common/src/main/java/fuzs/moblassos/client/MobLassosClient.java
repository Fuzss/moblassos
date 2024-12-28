package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ColorProvidersContext;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelPropertiesContext;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MobLassosClient implements ClientModConstructor {
    public static final ResourceLocation ITEM_PROPERTY_FILLED = MobLassos.id("filled");

    @Override
    public void onRegisterItemModelProperties(ItemModelPropertiesContext context) {
        context.registerItemProperty(ITEM_PROPERTY_FILLED,
                (ItemStack itemStack, ClientLevel clientLevel, LivingEntity livingEntity, int i) -> {
                    return ((LassoItem) itemStack.getItem()).hasStoredEntity(itemStack) ? 1.0F : 0.0F;
                }, ModRegistry.GOLDEN_LASSO_ITEM.value(), ModRegistry.AQUA_LASSO_ITEM.value(),
                ModRegistry.DIAMOND_LASSO_ITEM.value(), ModRegistry.EMERALD_LASSO_ITEM.value(),
                ModRegistry.HOSTILE_LASSO_ITEM.value(), ModRegistry.CREATIVE_LASSO_ITEM.value()
        );
    }

    @Override
    public void onRegisterItemColorProviders(ColorProvidersContext<Item, ItemColor> context) {
        context.registerColorProvider((ItemStack itemStack, int tintIndex) -> {
                    return FastColor.ARGB32.opaque(((LassoItem) itemStack.getItem()).getColor(itemStack, tintIndex));
                }, ModRegistry.GOLDEN_LASSO_ITEM.value(), ModRegistry.AQUA_LASSO_ITEM.value(),
                ModRegistry.DIAMOND_LASSO_ITEM.value(), ModRegistry.EMERALD_LASSO_ITEM.value(),
                ModRegistry.HOSTILE_LASSO_ITEM.value(), ModRegistry.CREATIVE_LASSO_ITEM.value()
        );
    }
}
