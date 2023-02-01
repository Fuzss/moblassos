package fuzs.moblassos.init;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.world.item.LassoForgeItem;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModRegistryForge {
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(MobLassos.MOD_ID);
    public static final RegistryReference<Item> GOLDEN_LASSO_ITEM = REGISTRY.registerItem("golden_lasso", () -> new LassoForgeItem(new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB).stacksTo(1), LassoItem.Type.GOLDEN));
    public static final RegistryReference<Item> AQUA_LASSO_ITEM = REGISTRY.registerItem("aqua_lasso", () -> new LassoForgeItem(new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB).stacksTo(1), LassoItem.Type.AQUA));
    public static final RegistryReference<Item> DIAMOND_LASSO_ITEM = REGISTRY.registerItem("diamond_lasso", () -> new LassoForgeItem(new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB).stacksTo(1), LassoItem.Type.DIAMOND));
    public static final RegistryReference<Item> EMERALD_LASSO_ITEM = REGISTRY.registerItem("emerald_lasso", () -> new LassoForgeItem(new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB).stacksTo(1), LassoItem.Type.EMERALD));
    public static final RegistryReference<Item> HOSTILE_LASSO_ITEM = REGISTRY.registerItem("hostile_lasso", () -> new LassoForgeItem(new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB).stacksTo(1), LassoItem.Type.HOSTILE));
    public static final RegistryReference<Item> CREATIVE_LASSO_ITEM = REGISTRY.registerItem("creative_lasso", () -> new LassoForgeItem(new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB).rarity(Rarity.EPIC).stacksTo(1), LassoItem.Type.CREATIVE));

    public static void touch() {

    }
}
