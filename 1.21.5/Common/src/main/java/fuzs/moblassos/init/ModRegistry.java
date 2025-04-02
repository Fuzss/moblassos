package fuzs.moblassos.init;

import com.mojang.serialization.Codec;
import fuzs.moblassos.MobLassos;
import fuzs.moblassos.world.item.ContractItem;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(MobLassos.MOD_ID);
    public static final Holder.Reference<DataComponentType<Long>> ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE = REGISTRIES.registerDataComponentType(
            "entity_pick_up_time",
            builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));
    public static final Holder.Reference<DataComponentType<Long>> ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE = REGISTRIES.registerDataComponentType(
            "entity_release_time",
            builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));
    public static final Holder.Reference<Item> GOLDEN_LASSO_ITEM = REGISTRIES.registerLazily(Registries.ITEM,
            "golden_lasso");
    public static final Holder.Reference<Item> AQUA_LASSO_ITEM = REGISTRIES.registerLazily(Registries.ITEM,
            "aqua_lasso");
    public static final Holder.Reference<Item> DIAMOND_LASSO_ITEM = REGISTRIES.registerLazily(Registries.ITEM,
            "diamond_lasso");
    public static final Holder.Reference<Item> EMERALD_LASSO_ITEM = REGISTRIES.registerLazily(Registries.ITEM,
            "emerald_lasso");
    public static final Holder.Reference<Item> HOSTILE_LASSO_ITEM = REGISTRIES.registerLazily(Registries.ITEM,
            "hostile_lasso");
    public static final Holder.Reference<Item> CREATIVE_LASSO_ITEM = REGISTRIES.registerLazily(Registries.ITEM,
            "creative_lasso");
    public static final Holder.Reference<Item> CONTRACT_ITEM = REGISTRIES.registerItem("contract",
            ContractItem::new,
            () -> new Item.Properties().stacksTo(1));
    public static final Holder.Reference<CreativeModeTab> CREATIVE_MODE_TAB = REGISTRIES.registerCreativeModeTab(
            GOLDEN_LASSO_ITEM);
    public static final ResourceKey<Enchantment> HOLDING_ENCHANTMENT = REGISTRIES.registerEnchantment("holding");
    public static final Holder.Reference<SoundEvent> LASSO_PICK_UP_SOUND_EVENT = REGISTRIES.registerSoundEvent(
            "item.lasso.pick_up");
    public static final Holder.Reference<SoundEvent> LASSO_RELEASE_SOUND_EVENT = REGISTRIES.registerSoundEvent(
            "item.lasso.release");

    public static final DataAttachmentType<Entity, Unit> VILLAGER_CONTRACT_ATTACHMENT_TYPE = DataAttachmentRegistry.<Unit>entityBuilder()
            .persistent(Unit.CODEC)
            .networkSynchronized(StreamCodec.unit(Unit.INSTANCE), PlayerSet::nearEntity)
            .build(MobLassos.id("villager_contract"));

    static final TagFactory TAGS = TagFactory.make(MobLassos.MOD_ID);
    public static final TagKey<Item> LASSOS_ITEM_TAG = TAGS.registerItemTag("lassos");
    public static final TagKey<Item> LASSO_ENCHANTABLE_ITEM_TAG = TAGS.registerItemTag("enchantable/lasso");
    public static final TagKey<EntityType<?>> BOSSES_ENTITY_TYPE_TAG = TagFactory.COMMON.registerEntityTypeTag("bosses");

    public static void bootstrap() {
        // NO-OP
    }
}
