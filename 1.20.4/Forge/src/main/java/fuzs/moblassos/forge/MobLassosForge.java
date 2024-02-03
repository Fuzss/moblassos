package fuzs.moblassos.forge;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.forge.init.ModRegistryForge;
import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.forge.api.capability.v3.ForgeCapabilityHelper;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(MobLassos.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobLassosForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModRegistryForge.touch();
        ModConstructor.construct(MobLassos.MOD_ID, MobLassos::new);
        registerCapabilities();
    }

    private static void registerCapabilities() {
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.VILLAGER_CONTRACT_CAPABILITY, new CapabilityToken<>() {
            // NO-OP
        });
    }
}
