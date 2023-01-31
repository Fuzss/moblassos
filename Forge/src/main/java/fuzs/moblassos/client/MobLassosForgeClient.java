package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = MobLassos.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MobLassosForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientFactories.INSTANCE.clientModConstructor(MobLassos.MOD_ID).accept(new MobLassosClient());
    }

    @SubscribeEvent
    public static void onRegisterColorHandlers$Item(final RegisterColorHandlersEvent.Item evt) {
        evt.register((stack, tintIndex) -> {
            return ((LassoItem) stack.getItem()).getColor(stack, tintIndex);
        }, ModRegistry.GOLDEN_LASSO_ITEM.get(), ModRegistry.AQUA_LASSO_ITEM.get(), ModRegistry.DIAMOND_LASSO_ITEM.get(), ModRegistry.EMERALD_LASSO_ITEM.get(), ModRegistry.HOSTILE_LASSO_ITEM.get(), ModRegistry.CREATIVE_LASSO_ITEM.get());
    }
}
