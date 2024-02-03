package fuzs.moblassos.data.tags;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;

public class ModItemTagProvider extends AbstractTagProvider.Items {

    public ModItemTagProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.LASSOS_ITEM_TAG)
                .add(ModRegistry.GOLDEN_LASSO_ITEM.value(),
                        ModRegistry.AQUA_LASSO_ITEM.value(),
                        ModRegistry.DIAMOND_LASSO_ITEM.value(),
                        ModRegistry.EMERALD_LASSO_ITEM.value(),
                        ModRegistry.HOSTILE_LASSO_ITEM.value(),
                        ModRegistry.CREATIVE_LASSO_ITEM.value()
                );
    }
}
