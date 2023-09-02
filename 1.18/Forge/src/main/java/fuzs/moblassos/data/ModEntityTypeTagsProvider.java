package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractTagProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModEntityTypeTagsProvider extends AbstractTagProvider.EntityTypes {

    public ModEntityTypeTagsProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTags() {
        this.tag(ModRegistry.GOLDEN_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.AQUA_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.DIAMOND_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.EMERALD_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.HOSTILE_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.CREATIVE_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
    }
}
