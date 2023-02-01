package fuzs.moblassos.world.item;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.config.ServerConfig;
import fuzs.moblassos.core.CommonAbstractions;
import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.proxy.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
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

    public static Optional<InteractionResult> onEntityInteract(Player player, Level level, InteractionHand hand, Entity entity) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof LassoItem item && entity instanceof Mob mob && entity.isAlive()) {
            if (!stack.hasTag() || !stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG)) {
                if (!item.hasStoredEntity(stack) && item.type.canPlayerPickUp(player, mob)) {
                    if (!player.level.isClientSide) {
                        entity.playSound(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.get());
                        if (entity.hasCustomName()) stack.setHoverName(entity.getCustomName());
                        CompoundTag compoundTag = item.storeEntity(entity);
                        entity.discard();
                        stack.addTagElement(TAG_STORED_ENTITY, compoundTag);
                        if (item.type.hasMaxHoldingTime()) {
                            stack.getTag().putLong(TAG_ENTITY_PICK_UP_TIME, level.getGameTime());
                        }
                    }
                    return Optional.of(InteractionResult.sidedSuccess(player.level.isClientSide));
                }
            }
        }
        return Optional.empty();
    }

    private static void finalizeLassoSpawn(Entity entity, Level level, BlockPos pos, boolean shouldOffsetY) {
        double offsetY;
        if (shouldOffsetY) {
            entity.setPos((double) pos.getX() + 0.5D, pos.getY() + 1, (double) pos.getZ() + 0.5D);
            offsetY = getYOffset(level, pos, entity.getBoundingBox());
        } else {
            offsetY = 0.0D;
        }

        entity.moveTo((double) pos.getX() + 0.5D, (double) pos.getY() + offsetY, (double) pos.getZ() + 0.5D, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
        if (entity instanceof Mob mob) {
            mob.yHeadRot = mob.getYRot();
            mob.yBodyRot = mob.getYRot();
            mob.playAmbientSound();
        }
    }

    protected static double getYOffset(LevelReader level, BlockPos pos, AABB box) {
        AABB aabb = new AABB(pos);
        Iterable<VoxelShape> iterable = level.getCollisions(null, aabb);
        return 1.0D + Shapes.collide(Direction.Axis.Y, box, iterable, -1.0D);
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

    private CompoundTag storeEntity(Entity entity) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString("id", this.getEncodeId(entity));
        entity.saveWithoutId(compoundtag);
        return compoundtag;
    }

    @Nullable
    protected final String getEncodeId(Entity entity) {
        EntityType<?> entityType = entity.getType();
        ResourceLocation resourceLocation = EntityType.getKey(entityType);
        return entityType.canSerialize() && resourceLocation != null ? resourceLocation.toString() : null;
    }

    private boolean releaseEntity(CompoundTag tag, Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !tag.isEmpty()) {
            if (((ServerLevel) level).getEntity(tag.getUUID(Entity.UUID_TAG)) != null) {
                // causes an issue with duplicate uuids when the lasso stack is copied in creative mode,
                // but we should not always remove the uuid as we rely on it for the villager contract
                tag.remove(Entity.UUID_TAG);
            }
            return EntityType.create(tag, level).map((entity) -> {
                finalizeLassoSpawn(entity, level, pos, true);
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

    public InteractionResult useOn(UseOnContext context) {
        if (!this.hasStoredEntity(context.getItemInHand())) return InteractionResult.PASS;
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemStack = context.getItemInHand();
            BlockPos blockPos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockState = level.getBlockState(blockPos);

            BlockPos blockPos2;
            if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
                blockPos2 = blockPos;
            } else {
                blockPos2 = blockPos.relative(direction);
            }

            this.releaseOn(context.getPlayer(), level, itemStack, blockPos, blockPos2);

            return InteractionResult.CONSUME;
        }
    }

    private void releaseOn(Entity entity, Level level, ItemStack itemStack, BlockPos sourcePos, BlockPos releasePos) {
        CompoundTag tag = itemStack.getTagElement(TAG_STORED_ENTITY);

        if (this.releaseEntity(tag, level, releasePos, itemStack)) {
            level.gameEvent(entity, GameEvent.ENTITY_PLACE, sourcePos);
        }

        itemStack.removeTagKey(TAG_STORED_ENTITY);

        if (this.type.hasMaxHoldingTime()) {
            long pickUpTime = itemStack.getTag().getLong(TAG_ENTITY_PICK_UP_TIME);
            itemStack.getTag().remove(TAG_ENTITY_PICK_UP_TIME);
            long passedPickUpTime = level.getGameTime() - pickUpTime;
            itemStack.getTag().putLong(TAG_ENTITY_RELEASE_TIME, level.getGameTime() + Math.min(0, passedPickUpTime - this.getMaxHoldingTime(itemStack)) / 5);
        }

        itemStack.resetHoverName();

        if (itemStack.getTag().isEmpty()) {
            itemStack.setTag(null);
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
                long pickUpTime = stack.getTag().getLong(TAG_ENTITY_PICK_UP_TIME);
                long diff = level.getGameTime() - pickUpTime;
                int maxHoldingTime = this.getMaxHoldingTime(stack);
                if (diff >= maxHoldingTime) {
                    this.releaseOn(entity, level, stack, entity.blockPosition(), entity.blockPosition());
                }
            }
            if (stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG)) {
                long releaseTime = stack.getTag().getLong(TAG_ENTITY_RELEASE_TIME);
                long diff = level.getGameTime() - releaseTime;
                int maxHoldingTime = this.getMaxHoldingTime(stack) / 5;
                if (diff >= maxHoldingTime) {
                    stack.getTag().remove(TAG_ENTITY_RELEASE_TIME);
                    if (stack.getTag().isEmpty()) {
                        stack.setTag(null);
                    }
                }
            }
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.type.hasMaxHoldingTime() && stack.hasTag() && (stack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG) || stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG));
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG)) {
            long pickUpTime = stack.getTag().getLong(TAG_ENTITY_PICK_UP_TIME);
            long currentHoldingTime = Proxy.INSTANCE.getClientLevel().getGameTime() - pickUpTime;
            int maxHoldingTime = this.getMaxHoldingTime(stack);
            currentHoldingTime = Math.min(currentHoldingTime, maxHoldingTime);
            return Math.round(13.0F - currentHoldingTime * 13.0F / maxHoldingTime);
        }
        if (stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG)) {
            long releaseTime = stack.getTag().getLong(TAG_ENTITY_RELEASE_TIME);
            long currentHoldingTime = Proxy.INSTANCE.getClientLevel().getGameTime() - releaseTime;
            int maxHoldingTime = this.getMaxHoldingTime(stack) / 5;
            currentHoldingTime = Math.min(currentHoldingTime, maxHoldingTime);
            return Math.round(13.0F - (maxHoldingTime - currentHoldingTime) * 13.0F / maxHoldingTime);
        }
        return 0;
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
        if (isAdvanced.isAdvanced() && stack.hasTag()) {
            if (stack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG)) {
                long pickUpTime = stack.getTag().getLong(TAG_ENTITY_PICK_UP_TIME);
                long currentHoldingTime = Proxy.INSTANCE.getClientLevel().getGameTime() - pickUpTime;
                int maxHoldingTime = this.getMaxHoldingTime(stack);
                currentHoldingTime = Math.min(currentHoldingTime, maxHoldingTime);
                tooltipComponents.add(Component.translatable("item.moblassos.lasso.remaining", (maxHoldingTime - currentHoldingTime) / 20).withStyle(ChatFormatting.GRAY));
            }
            if (stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG)) {
                long releaseTime = stack.getTag().getLong(TAG_ENTITY_RELEASE_TIME);
                long currentHoldingTime = Proxy.INSTANCE.getClientLevel().getGameTime() - releaseTime;
                int maxHoldingTime = this.getMaxHoldingTime(stack) / 5;
                currentHoldingTime = Math.min(currentHoldingTime, maxHoldingTime);
                tooltipComponents.add(Component.translatable("item.moblassos.lasso.remaining", (maxHoldingTime - currentHoldingTime) / 20).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        if (this.hasStoredEntity(itemEntity.getItem())) {
            this.releaseEntity(itemEntity.getItem().getTagElement(TAG_STORED_ENTITY), itemEntity.level, itemEntity.blockPosition(), itemEntity.getItem());
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

    public int getMaxHoldingTime(ItemStack stack) {
        int time = this.type.getMaxHoldingTime();
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.HOLDING_ENCHANTMENT.get(), stack);
        if (level > 0) time += time * level * 0.2F;
        return time;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
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
            if (CommonAbstractions.INSTANCE.isBossMob(mob)) return false;
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
