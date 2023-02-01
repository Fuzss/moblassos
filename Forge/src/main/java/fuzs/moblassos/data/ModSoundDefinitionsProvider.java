package fuzs.moblassos.data;

import fuzs.moblassos.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {
    private final String modId;

    public ModSoundDefinitionsProvider(DataGenerator dataGenerator, String modId, ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
        this.modId = modId;
    }

    protected static SoundDefinition.Sound sound(SoundEvent soundEvent) {
        return sound(soundEvent.getLocation(), SoundDefinition.SoundType.EVENT);
    }

    @Override
    public void registerSounds() {
        this.add(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.get(), sound(SoundEvents.BEEHIVE_ENTER));
        this.add(ModRegistry.LASSO_RELEASE_SOUND_EVENT.get(), sound(SoundEvents.BEEHIVE_EXIT));
    }

    protected void add(final SoundEvent soundEvent, final SoundDefinition.Sound... sounds) {
        this.add(soundEvent.getLocation(), definition().with(sounds));
    }

    @Override
    protected void add(final ResourceLocation soundEvent, final SoundDefinition definition) {
        super.add(soundEvent, definition.subtitle("subtitles." + soundEvent.getPath()));
    }

    protected ResourceLocation id(String path) {
        return new ResourceLocation(this.modId, path);
    }

    protected ResourceLocation vanilla(String path) {
        return new ResourceLocation(path);
    }
}
