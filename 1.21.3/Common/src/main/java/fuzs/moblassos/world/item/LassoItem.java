package fuzs.moblassos.world.item;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.config.ServerConfig;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.util.LassoMobHelper;
import fuzs.puzzleslib.api.core.v1.Proxy;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.init.v3.registry.LookupHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LassoItem extends Item {
    public static final String KEY_REMAINING_TIME_IN_SECONDS = "item.moblassos.lasso.remaining";
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    private final LassoType type;

    public LassoItem(Properties properties, LassoType type) {
        super(properties.stacksTo(1)
                // don't hide the pretty colored inside from rendering
                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
                .component(DataComponents.ENTITY_DATA, CustomData.EMPTY));
        this.type = type;
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
                                    level.getGameTime()
                            );
                        }
                    }
                }
            }
            return EventResultHolder.interrupt(InteractionResult.sidedSuccess(player.level().isClientSide));
        }
        return EventResultHolder.pass();
    }

    public int getColor(ItemStack itemStack, int tintIndex) {
        if (tintIndex == 0) return -1;
        SpawnEggItem spawnEggItem = SpawnEggItem.byId(this.getStoredEntityType(itemStack));
        if (spawnEggItem == null) return -1;
        return spawnEggItem.getColor(tintIndex - 1);
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
            long releaseTime = level.getGameTime() + Math.min(0,
                    currentHoldingTime - this.getMaxHoldingTime(level, itemStack)
            ) / 5;
            itemStack.set(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value(), releaseTime);
        }
    }

    private boolean releaseContentAt(CompoundTag tag, Level level, BlockPos pos, ItemStack itemStack) {
        if (!level.isClientSide && !tag.isEmpty()) {
            LassoMobHelper.removeTagKeys((ServerLevel) level, tag);
            return EntityType.create(tag, level).map((entity) -> {

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
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (this.type == LassoType.HOSTILE && this.hasStoredEntity(itemStack)) {
            int hostileDamageRate = MobLassos.CONFIG.get(ServerConfig.class).hostileDamageRate;
            if (hostileDamageRate != -1 && level.getGameTime() % (hostileDamageRate * 20L) == 0) {
                entity.hurt(level.damageSources().magic(), 1.0F);
            }
        }
        if (this.type.hasMaxHoldingTime()) {
            if (itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value())) {
                int maxHoldingTime = this.getMaxHoldingTime(level, itemStack);
                long currentHoldingTime = this.getCurrentHoldingTime(level, itemStack,
                        ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value(), maxHoldingTime
                );
                if (currentHoldingTime >= maxHoldingTime) {
                    this.releaseContents(entity, level, itemStack, entity.blockPosition(), entity.blockPosition());
                    this.tryConvertPickUpTime(level, itemStack);
                }
            }
            if (itemStack.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value())) {
                int maxHoldingTime = this.getMaxHoldingTime(level, itemStack) / 5;
                long currentHoldingTime = this.getCurrentHoldingTime(level, itemStack,
                        ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value(), maxHoldingTime
                );
                if (currentHoldingTime >= maxHoldingTime) {
                    itemStack.remove(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value());
                }
            }
        }
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return this.type.hasMaxHoldingTime() && (itemStack.has(
                ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value()) || itemStack.has(
                ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value()));
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        int maxHoldingTime = this.getMaxHoldingTime(Proxy.INSTANCE.getClientLevel(), itemStack);
        long currentHoldingTime = 0;
        if (itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value())) {
            currentHoldingTime = this.getCurrentHoldingTime(Proxy.INSTANCE.getClientLevel(), itemStack,
                    ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value(), maxHoldingTime
            );
        }
        if (itemStack.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value())) {
            maxHoldingTime /= 5;
            currentHoldingTime = maxHoldingTime - this.getCurrentHoldingTime(Proxy.INSTANCE.getClientLevel(), itemStack,
                    ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value(), maxHoldingTime
            );
        }
        return Math.round(13.0F - currentHoldingTime * 13.0F / maxHoldingTime);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return BAR_COLOR;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (context != TooltipContext.EMPTY) {
            if (this.hasStoredEntity(itemStack)) {
                MutableComponent component = Component.translatable("gui.entity_tooltip.type",
                        this.getStoredEntityType(itemStack).getDescription()
                );
                tooltipComponents.add(component.withStyle(ChatFormatting.BLUE));
            } else {
                tooltipComponents.add(
                        Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GOLD));
            }
            if (tooltipFlag.isAdvanced() && MobLassos.CONFIG.getHolder(ServerConfig.class).isAvailable()) {
                boolean hasPickUpTime = itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value());
                boolean hasReleaseTime = itemStack.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value());
                if (hasPickUpTime || hasReleaseTime) {
                    int maxHoldingTime = this.getMaxHoldingTime(context.registries(), itemStack);
                    long currentHoldingTime = 0;
                    if (hasPickUpTime) {
                        currentHoldingTime = this.getCurrentHoldingTime(Proxy.INSTANCE.getClientLevel(), itemStack,
                                ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value(), maxHoldingTime
                        );
                    }
                    if (hasReleaseTime) {
                        maxHoldingTime /= 5;
                        currentHoldingTime = this.getCurrentHoldingTime(Proxy.INSTANCE.getClientLevel(), itemStack,
                                ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value(), maxHoldingTime
                        );
                    }
                    tooltipComponents.add(Component.translatable(KEY_REMAINING_TIME_IN_SECONDS,
                            (maxHoldingTime - currentHoldingTime) / 20
                    ).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }

    private long getCurrentHoldingTime(Level level, ItemStack itemStack, DataComponentType<Long> dataComponentType, int maxHoldingTime) {
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
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}