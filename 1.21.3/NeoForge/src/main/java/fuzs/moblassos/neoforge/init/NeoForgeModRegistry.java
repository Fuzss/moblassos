package fuzs.moblassos.neoforge.init;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.neoforge.world.item.NeoForgeLassoItem;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.moblassos.world.item.LassoType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class NeoForgeModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(MobLassos.MOD_ID);
    public static final Holder.Reference<Item> GOLDEN_LASSO_ITEM = registerLassoItem("golden_lasso", LassoType.GOLDEN);
    public static final Holder.Reference<Item> AQUA_LASSO_ITEM = registerLassoItem("aqua_lasso", LassoType.AQUA);
    public static final Holder.Reference<Item> DIAMOND_LASSO_ITEM = registerLassoItem("diamond_lasso",
            LassoType.DIAMOND);
    public static final Holder.Reference<Item> EMERALD_LASSO_ITEM = registerLassoItem("emerald_lasso",
            LassoType.EMERALD);
    public static final Holder.Reference<Item> HOSTILE_LASSO_ITEM = registerLassoItem("hostile_lasso",
            LassoType.HOSTILE);
    public static final Holder.Reference<Item> CREATIVE_LASSO_ITEM = registerLassoItem("creative_lasso",
            () -> new Item.Properties().rarity(Rarity.EPIC),
            LassoType.CREATIVE);

    public static void bootstrap() {
        // NO-OP
    }

    private static Holder.Reference<Item> registerLassoItem(String path, LassoType lassoType) {
        return LassoItem.registerLassoItem(REGISTRIES, path, NeoForgeLassoItem::new, lassoType);
    }

    private static Holder.Reference<Item> registerLassoItem(String path, Supplier<Item.Properties> itemPropertiesSupplier, LassoType lassoType) {
        return LassoItem.registerLassoItem(REGISTRIES, path, NeoForgeLassoItem::new, itemPropertiesSupplier, lassoType);
    }
}
