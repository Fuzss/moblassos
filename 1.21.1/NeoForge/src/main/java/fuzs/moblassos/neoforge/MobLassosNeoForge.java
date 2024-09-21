package fuzs.moblassos.neoforge;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.data.ModRecipeProvider;
import fuzs.moblassos.data.client.ModLanguageProvider;
import fuzs.moblassos.data.client.ModModelProvider;
import fuzs.moblassos.data.tags.ModEntityTypeTagProvider;
import fuzs.moblassos.data.tags.ModItemTagProvider;
import fuzs.moblassos.neoforge.data.ModSoundDefinitionsProvider;
import fuzs.moblassos.neoforge.init.ModRegistryNeoForge;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(MobLassos.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobLassosNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModRegistryNeoForge.touch();
        ModConstructor.construct(MobLassos.MOD_ID, MobLassos::new);
        DataProviderHelper.registerDataProviders(MobLassos.MOD_ID,
                ModEntityTypeTagProvider::new,
                ModItemTagProvider::new,
                ModLanguageProvider::new,
                ModModelProvider::new,
                ModRecipeProvider::new,
                ModSoundDefinitionsProvider::new
        );
    }
}
