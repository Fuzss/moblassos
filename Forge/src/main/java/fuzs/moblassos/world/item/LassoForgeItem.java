package fuzs.moblassos.world.item;

import net.minecraft.world.item.ItemStack;

public class LassoForgeItem extends LassoItem {

    public LassoForgeItem(Properties properties, Type type) {
        super(properties, type);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }
}
