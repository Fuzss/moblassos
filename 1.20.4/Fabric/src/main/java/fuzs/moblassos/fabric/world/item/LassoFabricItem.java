package fuzs.moblassos.fabric.world.item;

import fuzs.moblassos.world.item.LassoItem;
import fuzs.moblassos.world.item.LassoType;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LassoFabricItem extends LassoItem implements FabricItem {

    public LassoFabricItem(Properties properties, LassoType type) {
        super(properties, type);
    }

    @Override
    public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }
}
