package fuzs.moblassos.network;

import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.network.v3.ClientMessageListener;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;

public record ClientboundVillagerContractMessage(int entityId) implements ClientboundMessage<ClientboundVillagerContractMessage> {

    @Override
    public ClientMessageListener<ClientboundVillagerContractMessage> getHandler() {
        return new ClientMessageListener<>() {

            @Override
            public void handle(ClientboundVillagerContractMessage message, Minecraft client, ClientPacketListener handler, LocalPlayer player, ClientLevel level) {
                ModRegistry.VILLAGER_CONTRACT_CAPABILITY.maybeGet(level.getEntity(message.entityId())).ifPresent(VillagerContractCapability::acceptContract);
            }
        };
    }
}
