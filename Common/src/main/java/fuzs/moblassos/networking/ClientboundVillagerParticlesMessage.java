package fuzs.moblassos.networking;

import fuzs.moblassos.world.item.ContractItem;
import fuzs.puzzleslib.api.networking.v3.ClientMessageListener;
import fuzs.puzzleslib.api.networking.v3.ClientboundMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.npc.AbstractVillager;

public record ClientboundVillagerParticlesMessage(int entityId, boolean happyParticles) implements ClientboundMessage<ClientboundVillagerParticlesMessage> {

    @Override
    public ClientMessageListener<ClientboundVillagerParticlesMessage> getHandler() {
        return new ClientMessageListener<>() {

            @Override
            public void handle(ClientboundVillagerParticlesMessage message, Minecraft client, ClientPacketListener handler, LocalPlayer player, ClientLevel level) {
                ContractItem.addParticlesAroundVillager((AbstractVillager) level.getEntity(message.entityId()), message.happyParticles() ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.ANGRY_VILLAGER);
            }
        };
    }
}
