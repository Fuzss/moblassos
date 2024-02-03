package fuzs.moblassos.init;

import fuzs.extensibleenums.api.v2.CommonAbstractions;
import fuzs.moblassos.MobLassos;
import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.world.item.ContractItem;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.moblassos.world.item.enchantment.HoldingEnchantment;
import fuzs.puzzleslib.api.capability.v3.CapabilityController;
import fuzs.puzzleslib.api.capability.v3.data.EntityCapabilityKey;
import fuzs.puzzleslib.api.capability.v3.data.SyncStrategy;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.BoundTagFactory;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ModRegistry {
    public static final EnchantmentCategory LASSO_ENCHANTMENT_CATEGORY = CommonAbstractions.createEnchantmentCategory(
            MobLassos.id("lasso"),
            item -> item instanceof LassoItem
    );

    static final RegistryManager REGISTRY = RegistryManager.from(MobLassos.MOD_ID);
    public static final Holder.Reference<Item> GOLDEN_LASSO_ITEM = REGISTRY.registerLazily(Registries.ITEM,
            "golden_lasso"
    );
    public static final Holder.Reference<Item> AQUA_LASSO_ITEM = REGISTRY.registerLazily(Registries.ITEM, "aqua_lasso");
    public static final Holder.Reference<Item> DIAMOND_LASSO_ITEM = REGISTRY.registerLazily(Registries.ITEM,
            "diamond_lasso"
    );
    public static final Holder.Reference<Item> EMERALD_LASSO_ITEM = REGISTRY.registerLazily(Registries.ITEM,
            "emerald_lasso"
    );
    public static final Holder.Reference<Item> HOSTILE_LASSO_ITEM = REGISTRY.registerLazily(Registries.ITEM,
            "hostile_lasso"
    );
    public static final Holder.Reference<Item> CREATIVE_LASSO_ITEM = REGISTRY.registerLazily(Registries.ITEM,
            "creative_lasso"
    );
    public static final Holder.Reference<Item> CONTRACT_ITEM = REGISTRY.registerItem("contract",
            () -> new ContractItem(new Item.Properties().stacksTo(1))
    );
    public static final Holder.Reference<Enchantment> HOLDING_ENCHANTMENT = REGISTRY.registerEnchantment("holding",
            () -> new HoldingEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND)
    );
    public static final Holder.Reference<SoundEvent> LASSO_PICK_UP_SOUND_EVENT = REGISTRY.registerSoundEvent(
            "item.lasso.pick_up");
    public static final Holder.Reference<SoundEvent> LASSO_RELEASE_SOUND_EVENT = REGISTRY.registerSoundEvent(
            "item.lasso.release");

    static final CapabilityController CAPABILITY = CapabilityController.from(MobLassos.MOD_ID);
    public static final EntityCapabilityKey<AbstractVillager, VillagerContractCapability> VILLAGER_CONTRACT_CAPABILITY = CAPABILITY.registerEntityCapability(
            "villager_contract",
            VillagerContractCapability.class,
            VillagerContractCapability::new,
            AbstractVillager.class
    ).setSyncStrategy(SyncStrategy.TRACKING);

    static final BoundTagFactory TAGS = BoundTagFactory.make(MobLassos.MOD_ID);
    public static final TagKey<Item> LASSOS_ITEM_TAG = TAGS.registerItemTag("lassos");

    public static void touch() {

    }
}
