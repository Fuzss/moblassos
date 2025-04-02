package fuzs.moblassos.world.item;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.config.ServerConfig;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.util.LassoMobHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.init.v3.registry.LookupHelper;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import fuzs.puzzleslib.impl.core.proxy.ProxyImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LassoItem extends Item {
    public static final String KEY_REMAINING_TIME_IN_SECONDS = "item.moblassos.lasso.remaining";
    private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.4F, 0.4F, 1.0F);

    private final LassoType type;

    public LassoItem(Properties properties, LassoType type) {
        super(properties);
        this.type = type;
    }

    public static Holder.Reference<Item> registerLassoItem(RegistryManager registries, String path, BiFunction<Properties, LassoType, Item> itemFactory, LassoType lassoType) {
        return registerLassoItem(registries, path, itemFactory, Item.Properties::new, lassoType);
    }

    public static Holder.Reference<Item> registerLassoItem(RegistryManager registries, String path, BiFunction<Item.Properties, LassoType, Item> itemFactory, Supplier<Properties> itemPropertiesSupplier, LassoType lassoType) {
        return registries.registerItem(path,
                (Item.Properties properties) -> itemFactory.apply(properties, lassoType),
                () -> itemPropertiesSupplier.get()
                        .stacksTo(1)
                        .enchantable(1)
                        .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                        .component(DataComponents.ENTITY_DATA, CustomData.EMPTY));
    }

    public static EventResultHolder<InteractionResult> onEntityInteract(Player player, Level level, InteractionHand hand, Entity entity) {
        // we do not override the item method, this is called by an event/callback instead to allow overriding interaction implemented on the entity which runs first and might prevent all this
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.getItem() instanceof LassoItem item && entity instanceof Mob mob && entity.isAlive()) {
            if (!itemInHand.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value())) {
                if (!item.hasStoredEntity(itemInHand) && item.type.canPlayerPickUp(player, mob)) {
                    if (!player.level().isClientSide) {
                        entity.stopRiding();
                        entity.ejectPassengers();
                        entity.playSound(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.value());
                        if (entity.hasCustomName()) {
                            itemInHand.set(DataComponents.CUSTOM_NAME, entity.getCustomName());
                        }
                        CompoundTag compoundTag = LassoMobHelper.saveEntity(entity);
                        entity.discard();
                        itemInHand.set(DataComponents.ENTITY_DATA, CustomData.of(compoundTag));
                        if (item.type.hasMaxHoldingTime()) {
                            itemInHand.set(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value(),
                                    level.getGameTime());
                        }
                    }
                }
            }

            return EventResultHolder.interrupt(InteractionResultHelper.sidedSuccess(player.level().isClientSide));
        }

        return EventResultHolder.pass();
    }

    public boolean hasStoredEntity(ItemStack itemStack) {
        return !itemStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY).isEmpty();
    }

    @Nullable
    public EntityType<?> getStoredEntityType(ItemStack itemStack) {
        CustomData customData = itemStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
        return EntityType.by(customData.getUnsafe()).orElse(null);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (this.hasStoredEntity(context.getItemInHand())) {
            Level level = context.getLevel();
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                ItemStack itemInHand = context.getItemInHand();
                BlockPos blockPos = context.getClickedPos();
                Direction direction = context.getClickedFace();
                BlockState blockState = level.getBlockState(blockPos);

                BlockPos releasePos;
                if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
                    releasePos = blockPos;
                } else {
                    releasePos = blockPos.relative(direction);
                }

                this.releaseContents(context.getPlayer(), level, itemInHand, blockPos, releasePos);
                this.tryConvertPickUpTime(level, itemInHand);

                return InteractionResult.CONSUME;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    public void releaseContents(@Nullable Entity entity, Level level, ItemStack itemStack, BlockPos clickedPos, BlockPos releasePos) {

        CompoundTag tag = itemStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY).copyTag();
        if (this.releaseContentAt(tag, level, releasePos, itemStack)) {

            level.gameEvent(entity, GameEvent.ENTITY_PLACE, clickedPos);
        }

        itemStack.set(DataComponents.ENTITY_DATA, CustomData.EMPTY);
        itemStack.remove(DataComponents.CUSTOM_NAME);
    }

    public void tryConvertPickUpTime(Level level, ItemStack itemStack) {
        if (itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value())) {
            long pickUpTime = itemStack.get(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value());
            itemStack.remove(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value());
            long currentHoldingTime = level.getGameTime() - pickUpTime;
            long releaseTime = level.getGameTime() +
                    Math.min(0, currentHoldingTime - this.getMaxHoldingTime(level, itemStack)) / 5;
            itemStack.set(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value(), releaseTime);
        }
    }

    private boolean releaseContentAt(CompoundTag tag, Level level, BlockPos pos, ItemStack itemStack) {
        if (level instanceof ServerLevel serverLevel && !tag.isEmpty()) {
            LassoMobHelper.removeTagKeys(serverLevel, tag);
            return EntityType.create(tag, level, EntitySpawnReason.BUCKET).map((Entity entity) -> {

                LassoMobHelper.moveEntityTo(entity, level, pos, true);
                entity.setDeltaMovement(Vec3.ZERO);

                level.addFreshEntity(entity);

                if (itemStack.has(DataComponents.CUSTOM_NAME) && entity instanceof LivingEntity) {
                    entity.setCustomName(itemStack.getHoverName());
                }

                entity.playSound(ModRegistry.LASSO_RELEASE_SOUND_EVENT.value());

                return entity;
            }).isPresent();
        }

        return false;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel serverLevel, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        if (this.type == LassoType.HOSTILE && this.hasStoredEntity(itemStack)) {
            int hostileDamageRate = MobLassos.CONFIG.get(ServerConfig.class).hostileDamageRate;
            if (hostileDamageRate != -1 && serverLevel.getGameTime() % (hostileDamageRate * 20L) == 0) {
                entity.hurt(serverLevel.damageSources().magic(), 1.0F);
            }
        }
        if (this.type.hasMaxHoldingTime()) {
            if (itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value())) {
                int maxHoldingTime = this.getMaxHoldingTime(serverLevel, itemStack);
                long currentHoldingTime = this.getCurrentHoldingTime(serverLevel,
                        itemStack,
                        ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value(),
                        maxHoldingTime);
                if (currentHoldingTime >= maxHoldingTime) {
                    this.releaseContents(entity,
                            serverLevel,
                            itemStack,
                            entity.blockPosition(),
                            entity.blockPosition());
                    this.tryConvertPickUpTime(serverLevel, itemStack);
                }
            }
            if (itemStack.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value())) {
                int maxHoldingTime = this.getMaxHoldingTime(serverLevel, itemStack) / 5;
                long currentHoldingTime = this.getCurrentHoldingTime(serverLevel,
                        itemStack,
                        ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value(),
                        maxHoldingTime);
                if (currentHoldingTime >= maxHoldingTime) {
                    itemStack.remove(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value());
                }
            }
        }
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return this.type.hasMaxHoldingTime() &&
                (itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value()) ||
                        itemStack.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value()));
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        int maxHoldingTime = this.getMaxHoldingTime(ProxyImpl.get().getClientLevel(), itemStack);
        long currentHoldingTime = 0;
        if (itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value())) {
            currentHoldingTime = this.getCurrentHoldingTime(ProxyImpl.get().getClientLevel(),
                    itemStack,
                    ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value(),
                    maxHoldingTime);
        }
        if (itemStack.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value())) {
            maxHoldingTime /= 5;
            currentHoldingTime = maxHoldingTime - this.getCurrentHoldingTime(ProxyImpl.get().getClientLevel(),
                    itemStack,
                    ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value(),
                    maxHoldingTime);
        }
        return Math.round(13.0F - currentHoldingTime * 13.0F / maxHoldingTime);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return BAR_COLOR;
    }

    public long getCurrentHoldingTime(Level level, ItemStack itemStack, DataComponentType<Long> dataComponentType, int maxHoldingTime) {
        long time = itemStack.getOrDefault(dataComponentType, -1L);
        long currentHoldingTime = level.getGameTime() - time;
        return Math.min(currentHoldingTime, maxHoldingTime);
    }

    public int getMaxHoldingTime(Level level, ItemStack itemStack) {
        return this.getMaxHoldingTime(level.registryAccess(), itemStack);
    }

    public int getMaxHoldingTime(HolderLookup.Provider registries, ItemStack itemStack) {
        int time = this.type.getMaxHoldingTime();
        Holder<Enchantment> enchantment = LookupHelper.lookupEnchantment(registries, ModRegistry.HOLDING_ENCHANTMENT);
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack);
        if (enchantmentLevel > 0) {
            time += (int) (time * enchantmentLevel * MobLassos.CONFIG.get(ServerConfig.class).holdingMultiplier);
        }
        return time;
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        ItemStack itemStack = itemEntity.getItem();
        if (this.hasStoredEntity(itemStack)) {
            CompoundTag storedEntity = itemStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY).copyTag();
            this.releaseContentAt(storedEntity, itemEntity.level(), itemEntity.blockPosition(), itemStack);
        }
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}
