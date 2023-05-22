package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class MobLassosFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(MobLassos.MOD_ID, MobLassosClient::new);
    }
}
