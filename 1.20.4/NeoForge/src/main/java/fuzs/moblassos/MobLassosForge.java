package fuzs.moblassos;

import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.data.*;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.init.ModRegistryForge;
import fuzs.puzzleslib.api.capability.v2.ForgeCapabilityHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.concurrent.CompletableFuture;

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
        final DataGenerator dataGenerator = evt.getGenerator();
        final PackOutput packOutput = dataGenerator.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = evt.getLookupProvider();
        final ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModEntityTypeTagsProvider(packOutput, lookupProvider, MobLassos.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModItemTagsProvider(packOutput, lookupProvider, MobLassos.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModLanguageProvider(packOutput, MobLassos.MOD_ID));
        dataGenerator.addProvider(true, new ModModelProvider(packOutput, MobLassos.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModRecipeProvider(packOutput));
        dataGenerator.addProvider(true, new ModSoundDefinitionsProvider(packOutput, MobLassos.MOD_ID, fileHelper));
    }
}
