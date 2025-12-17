package fuzs.moblassos.client;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.client.color.item.Lasso;
import fuzs.moblassos.client.renderer.item.properties.conditional.LassoFilled;
import fuzs.moblassos.config.ServerConfig;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.world.item.ContractItem;
import fuzs.moblassos.world.item.LassoItem;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelsContext;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.ItemTooltipRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MobLassosClient implements ClientModConstructor {

    @Override
    public void onClientSetup() {
        ItemTooltipRegistry.ITEM.registerItemTooltip(ContractItem.class, ContractItem::getDescriptionComponent);
        ItemTooltipRegistry.ITEM.registerItemTooltip(LassoItem.class,
                (ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, @Nullable Player player, Consumer<Component> tooltipLineConsumer) -> {
                    LassoItem item = (LassoItem) itemStack.getItem();
                    if (item.hasOccupant(itemStack)) {
                        MutableComponent component = Component.translatable("gui.entity_tooltip.type",
                                item.getStoredEntityType(itemStack).getDescription());
                        tooltipLineConsumer.accept(component.withStyle(ChatFormatting.BLUE));
                    } else {
                        tooltipLineConsumer.accept(Component.translatable(item.getDescriptionId() + ".desc")
                                .withStyle(ChatFormatting.GOLD));
                    }
                    if (player != null && tooltipFlag.isAdvanced() && MobLassos.CONFIG.getHolder(ServerConfig.class)
                            .isAvailable()) {
                        boolean hasPickUpTime = itemStack.has(ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value());
                        boolean hasReleaseTime = itemStack.has(ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value());
                        if (hasPickUpTime || hasReleaseTime) {
                            int maxHoldingTime = item.getMaxHoldingTime(tooltipContext.registries(), itemStack);
                            long currentHoldingTime = 0;
                            if (hasPickUpTime) {
                                currentHoldingTime = item.getCurrentHoldingTime(player.level(),
                                        itemStack,
                                        ModRegistry.ENTITY_PICK_UP_TIME_DATA_COMPONENT_TYPE.value(),
                                        maxHoldingTime);
                            }
                            if (hasReleaseTime) {
                                maxHoldingTime /= 5;
                                currentHoldingTime = item.getCurrentHoldingTime(player.level(),
                                        itemStack,
                                        ModRegistry.ENTITY_RELEASE_TIME_DATA_COMPONENT_TYPE.value(),
                                        maxHoldingTime);
                            }
                            tooltipLineConsumer.accept(Component.translatable(LassoItem.KEY_REMAINING_TIME_IN_SECONDS,
                                    (maxHoldingTime - currentHoldingTime) / 20).withStyle(ChatFormatting.GRAY));
                        }
                    }
                });
    }

    @Override
    public void onRegisterItemModels(ItemModelsContext context) {
        context.registerConditionalItemModelProperty(MobLassos.id("lasso/filled"), LassoFilled.MAP_CODEC);
        context.registerItemTintSource(MobLassos.id("lasso"), Lasso.MAP_CODEC);
    }
}
