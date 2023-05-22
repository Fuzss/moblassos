package fuzs.moblassos.core;

import fuzs.extensibleenums.core.EnumFactories;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.Predicate;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public EnchantmentCategory createEnchantmentCategory(String internalName, Predicate<Item> canApplyTo) {
        return EnumFactories.createEnchantmentCategory(internalName, canApplyTo);
    }
}
