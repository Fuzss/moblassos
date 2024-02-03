package fuzs.moblassos.data.tags;

import fuzs.moblassos.world.item.LassoType;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;

public class ModEntityTypeTagProvider extends AbstractTagProvider.EntityTypes {

    public ModEntityTypeTagProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        for (LassoType lassoType : LassoType.values()) {
            this.tag(lassoType.getEntityTypeTagKey());
        }
    }
}
