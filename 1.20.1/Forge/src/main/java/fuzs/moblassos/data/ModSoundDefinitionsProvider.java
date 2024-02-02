package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractSoundDefinitionProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModSoundDefinitionsProvider extends AbstractSoundDefinitionProvider {

    public ModSoundDefinitionsProvider(PackOutput packOutput, String modId, ExistingFileHelper fileHelper) {
        super(packOutput, modId, fileHelper);
    }

    @Override
    public void registerSounds() {
        this.add(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.get(), sound(SoundEvents.BEEHIVE_ENTER));
        this.add(ModRegistry.LASSO_RELEASE_SOUND_EVENT.get(), sound(SoundEvents.BEEHIVE_EXIT));
    }
}
