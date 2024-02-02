package fuzs.moblassos.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.Predicate;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public EnchantmentCategory createEnchantmentCategory(String internalName, Predicate<Item> canApplyTo) {
        return EnchantmentCategory.create(internalName, canApplyTo);
    }
}
