package fuzs.moblassos.world.item;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.config.ServerConfig;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.util.LassoMobHelper;
import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.core.v1.Proxy;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LassoItem extends Item {
    public static final String TAG_STORED_ENTITY = "StoredEntity";
    public static final String TAG_ENTITY_PICK_UP_TIME = "EntityPickUpTime";
    public static final String TAG_ENTITY_RELEASE_TIME = "EntityReleaseTime";
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    private final Type type;

    public LassoItem(Properties properties, Type type) {
        super(properties);
        this.type = type;
    }

    public static EventResultHolder<InteractionResult> onEntityInteract(Player player, Level level, InteractionHand hand, Entity entity) {
        // we do not override the item method, this is called by an event/callback instead to allow overriding interaction implemented on the entity which runs first and might prevent all this
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof LassoItem item && entity instanceof Mob mob && entity.isAlive()) {
            if (!stack.hasTag() || !stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG)) {
                if (!item.hasStoredEntity(stack) && item.type.canPlayerPickUp(player, mob)) {
                    if (!player.level.isClientSide) {
                        entity.playSound(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.get());
                        if (entity.hasCustomName()) stack.setHoverName(entity.getCustomName());
                        CompoundTag tag = LassoMobHelper.saveEntity(entity);
                        entity.discard();
                        stack.addTagElement(TAG_STORED_ENTITY, tag);
                        if (item.type.hasMaxHoldingTime()) {
                            stack.getTag().putLong(TAG_ENTITY_PICK_UP_TIME, level.getGameTime());
                        }
                    }
                    return EventResultHolder.interrupt(InteractionResult.sidedSuccess(player.level.isClientSide));
                }
            }
        }
        return EventResultHolder.pass();
    }

    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 0) return -1;
        SpawnEggItem spawnEggItem = SpawnEggItem.byId(this.getStoredEntityType(stack));
        if (spawnEggItem == null) return -1;
        return spawnEggItem.getColor(tintIndex - 1);
    }

    public boolean hasStoredEntity(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(TAG_STORED_ENTITY);
    }

    @Nullable
    public EntityType<?> getStoredEntityType(ItemStack stack) {
        if (!this.hasStoredEntity(stack)) return null;
        return EntityType.by(stack.getTagElement(TAG_STORED_ENTITY)).orElse(null);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!this.hasStoredEntity(context.getItemInHand())) return InteractionResult.PASS;
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemStack = context.getItemInHand();
            BlockPos clickedPos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockState = level.getBlockState(clickedPos);

            BlockPos releasePos;
            if (blockState.getCollisionShape(level, clickedPos).isEmpty()) {
                releasePos = clickedPos;
            } else {
                releasePos = clickedPos.relative(direction);
            }

            this.releaseContents(context.getPlayer(), level, itemStack, clickedPos, releasePos);
            this.tryConvertPickUpTime(level, itemStack);

            return InteractionResult.CONSUME;
        }
    }

    private void releaseContents(Entity entity, Level level, ItemStack itemStack, BlockPos clickedPos, BlockPos releasePos) {
        CompoundTag tag = itemStack.getTagElement(TAG_STORED_ENTITY);

        if (this.releaseContentAt(tag, level, releasePos, itemStack)) {
            level.gameEvent(entity, GameEvent.ENTITY_PLACE, clickedPos);
        }

        itemStack.removeTagKey(TAG_STORED_ENTITY);

        itemStack.resetHoverName();

        this.tryClearTag(itemStack);
    }

    private void tryConvertPickUpTime(Level level, ItemStack itemStack) {
        if (itemStack.hasTag() && itemStack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG)) {
            long pickUpTime = itemStack.getTag().getLong(TAG_ENTITY_PICK_UP_TIME);
            itemStack.getTag().remove(TAG_ENTITY_PICK_UP_TIME);
            long currentHoldingTime = level.getGameTime() - pickUpTime;
            itemStack.getTag().putLong(TAG_ENTITY_RELEASE_TIME, level.getGameTime() + Math.min(0, currentHoldingTime - this.getMaxHoldingTime(itemStack)) / 5);
            this.tryClearTag(itemStack);
        }
    }

    private boolean releaseContentAt(CompoundTag tag, Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !tag.isEmpty()) {
            this.maybeRemoveUUID(tag, (ServerLevel) level);
            return EntityType.create(tag, level).map((entity) -> {

                LassoMobHelper.moveEntityTo(entity, level, pos, true);
                entity.setDeltaMovement(Vec3.ZERO);

                level.addFreshEntity(entity);

                if (stack.hasCustomHoverName() && entity instanceof LivingEntity) {
                    entity.setCustomName(stack.getHoverName());
                }

                entity.playSound(ModRegistry.LASSO_RELEASE_SOUND_EVENT.get());

                return entity;
            }).isPresent();
        }

        return false;
    }

    private void maybeRemoveUUID(CompoundTag tag, ServerLevel level) {
        if (level.getEntity(tag.getUUID(Entity.UUID_TAG)) != null) {
            // causes an issue with duplicate uuids when the lasso stack is copied in creative mode,
            // but we should not always remove the uuid as we rely on it for the villager contract
            tag.remove(Entity.UUID_TAG);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (this.type == Type.HOSTILE && this.hasStoredEntity(stack)) {
            int hostileDamageRate = MobLassos.CONFIG.get(ServerConfig.class).hostileDamageRate;
            if (hostileDamageRate != -1 && level.getGameTime() % (hostileDamageRate * 20L) == 0) {
                entity.hurt(DamageSource.MAGIC, 1.0F);
            }
        }
        if (this.type.hasMaxHoldingTime() && stack.hasTag()) {
            if (stack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG)) {
                int maxHoldingTime = this.getMaxHoldingTime(stack);
                long currentHoldingTime = this.getCurrentHoldingTime(level, stack, TAG_ENTITY_PICK_UP_TIME, maxHoldingTime);
                if (currentHoldingTime >= maxHoldingTime) {
                    this.releaseContents(entity, level, stack, entity.blockPosition(), entity.blockPosition());
                    this.tryConvertPickUpTime(level, stack);
                }
            }
            if (stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG)) {
                int maxHoldingTime = this.getMaxHoldingTime(stack) / 5;
                long currentHoldingTime = this.getCurrentHoldingTime(level, stack, TAG_ENTITY_RELEASE_TIME, maxHoldingTime);
                if (currentHoldingTime >= maxHoldingTime) {
                    stack.getTag().remove(TAG_ENTITY_RELEASE_TIME);
                    this.tryClearTag(stack);
                }
            }
        }
    }

    private void tryClearTag(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().isEmpty()) {
            stack.setTag(null);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.type.hasMaxHoldingTime() && stack.hasTag() && (stack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG) || stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG));
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int maxHoldingTime = this.getMaxHoldingTime(stack);
        long currentHoldingTime = 0;
        if (stack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG)) {
            currentHoldingTime = this.getCurrentHoldingTime(Proxy.INSTANCE.getClientLevel(), stack, TAG_ENTITY_PICK_UP_TIME, maxHoldingTime);
        }
        if (stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG)) {
            maxHoldingTime /= 5;
            currentHoldingTime = maxHoldingTime - this.getCurrentHoldingTime(Proxy.INSTANCE.getClientLevel(), stack, TAG_ENTITY_RELEASE_TIME, maxHoldingTime);
        }
        return Math.round(13.0F - currentHoldingTime * 13.0F / maxHoldingTime);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (this.hasStoredEntity(stack)) {
            MutableComponent component = Component.translatable("gui.entity_tooltip.type", this.getStoredEntityType(stack).getDescription());
            tooltipComponents.add(component.withStyle(ChatFormatting.BLUE));
        } else {
            tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        }
        if (isAdvanced.isAdvanced() && stack.hasTag() && MobLassos.CONFIG.getHolder(ServerConfig.class).isAvailable()) {
            boolean hasPickUpTime = stack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG);
            boolean hasReleaseTime = stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG);
            if (hasPickUpTime || hasReleaseTime) {
                int maxHoldingTime = this.getMaxHoldingTime(stack);
                long currentHoldingTime = 0;
                if (hasPickUpTime) {
                    currentHoldingTime = this.getCurrentHoldingTime(Proxy.INSTANCE.getClientLevel(), stack, TAG_ENTITY_PICK_UP_TIME, maxHoldingTime);
                }
                if (hasReleaseTime) {
                    maxHoldingTime /= 5;
                    currentHoldingTime = this.getCurrentHoldingTime(Proxy.INSTANCE.getClientLevel(), stack, TAG_ENTITY_RELEASE_TIME, maxHoldingTime);
                }
                tooltipComponents.add(Component.translatable("item.moblassos.lasso.remaining", (maxHoldingTime - currentHoldingTime) / 20).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private long getCurrentHoldingTime(Level level, ItemStack stack, String timeTagKey, int maxHoldingTime) {
        long time = stack.getTag().getLong(timeTagKey);
        long currentHoldingTime = level.getGameTime() - time;
        return Math.min(currentHoldingTime, maxHoldingTime);
    }

    public int getMaxHoldingTime(ItemStack stack) {
        int time = this.type.getMaxHoldingTime();
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.HOLDING_ENCHANTMENT.get(), stack);
        if (level > 0) time += time * level * MobLassos.CONFIG.get(ServerConfig.class).holdingMultiplier;
        return time;
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        if (this.hasStoredEntity(stack)) {
            CompoundTag storedEntity = stack.getTagElement(TAG_STORED_ENTITY);
            this.releaseContentAt(storedEntity, itemEntity.level, itemEntity.blockPosition(), stack);
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

    @Override
    public boolean isFoil(ItemStack stack) {
        // don't hide the pretty colored inside from rendering
        return false;
    }

    public enum Type {
        GOLDEN(entity -> (entity instanceof Animal || entity instanceof AmbientCreature) && !(entity instanceof Enemy), () -> MobLassos.CONFIG.get(ServerConfig.class).goldenLassoTime, () -> ModRegistry.GOLDEN_LASSO_BLACKLIST_ENTITY_TYPE_TAG),
        AQUA(entity -> (entity instanceof WaterAnimal) && !(entity instanceof Enemy), () -> MobLassos.CONFIG.get(ServerConfig.class).aquaLassoTime, () -> ModRegistry.AQUA_LASSO_BLACKLIST_ENTITY_TYPE_TAG),
        DIAMOND(entity -> (entity instanceof Animal || entity instanceof AmbientCreature || entity instanceof WaterAnimal) && !(entity instanceof Enemy), () -> MobLassos.CONFIG.get(ServerConfig.class).diamondLassoTime, () -> ModRegistry.DIAMOND_LASSO_BLACKLIST_ENTITY_TYPE_TAG),
        EMERALD(entity -> entity instanceof AbstractVillager && ModRegistry.VILLAGER_CONTRACT_CAPABILITY.maybeGet(entity).filter(VillagerContractCapability::hasAcceptedContract).isPresent(), () -> MobLassos.CONFIG.get(ServerConfig.class).emeraldLassoTime, () -> ModRegistry.EMERALD_LASSO_BLACKLIST_ENTITY_TYPE_TAG),
        HOSTILE(entity -> entity instanceof Enemy, () -> MobLassos.CONFIG.get(ServerConfig.class).hostileLassoTime, () -> ModRegistry.HOSTILE_LASSO_BLACKLIST_ENTITY_TYPE_TAG),
        CREATIVE(entity -> true, () -> MobLassos.CONFIG.get(ServerConfig.class).creativeLassoTime, () -> ModRegistry.CREATIVE_LASSO_BLACKLIST_ENTITY_TYPE_TAG);

        private final Predicate<Mob> mobFilter;
        private final IntSupplier holdingTime;
        private final Supplier<TagKey<EntityType<?>>> blacklist;

        Type(Predicate<Mob> mobFilter, IntSupplier holdingTime, Supplier<TagKey<EntityType<?>>> blacklist) {
            this.mobFilter = mobFilter;
            this.holdingTime = holdingTime;
            this.blacklist = blacklist;
        }

        public boolean hasMaxHoldingTime() {
            return this.holdingTime.getAsInt() != -1;
        }

        public int getMaxHoldingTime() {
            return this.holdingTime.getAsInt() * 20;
        }

        public boolean canPlayerPickUp(Player player, Mob mob) {
            if (CommonAbstractions.INSTANCE.isBossMob(mob.getType())) return false;
            double hostileMobHealth = MobLassos.CONFIG.get(ServerConfig.class).hostileMobHealth;
            if (!mob.getType().is(this.blacklist.get()) && this.mobFilter.test(mob)) {
                if (this == HOSTILE && mob.getHealth() / mob.getMaxHealth() >= hostileMobHealth) {
                    player.displayClientMessage(Component.translatable(ModRegistry.HOSTILE_LASSO_ITEM.get().getDescriptionId() + ".hostile", mob.getDisplayName(), String.format("%.0f", hostileMobHealth * mob.getMaxHealth()), String.format("%.0f", mob.getHealth())).withStyle(ChatFormatting.RED), true);
                    return false;
                }
                return true;
            }
            return false;
        }
    }
}
