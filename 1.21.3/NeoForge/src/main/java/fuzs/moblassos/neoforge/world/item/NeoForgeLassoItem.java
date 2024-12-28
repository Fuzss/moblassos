package fuzs.moblassos.neoforge.world.item;

import fuzs.moblassos.world.item.LassoItem;
import fuzs.moblassos.world.item.LassoType;
import net.minecraft.world.item.ItemStack;

public class NeoForgeLassoItem extends LassoItem {

    public NeoForgeLassoItem(Properties properties, LassoType type) {
        super(properties, type);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }
}
