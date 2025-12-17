package fuzs.moblassos.neoforge;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.data.ModRecipeProvider;
import fuzs.moblassos.data.tags.ModEnchantmentTagProvider;
import fuzs.moblassos.data.tags.ModEntityTypeTagProvider;
import fuzs.moblassos.data.tags.ModItemTagProvider;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.neoforge.init.NeoForgeModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(MobLassos.MOD_ID)
public class MobLassosNeoForge {

    public MobLassosNeoForge() {
        NeoForgeModRegistry.bootstrap();
        ModConstructor.construct(MobLassos.MOD_ID, MobLassos::new);
        DataProviderHelper.registerDataProviders(MobLassos.MOD_ID,
                ModRegistry.REGISTRY_SET_BUILDER,
                ModEntityTypeTagProvider::new,
                ModItemTagProvider::new,
                ModEnchantmentTagProvider::new,
                ModRecipeProvider::new);
    }
}
