package fuzs.moblassos.init;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.capability.VillagerContractCapabilityImpl;
import fuzs.moblassos.world.item.ContractItem;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.moblassos.world.item.enchantment.HoldingEnchantment;
import fuzs.puzzleslib.api.capability.v2.CapabilityController;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import fuzs.puzzleslib.api.init.v2.RegistryManager;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Locale;

public class ModRegistry {
    public static final EnchantmentCategory LASSO_ENCHANTMENT_CATEGORY = fuzs.moblassos.core.CommonAbstractions.INSTANCE.createEnchantmentCategory(MobLassos.MOD_NAME.toUpperCase(Locale.ROOT).replace(" ", "_") + "_LASSO", item -> item instanceof LassoItem);
    
    static final RegistryManager REGISTRY = RegistryManager.instant(MobLassos.MOD_ID);
    public static final RegistryReference<Item> GOLDEN_LASSO_ITEM = REGISTRY.placeholder(Registries.ITEM, "golden_lasso");
    public static final RegistryReference<Item> AQUA_LASSO_ITEM = REGISTRY.placeholder(Registries.ITEM, "aqua_lasso");
    public static final RegistryReference<Item> DIAMOND_LASSO_ITEM = REGISTRY.placeholder(Registries.ITEM, "diamond_lasso");
    public static final RegistryReference<Item> EMERALD_LASSO_ITEM = REGISTRY.placeholder(Registries.ITEM, "emerald_lasso");
    public static final RegistryReference<Item> HOSTILE_LASSO_ITEM = REGISTRY.placeholder(Registries.ITEM, "hostile_lasso");
    public static final RegistryReference<Item> CREATIVE_LASSO_ITEM = REGISTRY.placeholder(Registries.ITEM, "creative_lasso");
    public static final RegistryReference<Item> CONTRACT_ITEM = REGISTRY.registerItem("contract", () -> new ContractItem(new Item.Properties().stacksTo(1)));
    public static final RegistryReference<Enchantment> HOLDING_ENCHANTMENT = REGISTRY.registerEnchantment("holding", () -> new HoldingEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
    public static final RegistryReference<SoundEvent> LASSO_PICK_UP_SOUND_EVENT = REGISTRY.registerSoundEvent("item.lasso.pick_up");
    public static final RegistryReference<SoundEvent> LASSO_RELEASE_SOUND_EVENT = REGISTRY.registerSoundEvent("item.lasso.release");

    private static final CapabilityController CAPABILITY = CapabilityController.from(MobLassos.MOD_ID);
    public static final CapabilityKey<VillagerContractCapability> VILLAGER_CONTRACT_CAPABILITY = CAPABILITY.registerEntityCapability("villager_contract", VillagerContractCapability.class, entity -> new VillagerContractCapabilityImpl(), AbstractVillager.class);

    public static final TagKey<Item> LASSOS_ITEM_TAG = REGISTRY.registerItemTag("lassos");
    public static final TagKey<EntityType<?>> GOLDEN_LASSO_BLACKLIST_ENTITY_TYPE_TAG = REGISTRY.registerEntityTypeTag("golden_lasso_blacklist");
    public static final TagKey<EntityType<?>> AQUA_LASSO_BLACKLIST_ENTITY_TYPE_TAG = REGISTRY.registerEntityTypeTag("aqua_lasso_blacklist");
    public static final TagKey<EntityType<?>> DIAMOND_LASSO_BLACKLIST_ENTITY_TYPE_TAG = REGISTRY.registerEntityTypeTag("diamond_lasso_blacklist");
    public static final TagKey<EntityType<?>> EMERALD_LASSO_BLACKLIST_ENTITY_TYPE_TAG = REGISTRY.registerEntityTypeTag("emerald_lasso_blacklist");
    public static final TagKey<EntityType<?>> HOSTILE_LASSO_BLACKLIST_ENTITY_TYPE_TAG = REGISTRY.registerEntityTypeTag("hostile_lasso_blacklist");
    public static final TagKey<EntityType<?>> CREATIVE_LASSO_BLACKLIST_ENTITY_TYPE_TAG = REGISTRY.registerEntityTypeTag("creative_lasso_blacklist");

    public static void touch() {

    }
}
