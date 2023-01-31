package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper fileHelper) {
        super(dataGenerator, new BlockTagsProvider(dataGenerator, modId, fileHelper), modId, fileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModRegistry.LASSOS_ITEM_TAG).add(ModRegistry.GOLDEN_LASSO_ITEM.get(), ModRegistry.AQUA_LASSO_ITEM.get(), ModRegistry.DIAMOND_LASSO_ITEM.get(), ModRegistry.EMERALD_LASSO_ITEM.get(), ModRegistry.HOSTILE_LASSO_ITEM.get(), ModRegistry.CREATIVE_LASSO_ITEM.get());
    }
}
