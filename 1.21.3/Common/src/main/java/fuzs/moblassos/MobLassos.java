package fuzs.moblassos;

import fuzs.moblassos.config.ServerConfig;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.network.ClientboundVillagerParticlesMessage;
import fuzs.moblassos.world.item.ContractItem;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.CreativeModeTabContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.puzzleslib.api.item.v2.CreativeModeTabConfigurator;
import fuzs.puzzleslib.api.network.v3.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobLassos implements ModConstructor {
    public static final String MOD_ID = "moblassos";
    public static final String MOD_NAME = "Mob Lassos";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = NetworkHandler.builder(MOD_ID).registerClientbound(
            ClientboundVillagerParticlesMessage.class);
    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        PlayerInteractEvents.USE_ENTITY.register(LassoItem::onEntityInteract);
        PlayerInteractEvents.USE_ENTITY.register(ContractItem::onEntityInteract);
    }

    @Override
    public void onRegisterCreativeModeTabs(CreativeModeTabContext context) {
        context.registerCreativeModeTab(CreativeModeTabConfigurator.from(MOD_ID)
                .icon(() -> new ItemStack(ModRegistry.GOLDEN_LASSO_ITEM.value()))
                .displayItems((itemDisplayParameters, output) -> {
                    output.accept(ModRegistry.GOLDEN_LASSO_ITEM.value());
                    output.accept(ModRegistry.AQUA_LASSO_ITEM.value());
                    output.accept(ModRegistry.DIAMOND_LASSO_ITEM.value());
                    output.accept(ModRegistry.EMERALD_LASSO_ITEM.value());
                    output.accept(ModRegistry.HOSTILE_LASSO_ITEM.value());
                    output.accept(ModRegistry.CREATIVE_LASSO_ITEM.value());
                    output.accept(ModRegistry.CONTRACT_ITEM.value());
                }));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
