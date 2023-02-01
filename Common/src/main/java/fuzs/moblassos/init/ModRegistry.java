package fuzs.moblassos.init;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.capability.VillagerContractCapabilityImpl;
import fuzs.moblassos.world.item.ContractItem;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.moblassos.world.item.enchantment.HoldingEnchantment;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import fuzs.puzzleslib.core.CommonAbstractions;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Locale;

public class ModRegistry {
    public static final CreativeModeTab CREATIVE_MODE_TAB = CommonAbstractions.INSTANCE.creativeModeTab(MobLassos.MOD_ID, () -> new ItemStack(ModRegistry.GOLDEN_LASSO_ITEM.get()));
    public static final EnchantmentCategory LASSO_ENCHANTMENT_CATEGORY = fuzs.moblassos.core.CommonAbstractions.INSTANCE.createEnchantmentCategory(MobLassos.MOD_NAME.toUpperCase(Locale.ROOT).replace(" ", "_") + "_LASSO", item -> item instanceof LassoItem);
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(MobLassos.MOD_ID);
    public static final RegistryReference<Item> GOLDEN_LASSO_ITEM = REGISTRY.placeholder(Registry.ITEM_REGISTRY, "golden_lasso");
    public static final RegistryReference<Item> AQUA_LASSO_ITEM = REGISTRY.placeholder(Registry.ITEM_REGISTRY, "aqua_lasso");
    public static final RegistryReference<Item> DIAMOND_LASSO_ITEM = REGISTRY.placeholder(Registry.ITEM_REGISTRY, "diamond_lasso");
    public static final RegistryReference<Item> EMERALD_LASSO_ITEM = REGISTRY.placeholder(Registry.ITEM_REGISTRY, "emerald_lasso");
    public static final RegistryReference<Item> HOSTILE_LASSO_ITEM = REGISTRY.placeholder(Registry.ITEM_REGISTRY, "hostile_lasso");
    public static final RegistryReference<Item> CREATIVE_LASSO_ITEM = REGISTRY.placeholder(Registry.ITEM_REGISTRY, "creative_lasso");
    public static final RegistryReference<Item> CONTRACT_ITEM = REGISTRY.registerItem("contract", () -> new ContractItem(new Item.Properties().tab(CREATIVE_MODE_TAB).stacksTo(1)));
    public static final RegistryReference<Enchantment> HOLDING_ENCHANTMENT = REGISTRY.registerEnchantment("holding", () -> new HoldingEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryReference<SoundEvent> LASSO_PICK_UP_SOUND_EVENT = REGISTRY.registerRawSoundEvent("item.lasso.pick_up");
    public static final RegistryReference<SoundEvent> LASSO_RELEASE_SOUND_EVENT = REGISTRY.registerRawSoundEvent("item.lasso.release");

    private static final CapabilityController CAPABILITY = CommonFactories.INSTANCE.capabilities(MobLassos.MOD_ID);
    public static final CapabilityKey<VillagerContractCapability> VILLAGER_CONTRACT_CAPABILITY = CAPABILITY.registerEntityCapability("villager_contract", VillagerContractCapability.class, entity -> new VillagerContractCapabilityImpl(), AbstractVillager.class);

    public static final TagKey<Item> LASSOS_ITEM_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "lassos"));
    public static final TagKey<EntityType<?>> GOLDEN_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "golden_lasso_blacklist"));
    public static final TagKey<EntityType<?>> AQUA_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "aqua_lasso_blacklist"));
    public static final TagKey<EntityType<?>> DIAMOND_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "diamond_lasso_blacklist"));
    public static final TagKey<EntityType<?>> EMERALD_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "emerald_lasso_blacklist"));
    public static final TagKey<EntityType<?>> HOSTILE_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "hostile_lasso_blacklist"));
    public static final TagKey<EntityType<?>> CREATIVE_LASSO_BLACKLIST_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobLassos.MOD_ID, "creative_lasso_blacklist"));

    public static void touch() {

    }
}
