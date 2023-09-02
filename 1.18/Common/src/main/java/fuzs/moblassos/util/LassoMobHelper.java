package fuzs.moblassos.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LassoMobHelper {

    public static CompoundTag saveEntity(Entity entity) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString("id", EntityType.getKey(entity.getType()).toString());
        entity.saveWithoutId(compoundtag);
        return compoundtag;
    }

    public static void moveEntityTo(Entity entity, Level level, BlockPos pos, boolean shouldOffsetY) {
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

    private static double getYOffset(LevelReader level, BlockPos pos, AABB box) {
        AABB aabb = new AABB(pos);
        Iterable<VoxelShape> iterable = level.getCollisions(null, aabb);
        return 1.0D + Shapes.collide(Direction.Axis.Y, box, iterable, -1.0D);
    }
}
