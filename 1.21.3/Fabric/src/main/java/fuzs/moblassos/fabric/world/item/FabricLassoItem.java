package fuzs.moblassos.fabric.world.item;

import fuzs.moblassos.world.item.LassoItem;
import fuzs.moblassos.world.item.LassoType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FabricLassoItem extends LassoItem {

    public FabricLassoItem(Properties properties, LassoType type) {
        super(properties, type);
    }

    @Override
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }
}
