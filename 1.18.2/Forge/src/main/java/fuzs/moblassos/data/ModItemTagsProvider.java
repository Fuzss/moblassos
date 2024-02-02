package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractTagProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModItemTagsProvider extends AbstractTagProvider.Items {

    public ModItemTagsProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTags() {
        this.tag(ModRegistry.LASSOS_ITEM_TAG).add(ModRegistry.GOLDEN_LASSO_ITEM.get(), ModRegistry.AQUA_LASSO_ITEM.get(), ModRegistry.DIAMOND_LASSO_ITEM.get(), ModRegistry.EMERALD_LASSO_ITEM.get(), ModRegistry.HOSTILE_LASSO_ITEM.get(), ModRegistry.CREATIVE_LASSO_ITEM.get());
    }
}
