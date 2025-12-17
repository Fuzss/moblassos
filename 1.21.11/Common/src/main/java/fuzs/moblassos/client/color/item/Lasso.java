package fuzs.moblassos.client.color.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.moblassos.world.item.LassoItem;
import net.minecraft.util.Util;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.SplittableRandom;
import java.util.function.Function;

public final class Lasso implements ItemTintSource {
    public static final MapCodec<Lasso> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf(
            "tint_layer").forGetter((Lasso lasso) -> lasso.tintLayer)).apply(instance, Lasso::new));
    static final Function<Long, Float> ENTITY_TYPE_HUES = Util.memoize((Long seed) -> {
        // prevent clustering when providing sequential seeds in a small range
        return new SplittableRandom(seed).nextFloat();
    });

    private final int tintLayer;

    public Lasso(int tintLayer) {
        this.tintLayer = tintLayer;
    }

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof LassoItem item) {
            long seed = BuiltInRegistries.ENTITY_TYPE.getId(item.getStoredEntityType(itemStack));
            float hue = ENTITY_TYPE_HUES.apply(seed);
            if (this.tintLayer == 0) {
                // highlightColor
                return Mth.hsvToArgb(hue, 0.9F, 0.9F, 255);
            } else {
                // backgroundColor
                return Mth.hsvToArgb(1.0F - hue, 0.8F, 0.9F, 255);
            }
        } else {
            return -1;
        }
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
