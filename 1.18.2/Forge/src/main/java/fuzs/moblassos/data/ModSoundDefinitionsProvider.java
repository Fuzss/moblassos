package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractSoundDefinitionProvider;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModSoundDefinitionsProvider extends AbstractSoundDefinitionProvider {

    public ModSoundDefinitionsProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    public void registerSounds() {
        this.add(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.get(), sound(SoundEvents.BEEHIVE_ENTER));
        this.add(ModRegistry.LASSO_RELEASE_SOUND_EVENT.get(), sound(SoundEvents.BEEHIVE_EXIT));
    }
}
