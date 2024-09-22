package fuzs.moblassos.network;

import fuzs.puzzleslib.api.network.v3.ClientMessageListener;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;

public record ClientboundVillagerParticlesMessage(int entityId,
                                                  boolean happyParticles) implements ClientboundMessage<ClientboundVillagerParticlesMessage> {

    @Override
    public ClientMessageListener<ClientboundVillagerParticlesMessage> getHandler() {
        return new ClientMessageListener<>() {

            @Override
            public void handle(ClientboundVillagerParticlesMessage message, Minecraft client, ClientPacketListener handler, LocalPlayer player, ClientLevel level) {
                Entity entity = level.getEntity(message.entityId());
                if (entity instanceof AbstractVillager abstractVillager) {
                    addParticlesAroundVillager(abstractVillager,
                            message.happyParticles() ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.ANGRY_VILLAGER
                    );
                }
            }

            private static void addParticlesAroundVillager(LivingEntity livingEntity, ParticleOptions particleOption) {
                for (int i = 0; i < 5; ++i) {
                    double d0 = livingEntity.getRandom().nextGaussian() * 0.02D;
                    double d1 = livingEntity.getRandom().nextGaussian() * 0.02D;
                    double d2 = livingEntity.getRandom().nextGaussian() * 0.02D;
                    livingEntity.level().addParticle(particleOption, livingEntity.getRandomX(1.0D),
                            livingEntity.getRandomY() + 1.0D, livingEntity.getRandomZ(1.0D), d0, d1, d2
                    );
                }
            }
        };
    }
}
