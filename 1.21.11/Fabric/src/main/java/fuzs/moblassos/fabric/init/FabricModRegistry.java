package fuzs.moblassos.fabric.init;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.fabric.world.item.FabricLassoItem;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.world.item.LassoType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;

public class FabricModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(MobLassos.MOD_ID);
    public static final Holder.Reference<Item> GOLDEN_LASSO_ITEM = REGISTRIES.registerItem("golden_lasso",
            (Item.Properties properties) -> new FabricLassoItem(properties, LassoType.GOLDEN),
            ModRegistry::lassoProperties);
    public static final Holder.Reference<Item> AQUA_LASSO_ITEM = REGISTRIES.registerItem("aqua_lasso",
            (Item.Properties properties) -> new FabricLassoItem(properties, LassoType.AQUA),
            ModRegistry::lassoProperties);
    public static final Holder.Reference<Item> DIAMOND_LASSO_ITEM = REGISTRIES.registerItem("diamond_lasso",
            (Item.Properties properties) -> new FabricLassoItem(properties, LassoType.DIAMOND),
            ModRegistry::lassoProperties);
    public static final Holder.Reference<Item> EMERALD_LASSO_ITEM = REGISTRIES.registerItem("emerald_lasso",
            (Item.Properties properties) -> new FabricLassoItem(properties, LassoType.EMERALD),
            ModRegistry::lassoProperties);
    public static final Holder.Reference<Item> HOSTILE_LASSO_ITEM = REGISTRIES.registerItem("hostile_lasso",
            (Item.Properties properties) -> new FabricLassoItem(properties, LassoType.HOSTILE),
            ModRegistry::lassoProperties);
    public static final Holder.Reference<Item> CREATIVE_LASSO_ITEM = REGISTRIES.registerItem("creative_lasso",
            (Item.Properties properties) -> new FabricLassoItem(properties, LassoType.CREATIVE),
            ModRegistry::creativeLassoProperties);

    public static void bootstrap() {
        // NO-OP
    }
}
