package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class MobLassosFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientFactories.INSTANCE.clientModConstructor(MobLassos.MOD_ID).accept(new MobLassosClient());
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            MobLassos.LOGGER.info("stack {}, tint {}", stack, tintIndex);
            return 0xFFFFFF;
        }, ModRegistry.GOLDEN_LASSO_ITEM.get());
    }
}
