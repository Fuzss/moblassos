package fuzs.moblassos;

import fuzs.moblassos.world.item.ContractItem;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.InteractionResult;

public class MobLassosFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(MobLassos.MOD_ID, ContentRegistrationFlags.BIOMES).accept(new MobLassos());
        registerHandlers();
    }

    private static void registerHandlers() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            return LassoItem.onEntityInteract(player, world, hand, entity).orElse(InteractionResult.PASS);
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            return ContractItem.onEntityInteract(player, world, hand, entity).orElse(InteractionResult.PASS);
        });
        ServerEntityEvents.ENTITY_LOAD.register(ContractItem::onEntityJoinServerLevel);
    }
}
