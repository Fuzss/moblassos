package fuzs.moblassos.network;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ClientboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.Level;

public record ClientboundVillagerParticlesMessage(int entityId,
                                                  boolean happyParticles) implements ClientboundPlayMessage {
    public static final StreamCodec<ByteBuf, ClientboundVillagerParticlesMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ClientboundVillagerParticlesMessage::entityId,
            ByteBufCodecs.BOOL,
            ClientboundVillagerParticlesMessage::happyParticles,
            ClientboundVillagerParticlesMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<>() {
            @Override
            public void accept(Context context) {
                Entity entity = context.level().getEntity(ClientboundVillagerParticlesMessage.this.entityId());
                if (entity instanceof AbstractVillager abstractVillager) {
                    addParticlesAroundVillager(context.level(),
                            abstractVillager,
                            ClientboundVillagerParticlesMessage.this.happyParticles() ? ParticleTypes.HAPPY_VILLAGER :
                                    ParticleTypes.ANGRY_VILLAGER);
                }
            }

            private static void addParticlesAroundVillager(Level level, LivingEntity livingEntity, ParticleOptions particleOption) {
                for (int i = 0; i < 5; ++i) {
                    double d0 = livingEntity.getRandom().nextGaussian() * 0.02D;
                    double d1 = livingEntity.getRandom().nextGaussian() * 0.02D;
                    double d2 = livingEntity.getRandom().nextGaussian() * 0.02D;
                    level.addParticle(particleOption,
                            livingEntity.getRandomX(1.0D),
                            livingEntity.getRandomY() + 1.0D,
                            livingEntity.getRandomZ(1.0D),
                            d0,
                            d1,
                            d2);
                }
            }
        };
    }
}
