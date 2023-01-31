package fuzs.moblassos;

import fuzs.moblassos.data.ModBlockStateProvider;
import fuzs.moblassos.data.ModItemTagsProvider;
import fuzs.moblassos.data.ModLanguageProvider;
import fuzs.moblassos.data.ModRecipeProvider;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(MobLassos.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobLassosForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CommonFactories.INSTANCE.modConstructor(MobLassos.MOD_ID, ContentRegistrationFlags.BIOMES).accept(new MobLassos());
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final PlayerInteractEvent.EntityInteract evt) -> {
            LassoItem.onEntityInteract(evt.getEntity(), evt.getLevel(), evt.getHand(), evt.getTarget()).ifPresent(result -> {
                evt.setCancellationResult(result);
                evt.setCanceled(true);
            });
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator dataGenerator = evt.getGenerator();
        ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModBlockStateProvider(dataGenerator, MobLassos.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModItemTagsProvider(dataGenerator, MobLassos.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModLanguageProvider(dataGenerator, MobLassos.MOD_ID));
        dataGenerator.addProvider(true, new ModRecipeProvider(dataGenerator, MobLassos.MOD_ID));
    }
}
