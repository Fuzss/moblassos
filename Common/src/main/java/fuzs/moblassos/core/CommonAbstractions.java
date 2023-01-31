package fuzs.moblassos.core;

import fuzs.puzzleslib.util.PuzzlesUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.Predicate;

public interface CommonAbstractions {
    CommonAbstractions INSTANCE = PuzzlesUtil.loadServiceProvider(CommonAbstractions.class);

    EnchantmentCategory createEnchantmentCategory(String internalName, Predicate<Item> canApplyTo);

    boolean isBossMob(Entity entity);
}
