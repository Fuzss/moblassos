package fuzs.moblassos.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;
import fuzs.moblassos.world.item.LassoItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record LassoFilled() implements ConditionalItemModelProperty {
    public static final MapCodec<LassoFilled> MAP_CODEC = MapCodec.unit(new LassoFilled());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int seed, ItemDisplayContext itemDisplayContext) {
        return itemStack.getItem() instanceof LassoItem item && item.hasOccupant(itemStack);
    }

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }
}
