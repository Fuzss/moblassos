package fuzs.moblassos.world.item;

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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
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
        if (stack.getItem() instanceof LassoItem item && entity.isAlive()) {
            if (!stack.hasTag() || !stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG)) {
                if (!item.hasStoredEntity(stack) && item.type.mobFilter.test(entity)) {
                    if (!player.level.isClientSide) {
                        entity.playSound(SoundEvents.BUCKET_FILL, 1.0F, 1.0F);
                        CompoundTag compoundTag = item.storeEntity(entity);
                        entity.discard();
                        stack.addTagElement(TAG_STORED_ENTITY, compoundTag);
                        stack.getTag().putLong(TAG_ENTITY_PICK_UP_TIME, level.getGameTime());
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

    private boolean releaseEntity(CompoundTag tag, Level level, BlockPos pos) {
        if (!level.isClientSide && !tag.isEmpty()) {
            return EntityType.create(tag, level).map((entity) -> {
                finalizeLassoSpawn(entity, level, pos, true);
                entity.setDeltaMovement(Vec3.ZERO);
                ((ServerLevel) level).addWithUUID(entity);
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

            releaseOn(context.getPlayer(), level, itemStack, blockPos, blockPos2);

            return InteractionResult.CONSUME;
        }
    }

    private void releaseOn(Entity entity, Level level, ItemStack itemStack, BlockPos sourcePos, BlockPos releasePos) {
        CompoundTag tag = itemStack.getTagElement(TAG_STORED_ENTITY);

        if (this.releaseEntity(tag, level, releasePos)) {
            level.gameEvent(entity, GameEvent.ENTITY_PLACE, sourcePos);
        }

        itemStack.removeTagKey(TAG_STORED_ENTITY);
        itemStack.getTag().remove(TAG_ENTITY_PICK_UP_TIME);
        itemStack.getTag().putLong(TAG_ENTITY_RELEASE_TIME, level.getGameTime());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (stack.hasTag()) {
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
                int maxHoldingTime = this.getMaxHoldingTime(stack);
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
        return stack.hasTag() && (stack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG) || stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG));
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.getTag().contains(TAG_ENTITY_PICK_UP_TIME, Tag.TAG_LONG)) {
            long pickUpTime = stack.getTag().getLong(TAG_ENTITY_PICK_UP_TIME);
            long currentHoldingTime = Proxy.INSTANCE.getClientLevel().getGameTime() - pickUpTime;
            int maxHoldingTime = this.getMaxHoldingTime(stack);
            return Math.round(13.0F - currentHoldingTime * 13.0F / maxHoldingTime);
        }
        if (stack.getTag().contains(TAG_ENTITY_RELEASE_TIME, Tag.TAG_LONG)) {
            long releaseTime = stack.getTag().getLong(TAG_ENTITY_RELEASE_TIME);
            long currentHoldingTime = Proxy.INSTANCE.getClientLevel().getGameTime() - releaseTime;
            int maxHoldingTime = this.getMaxHoldingTime(stack);
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
            tooltipComponents.add(component.withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        if (this.hasStoredEntity(itemEntity.getItem())) {
            this.releaseEntity(itemEntity.getItem().getTagElement(TAG_STORED_ENTITY), itemEntity.level, itemEntity.blockPosition());
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
        int time = this.type.holdingTime.getAsInt();
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.HOLDING_ENCHANTMENT.get(), stack);
        if (level > 0) time += time * level * 0.2F;
        return time;
    }

    public enum Type {
        GOLDEN(entity -> entity instanceof Animal && !(entity instanceof Enemy), () -> 2400);

        public final Predicate<Entity> mobFilter;
        public final IntSupplier holdingTime;

        Type(Predicate<Entity> mobFilter, IntSupplier holdingTime) {
            this.mobFilter = mobFilter;
            this.holdingTime = holdingTime;
        }
    }
}
