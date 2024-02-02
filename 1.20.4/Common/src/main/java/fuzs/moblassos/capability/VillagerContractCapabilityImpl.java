package fuzs.moblassos.capability;

import net.minecraft.nbt.CompoundTag;

public class VillagerContractCapabilityImpl implements VillagerContractCapability {
    public static final String TAG_ACCEPTED_CONTRACT = "AcceptedContract";

    private boolean acceptedContract;

    @Override
    public void acceptContract() {
        this.acceptedContract = true;
    }

    @Override
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
}
