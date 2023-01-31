package fuzs.moblassos.capability;

import fuzs.puzzleslib.capability.data.CapabilityComponent;
import net.minecraft.world.entity.Entity;

public interface VillagerContractCapability extends CapabilityComponent {

    void acceptContract();

    boolean hasAcceptedContract();

    static boolean canAcceptContract(Entity entity) {
        return entity.getUUID().getLeastSignificantBits() % 5 == 0;
    }
}
