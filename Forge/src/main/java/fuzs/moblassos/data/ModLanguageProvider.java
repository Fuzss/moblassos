package fuzs.moblassos.data;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
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
        this.add(ModRegistry.AQUA_LASSO_ITEM.get(), "Aqua Lasso");
        this.add(ModRegistry.DIAMOND_LASSO_ITEM.get(), "Diamond Lasso");
        this.add(ModRegistry.EMERALD_LASSO_ITEM.get(), "Emerald Lasso");
        this.add(ModRegistry.HOSTILE_LASSO_ITEM.get(), "Hostile Lasso");
        this.add(ModRegistry.CREATIVE_LASSO_ITEM.get(), "Creative Lasso");
        this.add(ModRegistry.CONTRACT_ITEM.get(), "Contract");
        this.add(ModRegistry.HOLDING_ENCHANTMENT.get(), "Holding");
        this.desc(ModRegistry.HOLDING_ENCHANTMENT.get(), "Increases the time a lasso is able to hold a mob.");
        this.add(ModRegistry.HOSTILE_LASSO_ITEM.get().getDescriptionId() + ".hostile", "%s must be below %s health (currently %s).");
        this.add(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".accept", "%s accepts your contract!");
        this.add(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".reject", "%s rejects your contract.");
    }

    public void add(CreativeModeTab tab, String name) {
        this.add(((TranslatableContents) tab.getDisplayName().getContents()).getKey(), name);
    }

    public void desc(Enchantment enchantment, String name) {
        this.add(enchantment.getDescriptionId() + ".desc", name);
    }
}
