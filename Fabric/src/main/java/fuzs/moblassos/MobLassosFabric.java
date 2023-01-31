package fuzs.moblassos;

import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.fabricmc.api.ModInitializer;

public class MobLassosFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(MobLassos.MOD_ID, ContentRegistrationFlags.BIOMES).accept(new MobLassos());
    }
}
