package fuzs.moblassos.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class LassoItem extends Item {
    public static final String TAG_STORED_ENTITY = "StoredEntity";
    public static final String TAG_ENTITY_PICK_UP_TIME = "EntityPickUpTime";
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    private final Type type;

    public LassoItem(Properties properties, Type type) {
        super(properties);
        this.type = type;
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (this.hasStoredEntity(stack)) return InteractionResult.PASS;
        if (this.type.mobFilter.test(interactionTarget)) {
            if (!player.level.isClientSide) {
                CompoundTag compoundTag = this.storeEntity(interactionTarget);
                interactionTarget.discard();
                stack.addTagElement(TAG_STORED_ENTITY, compoundTag);
            }
            return InteractionResult.sidedSuccess(player.level.isClientSide);
        }
        return InteractionResult.PASS;
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

    private void respawnEntity(CompoundTag tag, Level level, BlockPos pos) {
        if (!level.isClientSide && !tag.isEmpty()) {
            EntityType.create(tag, level).ifPresent((entity) -> {
                entity.setPos(pos.getX(), pos.getY(), pos.getZ());
                ((ServerLevel) level).addWithUUID(entity);
            });
        }

    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof GrowingPlantHeadBlock growingPlantHeadBlock) {
            if (!growingPlantHeadBlock.isMaxAge(blockState)) {
                Player player = context.getPlayer();
                ItemStack itemStack = context.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockPos, itemStack);
                }

                level.playSound(player, blockPos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlockAndUpdate(blockPos, growingPlantHeadBlock.getMaxAgeState(blockState));
                if (player != null) {
                    itemStack.hurtAndBreak(1, player, (playerx) -> {
                        playerx.broadcastBreakEvent(context.getHand());
                    });
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.useOn(context);
    }

//    public boolean isBarVisible(ItemStack stack) {
//        return getContentWeight(stack) > 0;
//    }
//
//    public int getBarWidth(ItemStack stack) {
//        return Math.min(1 + 12 * getContentWeight(stack) / 64, 13);
//    }
//
//    public int getBarColor(ItemStack stack) {
//        return BAR_COLOR;
//    }
//
//    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
//        tooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", new Object[]{getContentWeight(stack), 64}).withStyle(ChatFormatting.GRAY));
//    }
//
//    public void onDestroyed(ItemEntity itemEntity) {
//        ItemUtils.onContainerDestroyed(itemEntity, getContents(itemEntity.getItem()));
//    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    public enum Type {
        GOLDEN(entity -> entity instanceof Animal);

        public final Predicate<LivingEntity> mobFilter;

        Type(Predicate<LivingEntity> mobFilter) {
            this.mobFilter = mobFilter;
        }
    }
}
