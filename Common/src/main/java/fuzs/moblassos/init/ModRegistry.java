package fuzs.moblassos.init;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.moblassos.world.item.enchantment.HoldingEnchantment;
import fuzs.puzzleslib.core.CommonAbstractions;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModRegistry {
    public static final CreativeModeTab CREATIVE_MODE_TAB = CommonAbstractions.INSTANCE.creativeModeTab(MobLassos.MOD_ID, () -> new ItemStack(ModRegistry.GOLDEN_LASSO_ITEM.get()));
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(MobLassos.MOD_ID);
    public static final RegistryReference<Item> GOLDEN_LASSO_ITEM = REGISTRY.registerItem("golden_lasso", () -> new LassoItem(new Item.Properties().tab(CREATIVE_MODE_TAB).durability(1), LassoItem.Type.GOLDEN));
    public static final RegistryReference<Item> AQUA_LASSO_ITEM = REGISTRY.registerItem("aqua_lasso", () -> new LassoItem(new Item.Properties().tab(CREATIVE_MODE_TAB).durability(1), LassoItem.Type.GOLDEN));
    public static final RegistryReference<Item> DIAMOND_LASSO_ITEM = REGISTRY.registerItem("diamond_lasso", () -> new LassoItem(new Item.Properties().tab(CREATIVE_MODE_TAB).durability(1), LassoItem.Type.GOLDEN));
    public static final RegistryReference<Item> EMERALD_LASSO_ITEM = REGISTRY.registerItem("emerald_lasso", () -> new LassoItem(new Item.Properties().tab(CREATIVE_MODE_TAB).durability(1), LassoItem.Type.GOLDEN));
    public static final RegistryReference<Item> HOSTILE_LASSO_ITEM = REGISTRY.registerItem("hostile_lasso", () -> new LassoItem(new Item.Properties().tab(CREATIVE_MODE_TAB).durability(1), LassoItem.Type.GOLDEN));
    public static final RegistryReference<Item> CREATIVE_LASSO_ITEM = REGISTRY.registerItem("creative_lasso", () -> new LassoItem(new Item.Properties().tab(CREATIVE_MODE_TAB).durability(1), LassoItem.Type.GOLDEN));
    public static final RegistryReference<Item> CONTRACT_ITEM = REGISTRY.registerItem("contract", () -> new Item(new Item.Properties().tab(CREATIVE_MODE_TAB).durability(1)));
    public static final RegistryReference<Enchantment> HOLDING_ENCHANTMENT = REGISTRY.registerEnchantment("holding", () -> new HoldingEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));

    public static final TagKey<Item> LASSOS_ITEM_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "lassos"));
    public static final TagKey<EntityType<?>> GOLDEN_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "golden_lasso_blacklist"));
    public static final TagKey<EntityType<?>> AQUA_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "aqua_lasso_blacklist"));
    public static final TagKey<EntityType<?>> DIAMOND_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "diamond_lasso_blacklist"));
    public static final TagKey<EntityType<?>> EMERALD_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "emerald_lasso_blacklist"));
    public static final TagKey<EntityType<?>> HOSTILE_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "hostile_lasso_blacklist"));

    public static void touch() {

    }
}
