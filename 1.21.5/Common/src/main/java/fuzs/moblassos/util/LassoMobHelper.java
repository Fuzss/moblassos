package fuzs.moblassos.util;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class LassoMobHelper {
    private static final List<String> TAGS_TO_REMOVE = List.of("puzzleslib:spawn_type",
            "forge:spawn_type",
            "neoforge:spawn_type",
            "SleepingX",
            "SleepingY",
            "SleepingZ");

    public static CompoundTag saveEntity(Entity entity) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("id", EntityType.getKey(entity.getType()).toString());
        entity.saveWithoutId(compoundTag);
        return compoundTag;
    }

    public static void moveEntityTo(Entity entity, Level level, BlockPos pos, boolean shouldOffsetY) {
        double offsetY;
        if (shouldOffsetY) {
            entity.setPos((double) pos.getX() + 0.5D, pos.getY() + 1, (double) pos.getZ() + 0.5D);
            offsetY = getYOffset(level, pos, entity.getBoundingBox());
        } else {
            offsetY = 0.0D;
        }

        entity.snapTo((double) pos.getX() + 0.5D,
                (double) pos.getY() + offsetY,
                (double) pos.getZ() + 0.5D,
                Mth.wrapDegrees(level.random.nextFloat() * 360.0F),
                0.0F);
        if (entity instanceof Mob mob) {
            mob.yHeadRot = mob.getYRot();
            mob.yBodyRot = mob.getYRot();
            mob.playAmbientSound();
        }
    }

    private static double getYOffset(LevelReader level, BlockPos pos, AABB box) {
        AABB aabb = new AABB(pos);
        Iterable<VoxelShape> iterable = level.getCollisions(null, aabb);
        return 1.0D + Shapes.collide(Direction.Axis.Y, box, iterable, -1.0D);
    }

    public static void removeTagKeys(ServerLevel level, CompoundTag compoundTag) {
        TAGS_TO_REMOVE.forEach(compoundTag::remove);
        if (level.getEntity(compoundTag.read(Entity.UUID_TAG, UUIDUtil.CODEC).orElse(Util.NIL_UUID)) != null) {
            // causes an issue with duplicate uuids when the lasso stack is copied in creative mode,
            // but we should not always remove the uuid as we rely on it for the villager contract
            compoundTag.remove(Entity.UUID_TAG);
        }
    }
}
