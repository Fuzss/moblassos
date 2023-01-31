package fuzs.moblassos;

import fuzs.moblassos.config.ServerConfig;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.networking.ClientboundVillagerContractMessage;
import fuzs.moblassos.networking.ClientboundVillagerParticlesMessage;
import fuzs.puzzleslib.api.networking.v3.NetworkHandlerV3;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ModConstructor;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobLassos implements ModConstructor {
    public static final String MOD_ID = "moblassos";
    public static final String MOD_NAME = "Mob Lassos";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder CONFIG = CommonFactories.INSTANCE.serverConfig(ServerConfig.class, () -> new ServerConfig());
    public static final NetworkHandlerV3 NETWORKING = NetworkHandlerV3.builder(MOD_ID).registerClientbound(ClientboundVillagerContractMessage.class).registerClientbound(ClientboundVillagerParticlesMessage.class).build();

    @Override
    public void onConstructMod() {
        CONFIG.bakeConfigs(MOD_ID);
        ModRegistry.touch();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
