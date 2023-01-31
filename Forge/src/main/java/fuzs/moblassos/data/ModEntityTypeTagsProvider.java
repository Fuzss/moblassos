package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public ModEntityTypeTagsProvider(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
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
