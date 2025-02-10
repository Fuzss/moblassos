package fuzs.moblassos.client.color.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.moblassos.world.item.LassoItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;

public final class Lasso implements ItemTintSource {
    public static final MapCodec<Lasso> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf(
            "tint_layer").forGetter((Lasso lasso) -> lasso.tintLayer)).apply(instance, Lasso::new));

    private final ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
    private final int tintLayer;

    public Lasso(int tintLayer) {
        this.tintLayer = tintLayer;
    }

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof LassoItem item) {
            EntityType<?> entityType = item.getStoredEntityType(itemStack);
            return this.getSpawnEggTintLayer(entityType, clientLevel, livingEntity);
        } else {
            return -1;
        }
    }

    private int getSpawnEggTintLayer(@Nullable EntityType<?> entityType, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        Item item = SpawnEggItem.byId(entityType);
        ItemStack itemStack = item != null ? new ItemStack(item) : ItemStack.EMPTY;
        int[] tintLayers = this.getItemTintLayers(itemStack, clientLevel, livingEntity);
        return this.tintLayer >= 0 && this.tintLayer < tintLayers.length ? tintLayers[this.tintLayer] : -1;
    }

    private int[] getItemTintLayers(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        Minecraft.getInstance()
                .getItemModelResolver()
                .updateForTopItem(this.itemStackRenderState,
                        itemStack,
                        ItemDisplayContext.NONE,
                        false,
                        clientLevel,
                        livingEntity,
                        0);
        return this.itemStackRenderState.firstLayer().tintLayers;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
