package fuzs.moblassos.data.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.moblassos.world.item.LassoType;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(ModRegistry.CREATIVE_MODE_TAB.value(), MobLassos.MOD_NAME);
        builder.add(ModRegistry.GOLDEN_LASSO_ITEM.value(), "Golden Lasso");
        builder.add(ModRegistry.GOLDEN_LASSO_ITEM.value(), "desc", "Holds: Animals");
        builder.add(ModRegistry.AQUA_LASSO_ITEM.value(), "Aqua Lasso");
        builder.add(ModRegistry.AQUA_LASSO_ITEM.value(), "desc", "Holds: Water Animals");
        builder.add(ModRegistry.DIAMOND_LASSO_ITEM.value(), "Diamond Lasso");
        builder.add(ModRegistry.DIAMOND_LASSO_ITEM.value(), "desc", "Holds: Any Animals");
        builder.add(ModRegistry.EMERALD_LASSO_ITEM.value(), "Emerald Lasso");
        builder.add(ModRegistry.EMERALD_LASSO_ITEM.value(), "desc", "Holds: Villagers");
        builder.add(ModRegistry.HOSTILE_LASSO_ITEM.value(), "Hostile Lasso");
        builder.add(ModRegistry.HOSTILE_LASSO_ITEM.value(), "desc", "Holds: Monsters");
        builder.add(ModRegistry.CREATIVE_LASSO_ITEM.value(), "Creative Lasso");
        builder.add(ModRegistry.CREATIVE_LASSO_ITEM.value(), "desc", "Holds: Any");
        builder.add(ModRegistry.CONTRACT_ITEM.value(), "Contract");
        builder.add(ModRegistry.CONTRACT_ITEM.value(),
                "desc",
                "Allows picking-up villagers in an emerald lasso. Leveling up increases the chance to accept.");
        builder.add(ModRegistry.HOLDING_ENCHANTMENT, "Holding");
        builder.add(ModRegistry.HOLDING_ENCHANTMENT, "desc", "Increases the time a lasso is able to hold a mob.");
        builder.add(LassoType.GOLDEN.getFailureTranslationKey(), "%s cannot be captured");
        builder.add(LassoType.EMERALD.getFailureTranslationKey(), "%s must accept a contract first");
        builder.add(LassoType.HOSTILE.getFailureTranslationKey(), "%s must be below %s health (currently %s)");
        builder.add(ModRegistry.CONTRACT_ITEM.value().getDescriptionId() + ".accept", "%s accepts the contract");
        builder.add(ModRegistry.CONTRACT_ITEM.value().getDescriptionId() + ".reject", "%s rejects the contract");
        builder.add(LassoItem.KEY_REMAINING_TIME_IN_SECONDS, "Remaining: %ss");
        builder.add(ModRegistry.LASSO_PICK_UP_SOUND_EVENT.value(), "Mob is picked-up");
        builder.add(ModRegistry.LASSO_RELEASE_SOUND_EVENT.value(), "Mob is released");
    }
}
