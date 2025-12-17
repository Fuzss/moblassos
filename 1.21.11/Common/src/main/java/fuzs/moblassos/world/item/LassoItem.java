package fuzs.moblassos.world.item;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.config.ServerConfig;
import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.item.v2.EnchantingHelper;
import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import fuzs.puzzleslib.impl.core.proxy.ProxyImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;

public class LassoItem extends Item {
    public static final String KEY_REMAINING_TIME_IN_SECONDS = "item.moblassos.lasso.remaining";
    private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.4F, 0.4F, 1.0F);

    private final LassoType type;

    public LassoItem(Properties properties, LassoType type) {
        super(properties);
        this.type = type;
    }

    public static EventResultHolder<InteractionResult> onEntityInteract(Player player, Level level, InteractionHand interactionHand, Entity entity) {
        // we do not override the item method, this is called by an event/callback instead to allow overriding interaction implemented on the entity which runs first and might prevent all this
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if (itemInHand.getItem() instanceof LassoItem item && entity instanceof Mob mob && entity.isAlive()) {
            if (level instanceof ServerLevel
                    && !itemInHand.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value())) {
                if (!item.hasOccupant(itemInHand) && item.type.canPlayerPickUp(player, mob)) {
                    item.storeOccupant(itemInHand, mob, player);
                    if (item.type.hasMaxHoldingTime()) {
                        itemInHand.set(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value(),
                                level.getGameTime());
                    }
                }
            }

            return EventResultHolder.interrupt(InteractionResultHelper.sidedSuccess(player.level().isClientSide()));
        }

        return EventResultHolder.pass();
    }

    /**
     * @see BeehiveBlockEntity#addOccupant(Bee)
     */
    public void storeOccupant(ItemStack itemStack, Mob mob, Player player) {
        mob.stopRiding();
        mob.ejectPassengers();
        mob.dropLeash();
        TypedEntityData<EntityType<?>> entityData = BeehiveBlockEntity.Occupant.of(mob).entityData();
        itemStack.set(DataComponents.ENTITY_DATA, entityData);
        if (mob.hasCustomName()) {
            itemStack.set(DataComponents.CUSTOM_NAME, mob.getCustomName());
        }

        player.playSound(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.value());
        mob.discard();
    }

    public boolean hasOccupant(ItemStack itemStack) {
        return itemStack.has(DataComponents.ENTITY_DATA);
    }

    @Nullable
    public EntityType<?> getStoredEntityType(ItemStack itemStack) {
        if (itemStack.has(DataComponents.ENTITY_DATA)) {
            return itemStack.get(DataComponents.ENTITY_DATA).type();
        } else {
            return null;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (this.hasOccupant(context.getItemInHand())) {
            Level level = context.getLevel();
            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            } else {
                ItemStack itemInHand = context.getItemInHand();
                BlockPos blockPos = this.getReleasePosition(context);
                this.releaseContents(context.getPlayer(), (ServerLevel) level, itemInHand, blockPos);
                this.tryConvertPickUpTime(level, itemInHand);
                return InteractionResult.CONSUME;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private BlockPos getReleasePosition(UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        if (blockState.getCollisionShape(context.getLevel(), blockPos).isEmpty()) {
            return blockPos;
        } else {
            return blockPos.relative(context.getClickedFace());
        }
    }

    public void releaseContents(@Nullable Entity carrierEntity, ServerLevel serverLevel, ItemStack itemStack, BlockPos blockPos) {
        if (itemStack.has(DataComponents.ENTITY_DATA)) {
            EntityType<?> entityType = itemStack.get(DataComponents.ENTITY_DATA).type();
            Entity entity = entityType.create(serverLevel,
                    EntityType.createDefaultStackConfig(serverLevel, itemStack, null),
                    blockPos,
                    EntitySpawnReason.BUCKET,
                    true,
                    false);
            if (entity != null) {
                serverLevel.addFreshEntity(entity);
                entity.playSound(ModRegistry.LASSO_RELEASE_SOUND_EVENT.value());
                serverLevel.gameEvent(carrierEntity, GameEvent.ENTITY_PLACE, blockPos);
            }
        }

        itemStack.remove(DataComponents.ENTITY_DATA);
        itemStack.remove(DataComponents.CUSTOM_NAME);
    }

    public void tryConvertPickUpTime(Level level, ItemStack itemStack) {
        if (itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value())) {
            long pickUpTime = itemStack.get(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value());
            itemStack.remove(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value());
            long currentHoldingTime = level.getGameTime() - pickUpTime;
            long releaseTime = level.getGameTime()
                    + Math.min(0, currentHoldingTime - this.getMaxHoldingTime(level, itemStack)) / 5;
            itemStack.set(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value(), releaseTime);
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel serverLevel, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        if (this.type == LassoType.HOSTILE && this.hasOccupant(itemStack)) {
            int hostileDamageRate = MobLassos.CONFIG.get(ServerConfig.class).hostileDamageRate;
            if (hostileDamageRate != -1 && serverLevel.getGameTime() % (hostileDamageRate * 20L) == 0) {
                entity.hurtServer(serverLevel, serverLevel.damageSources().magic(), 1.0F);
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
                    this.releaseContents(entity, serverLevel, itemStack, entity.blockPosition());
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
        return this.type.hasMaxHoldingTime() && (
                itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value())
                        || itemStack.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value()));
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
        Holder<Enchantment> enchantment = EnchantingHelper.lookup(registries, ModRegistry.HOLDING_ENCHANTMENT);
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack);
        if (enchantmentLevel > 0) {
            time += (int) (time * enchantmentLevel * MobLassos.CONFIG.get(ServerConfig.class).holdingMultiplier);
        }
        return time;
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        if (itemEntity.level() instanceof ServerLevel serverLevel && this.hasOccupant(itemEntity.getItem())) {
            this.releaseContents(null, serverLevel, itemEntity.getItem(), itemEntity.blockPosition());
        }
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}
