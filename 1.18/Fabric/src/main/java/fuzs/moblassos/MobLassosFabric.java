package fuzs.moblassos;

import fuzs.moblassos.init.ModRegistryFabric;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class MobLassosFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModRegistryFabric.touch();
        ModConstructor.construct(MobLassos.MOD_ID, MobLassos::new);
    }
}
