package fuzs.moblassos.data.tags;

import fuzs.moblassos.world.item.LassoType;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTagProvider extends AbstractTagProvider<EntityType<?>> {

    public ModEntityTypeTagProvider(DataProviderContext context) {
        super(Registries.ENTITY_TYPE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        for (LassoType lassoType : LassoType.values()) {
            this.add(lassoType.getEntityTypeTagKey()).addOptionalTag("c:capturing_not_supported");
        }
    }
}
