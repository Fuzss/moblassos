package fuzs.moblassos.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.Tags;

import java.util.function.Predicate;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public EnchantmentCategory createEnchantmentCategory(String internalName, Predicate<Item> canApplyTo) {
        return EnchantmentCategory.create(internalName, canApplyTo);
    }

    @Override
    public boolean isBossMob(Entity entity) {
        return entity.getType().is(Tags.EntityTypes.BOSSES);
    }
}
