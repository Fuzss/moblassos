package fuzs.moblassos.data.tags;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class ModItemTagProvider extends AbstractTagProvider<Item> {

    public ModItemTagProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.add(ModRegistry.LASSOS_ITEM_TAG).add(ModRegistry.GOLDEN_LASSO_ITEM.value(),
                ModRegistry.AQUA_LASSO_ITEM.value(), ModRegistry.DIAMOND_LASSO_ITEM.value(),
                ModRegistry.EMERALD_LASSO_ITEM.value(), ModRegistry.HOSTILE_LASSO_ITEM.value(),
                ModRegistry.CREATIVE_LASSO_ITEM.value()
        );
        this.add(ModRegistry.LASSO_ENCHANTABLE_ITEM_TAG).addTag(ModRegistry.LASSOS_ITEM_TAG);
    }
}
