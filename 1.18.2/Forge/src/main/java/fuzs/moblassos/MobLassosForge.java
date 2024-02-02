package fuzs.moblassos;

import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.data.*;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.init.ModRegistryForge;
import fuzs.puzzleslib.api.capability.v2.ForgeCapabilityHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(MobLassos.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobLassosForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(MobLassos.MOD_ID, MobLassos::new);
        ModRegistryForge.touch();
        registerCapabilities();
    }

    private static void registerCapabilities() {
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.VILLAGER_CONTRACT_CAPABILITY, new CapabilityToken<VillagerContractCapability>() {});
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        evt.getGenerator().addProvider(new ModEntityTypeTagsProvider(evt, MobLassos.MOD_ID));
        evt.getGenerator().addProvider(new ModItemTagsProvider(evt, MobLassos.MOD_ID));
        evt.getGenerator().addProvider(new ModLanguageProvider(evt, MobLassos.MOD_ID));
        evt.getGenerator().addProvider(new ModModelProvider(evt, MobLassos.MOD_ID));
        evt.getGenerator().addProvider(new ModRecipeProvider(evt, MobLassos.MOD_ID));
        evt.getGenerator().addProvider(new ModSoundDefinitionsProvider(evt, MobLassos.MOD_ID));
    }
}
