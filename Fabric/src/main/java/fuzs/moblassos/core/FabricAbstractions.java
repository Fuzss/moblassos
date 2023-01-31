package fuzs.moblassos.core;

import fuzs.extensibleenums.core.EnumFactories;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.Predicate;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public EnchantmentCategory createEnchantmentCategory(String internalName, Predicate<Item> canApplyTo) {
        return EnumFactories.createEnchantmentCategory(internalName, canApplyTo);
    }

    @Override
    public boolean isBossMob(Entity entity) {
        return entity.getType().is(ConventionalEntityTypeTags.BOSSES);
    }
}
