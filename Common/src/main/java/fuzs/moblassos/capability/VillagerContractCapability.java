package fuzs.moblassos.capability;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.config.ServerConfig;
import fuzs.puzzleslib.capability.data.CapabilityComponent;
import net.minecraft.world.entity.Entity;

public interface VillagerContractCapability extends CapabilityComponent {

    void acceptContract();

    boolean hasAcceptedContract();

    static boolean canAcceptContract(Entity entity) {
        double villagerAcceptsContractChance = MobLassos.CONFIG.get(ServerConfig.class).villagerAcceptsContractChance;
        if (villagerAcceptsContractChance == 0.0) return false;
        int acceptsChance = (int) (1.0 / villagerAcceptsContractChance);
        return entity.getUUID().getLeastSignificantBits() % acceptsChance == 0;
    }
}
