package fuzs.moblassos.capability;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.config.ServerConfig;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;

public class VillagerContractCapability extends CapabilityComponent<AbstractVillager> {
    public static final String TAG_ACCEPTED_CONTRACT = "AcceptedContract";

    private boolean acceptedContract;

    public void acceptContract() {
        if (!this.acceptedContract) {
            this.acceptedContract = true;
            this.setChanged();
        }
    }

    public boolean hasAcceptedContract() {
        return this.acceptedContract;
    }

    @Override
    public void write(CompoundTag tag) {
        tag.putBoolean(TAG_ACCEPTED_CONTRACT, this.acceptedContract);
    }

    @Override
    public void read(CompoundTag tag) {
        this.acceptedContract = tag.getBoolean(TAG_ACCEPTED_CONTRACT);
    }

    public static boolean canAcceptContract(AbstractVillager abstractVillager) {
        if (MobLassos.CONFIG.get(ServerConfig.class).villagerAcceptsContract) {
            if (abstractVillager instanceof Villager villager) {
                return Math.abs(villager.getUUID().getLeastSignificantBits() % VillagerData.MAX_VILLAGER_LEVEL) <
                        villager.getVillagerData().getLevel();
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
