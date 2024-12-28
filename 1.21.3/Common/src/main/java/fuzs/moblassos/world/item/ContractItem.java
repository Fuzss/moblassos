package fuzs.moblassos.world.item;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.network.ClientboundVillagerParticlesMessage;
import fuzs.puzzleslib.api.core.v1.Proxy;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ContractItem extends Item {

    public ContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.addAll(Proxy.INSTANCE.splitTooltipLines(this.getDescriptionComponent()));
    }

    public Component getDescriptionComponent() {
        return Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY);
    }

    public static EventResultHolder<InteractionResult> onEntityInteract(Player player, Level level, InteractionHand hand, Entity entity) {

        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.is(ModRegistry.CONTRACT_ITEM.value()) && entity instanceof AbstractVillager abstractVillager &&
                abstractVillager.isAlive()) {

            if (canAcceptContract(abstractVillager)) {

                if (!ModRegistry.VILLAGER_CONTRACT_ATTACHMENT_TYPE.has(abstractVillager)) {

                    if (!level.isClientSide) {

                        ModRegistry.VILLAGER_CONTRACT_ATTACHMENT_TYPE.set(abstractVillager, Unit.INSTANCE);

                        if (!player.getAbilities().instabuild) {

                            itemInHand.shrink(1);
                        }
                    }

                    onUseContract(level, player, abstractVillager, itemInHand, true);
                } else {

                    return EventResultHolder.pass();
                }
            } else {

                if (!level.isClientSide) {

                    abstractVillager.setUnhappyCounter(40);
                    abstractVillager.playSound(SoundEvents.VILLAGER_NO, 1.0F, abstractVillager.getVoicePitch());
                }

                onUseContract(level, player, abstractVillager, ItemStack.EMPTY, false);
            }
        } else {

            return EventResultHolder.pass();
        }

        return EventResultHolder.interrupt(InteractionResult.sidedSuccess(level.isClientSide));
    }

    private static void onUseContract(Level level, Player player, AbstractVillager abstractVillager, ItemStack itemInHand, boolean happyParticles) {
        if (!level.isClientSide) {
            // must just not be empty for yes sound to play, so any stack is ok basically
            // just an empty stack for no sound to play
            abstractVillager.notifyTradeUpdated(itemInHand);
            MobLassos.NETWORK.sendToAllTracking(abstractVillager,
                    new ClientboundVillagerParticlesMessage(abstractVillager.getId(), happyParticles), false
            );
        }
        Component displayName = getVillagerDisplayName(abstractVillager);
        player.displayClientMessage(Component.translatable(
                ModRegistry.CONTRACT_ITEM.value().getDescriptionId() + "." + (happyParticles ? "accept" : "reject"),
                displayName
        ).withStyle(happyParticles ? ChatFormatting.GREEN : ChatFormatting.RED), true);
    }

    private static Component getVillagerDisplayName(AbstractVillager abstractVillager) {
        if (abstractVillager instanceof Villager villager) {
            // include villager level as a hint that it's relevant for accepting a contract
            Component merchantLevel = Component.translatable("merchant.level." + villager.getVillagerData().getLevel());
            return Component.empty()
                    .append(abstractVillager.getDisplayName())
                    .append(" (")
                    .append(merchantLevel)
                    .append(")");
        } else {
            return abstractVillager.getDisplayName();
        }
    }

    public static boolean canAcceptContract(AbstractVillager abstractVillager) {
        if (abstractVillager instanceof Villager villager) {
            return Math.abs(villager.getUUID().getLeastSignificantBits() % VillagerData.MAX_VILLAGER_LEVEL) <
                    villager.getVillagerData().getLevel();
        } else {
            return true;
        }
    }
}
