package fuzs.moblassos.data;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTranslations() {
        this.addCreativeModeTab(MobLassos.MOD_NAME);
        this.add(ModRegistry.GOLDEN_LASSO_ITEM.get(), "Golden Lasso");
        this.addAdditional(ModRegistry.GOLDEN_LASSO_ITEM.get(), "desc", "Holds: Animals");
        this.add(ModRegistry.AQUA_LASSO_ITEM.get(), "Aqua Lasso");
        this.addAdditional(ModRegistry.AQUA_LASSO_ITEM.get(), "desc", "Holds: Water Animals");
        this.add(ModRegistry.DIAMOND_LASSO_ITEM.get(), "Diamond Lasso");
        this.addAdditional(ModRegistry.DIAMOND_LASSO_ITEM.get(), "desc", "Holds: Any Animals");
        this.add(ModRegistry.EMERALD_LASSO_ITEM.get(), "Emerald Lasso");
        this.addAdditional(ModRegistry.EMERALD_LASSO_ITEM.get(), "desc", "Holds: Villagers");
        this.add(ModRegistry.HOSTILE_LASSO_ITEM.get(), "Hostile Lasso");
        this.addAdditional(ModRegistry.HOSTILE_LASSO_ITEM.get(), "desc", "Holds: Monsters");
        this.add(ModRegistry.CREATIVE_LASSO_ITEM.get(), "Creative Lasso");
        this.addAdditional(ModRegistry.CREATIVE_LASSO_ITEM.get(), "desc", "Holds: Any");
        this.add(ModRegistry.CONTRACT_ITEM.get(), "Contract");
        this.addAdditional(ModRegistry.CONTRACT_ITEM.get(), "desc", "Villagers must accept a contract before picking-up in an emerald lasso. Leveling up increases the chance to accept.");
        this.add(ModRegistry.HOLDING_ENCHANTMENT.get(), "Holding");
        this.addAdditional(ModRegistry.HOLDING_ENCHANTMENT.get(), "desc", "Increases the time a lasso is able to hold a mob.");
        this.add(ModRegistry.HOSTILE_LASSO_ITEM.get().getDescriptionId() + ".hostile", "%s must be below %s health (currently %s)");
        this.add(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".accept", "%s accepts the contract");
        this.add(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".reject", "%s rejects the contract");
        this.add("item.moblassos.lasso.remaining", "Remaining: %ss");
        this.add(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.get(), "Mob is picked-up");
        this.add(ModRegistry.LASSO_RELEASE_SOUND_EVENT.get(), "Mob is released");
    }
}
