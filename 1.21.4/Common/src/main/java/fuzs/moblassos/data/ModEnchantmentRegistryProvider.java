package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractRegistriesDatapackGenerator;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentRegistryProvider extends AbstractRegistriesDatapackGenerator<Enchantment> {

    public ModEnchantmentRegistryProvider(DataProviderContext context) {
        super(Registries.ENCHANTMENT, context);
    }

    @Override
    public void addBootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        registerEnchantment(context, ModRegistry.HOLDING_ENCHANTMENT, Enchantment.enchantment(
                Enchantment.definition(items.getOrThrow(ModRegistry.LASSO_ENCHANTABLE_ITEM_TAG), 5, 3,
                        Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(55, 8), 2, EquipmentSlotGroup.MAINHAND
                )));
    }
}
