package fuzs.moblassos.fabric.init;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.fabric.world.item.LassoFabricItem;
import fuzs.moblassos.world.item.LassoType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModRegistryFabric {
    static final RegistryManager REGISTRY = RegistryManager.from(MobLassos.MOD_ID);
    public static final Holder.Reference<Item> GOLDEN_LASSO_ITEM = REGISTRY.registerItem("golden_lasso",
            () -> new LassoFabricItem(new Item.Properties().stacksTo(1), LassoType.GOLDEN)
    );
    public static final Holder.Reference<Item> AQUA_LASSO_ITEM = REGISTRY.registerItem("aqua_lasso",
            () -> new LassoFabricItem(new Item.Properties().stacksTo(1), LassoType.AQUA)
    );
    public static final Holder.Reference<Item> DIAMOND_LASSO_ITEM = REGISTRY.registerItem("diamond_lasso",
            () -> new LassoFabricItem(new Item.Properties().stacksTo(1), LassoType.DIAMOND)
    );
    public static final Holder.Reference<Item> EMERALD_LASSO_ITEM = REGISTRY.registerItem("emerald_lasso",
            () -> new LassoFabricItem(new Item.Properties().stacksTo(1), LassoType.EMERALD)
    );
    public static final Holder.Reference<Item> HOSTILE_LASSO_ITEM = REGISTRY.registerItem("hostile_lasso",
            () -> new LassoFabricItem(new Item.Properties().stacksTo(1), LassoType.HOSTILE)
    );
    public static final Holder.Reference<Item> CREATIVE_LASSO_ITEM = REGISTRY.registerItem("creative_lasso",
            () -> new LassoFabricItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1), LassoType.CREATIVE)
    );

    public static void touch() {

    }
}
