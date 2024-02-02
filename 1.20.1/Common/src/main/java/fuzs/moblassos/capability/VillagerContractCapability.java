package fuzs.moblassos.capability;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.config.ServerConfig;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;

public interface VillagerContractCapability extends CapabilityComponent {

    void acceptContract();

    boolean hasAcceptedContract();

    static boolean canAcceptContract(AbstractVillager abstractVillager) {
        if (!MobLassos.CONFIG.get(ServerConfig.class).villagerAcceptsContract) return false;
        if (abstractVillager instanceof Villager villager) {
            return Math.abs(villager.getUUID().getLeastSignificantBits() % VillagerData.MAX_VILLAGER_LEVEL) < villager.getVillagerData().getLevel();
        } else {
            return true;
        }
    }
}
