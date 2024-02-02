package fuzs.moblassos.world.item.enchantment;

import fuzs.moblassos.init.ModRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class HoldingEnchantment extends Enchantment {

    public HoldingEnchantment(Enchantment.Rarity rarity, EquipmentSlot... applicableSlots) {
        super(rarity, ModRegistry.LASSO_ENCHANTMENT_CATEGORY, applicableSlots);
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 8;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return super.getMinCost(enchantmentLevel) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
