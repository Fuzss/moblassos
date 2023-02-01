package fuzs.moblassos.data;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId) {
        super(gen, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.CREATIVE_MODE_TAB, MobLassos.MOD_NAME);
        this.add(ModRegistry.GOLDEN_LASSO_ITEM.get(), "Golden Lasso");
        this.desc(ModRegistry.GOLDEN_LASSO_ITEM.get(), "Holds: Animals");
        this.add(ModRegistry.AQUA_LASSO_ITEM.get(), "Aqua Lasso");
        this.desc(ModRegistry.AQUA_LASSO_ITEM.get(), "Holds: Water Animals");
        this.add(ModRegistry.DIAMOND_LASSO_ITEM.get(), "Diamond Lasso");
        this.desc(ModRegistry.DIAMOND_LASSO_ITEM.get(), "Holds: Any Animals");
        this.add(ModRegistry.EMERALD_LASSO_ITEM.get(), "Emerald Lasso");
        this.desc(ModRegistry.EMERALD_LASSO_ITEM.get(), "Holds: Villagers\\nA villager must accept a contract before it can be picked-up. Most villagers will not accept any contract.");
        this.add(ModRegistry.HOSTILE_LASSO_ITEM.get(), "Hostile Lasso");
        this.desc(ModRegistry.HOSTILE_LASSO_ITEM.get(), "Holds: Monsters");
        this.add(ModRegistry.CREATIVE_LASSO_ITEM.get(), "Creative Lasso");
        this.desc(ModRegistry.CREATIVE_LASSO_ITEM.get(), "Holds: Any");
        this.add(ModRegistry.CONTRACT_ITEM.get(), "Contract");
        this.desc(ModRegistry.CONTRACT_ITEM.get(), "A villager must accept a contract before it can be picked-up in an emerald lasso. Most villagers will not accept any contract.");
        this.add(ModRegistry.HOLDING_ENCHANTMENT.get(), "Holding");
        this.desc(ModRegistry.HOLDING_ENCHANTMENT.get(), "Increases the time a lasso is able to hold a mob.");
        this.add(ModRegistry.HOSTILE_LASSO_ITEM.get().getDescriptionId() + ".hostile", "%s must be below %s health (currently %s)");
        this.add(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".accept", "%s accepts the contract");
        this.add(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".reject", "%s rejects the contract");
        this.add("item.moblassos.lasso.remaining", "Remaining: %ss");
        this.add(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.get(), "Mob is picked-up");
        this.add(ModRegistry.LASSO_RELEASE_SOUND_EVENT.get(), "Mob is released");
    }

    public void add(CreativeModeTab tab, String name) {
        this.add(((TranslatableContents) tab.getDisplayName().getContents()).getKey(), name);
    }

    public void desc(Item item, String name) {
        this.add(item.getDescriptionId() + ".desc", name);
    }

    public void desc(Enchantment enchantment, String name) {
        this.add(enchantment.getDescriptionId() + ".desc", name);
    }

    public void add(SoundEvent soundEvent, String name) {
        this.add("subtitles." + soundEvent.getLocation().getPath(), name);
    }
}
