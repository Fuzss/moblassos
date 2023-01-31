package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class MobLassosFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientFactories.INSTANCE.clientModConstructor(MobLassos.MOD_ID).accept(new MobLassosClient());
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return ((LassoItem) stack.getItem()).getColor(stack, tintIndex);
        }, ModRegistry.GOLDEN_LASSO_ITEM.get(), ModRegistry.AQUA_LASSO_ITEM.get(), ModRegistry.DIAMOND_LASSO_ITEM.get(), ModRegistry.EMERALD_LASSO_ITEM.get(), ModRegistry.HOSTILE_LASSO_ITEM.get(), ModRegistry.CREATIVE_LASSO_ITEM.get());
    }
}
