package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends AbstractTagProvider.EntityTypes {

    public ModEntityTypeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, ExistingFileHelper fileHelper) {
        super(packOutput, lookupProvider, modId, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.GOLDEN_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.AQUA_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.DIAMOND_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.EMERALD_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.HOSTILE_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.CREATIVE_LASSO_BLACKLIST_ENTITY_TYPE_TAG);
    }
}
