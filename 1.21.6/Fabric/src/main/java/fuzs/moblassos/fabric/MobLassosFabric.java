package fuzs.moblassos.fabric;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.fabric.init.FabricModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class MobLassosFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricModRegistry.bootstrap();
        ModConstructor.construct(MobLassos.MOD_ID, MobLassos::new);
    }
}
