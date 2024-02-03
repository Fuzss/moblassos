package fuzs.moblassos.neoforge.data;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.neoforge.api.data.v2.client.AbstractSoundDefinitionProvider;
import fuzs.puzzleslib.neoforge.api.data.v2.core.ForgeDataProviderContext;
import net.minecraft.sounds.SoundEvents;

public class ModSoundDefinitionsProvider extends AbstractSoundDefinitionProvider {

    public ModSoundDefinitionsProvider(ForgeDataProviderContext context) {
        super(context);
    }

    @Override
    public void addSoundDefinitions() {
        this.add(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.value(), sound(SoundEvents.BEEHIVE_ENTER));
        this.add(ModRegistry.LASSO_RELEASE_SOUND_EVENT.value(), sound(SoundEvents.BEEHIVE_EXIT));
    }
}
