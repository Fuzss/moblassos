package fuzs.moblassos.capability;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.config.ServerConfig;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface VillagerContractCapability extends CapabilityComponent {

    void acceptContract();

    boolean hasAcceptedContract();

    static boolean canAcceptContract(Entity entity) {
        double villagerAcceptsContractChance = MobLassos.CONFIG.get(ServerConfig.class).villagerAcceptsContractChance;
        if (villagerAcceptsContractChance == 0.0) return false;
        if (entity instanceof LivingEntity livingEntity && livingEntity.isBaby()) {
            villagerAcceptsContractChance = Math.min(1.0, villagerAcceptsContractChance * 2.0);
        }
        int acceptsChance = (int) (1.0 / villagerAcceptsContractChance);
        return entity.getUUID().getLeastSignificantBits() % acceptsChance == 0;
    }
}
