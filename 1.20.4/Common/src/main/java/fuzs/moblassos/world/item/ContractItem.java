package fuzs.moblassos.world.item;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.network.ClientboundVillagerContractMessage;
import fuzs.moblassos.network.ClientboundVillagerParticlesMessage;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ContractItem extends Item {

    public ContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    public static EventResultHolder<InteractionResult> onEntityInteract(Player player, Level level, InteractionHand hand, Entity entity) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof ContractItem && entity instanceof AbstractVillager abstractVillager && entity.isAlive()) {
            Component displayName;
            if (abstractVillager instanceof Villager villager) {
                // include villager level as a hint that it's relevant for accepting a contract
                Component merchantLevel = Component.translatable("merchant.level." + villager.getVillagerData().getLevel());
                displayName = Component.empty().append(entity.getDisplayName()).append(" (").append(merchantLevel).append(")");
            } else {
                displayName = entity.getDisplayName();
            }
            if (VillagerContractCapability.canAcceptContract(abstractVillager)) {
                return ModRegistry.VILLAGER_CONTRACT_CAPABILITY.maybeGet(entity).filter(Predicate.not(VillagerContractCapability::hasAcceptedContract)).map(capability -> {
                    capability.acceptContract();
                    if (!level.isClientSide) {
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        MobLassos.NETWORKING.sendToAllTracking(entity, new ClientboundVillagerParticlesMessage(entity.getId(), true));
                    }
                    // must just not be empty for yes sound to play, so any stack is ok basically
                    abstractVillager.notifyTradeUpdated(stack);
                    player.displayClientMessage(Component.translatable(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".accept", displayName).withStyle(ChatFormatting.GREEN), true);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }).or(() -> Optional.of(InteractionResult.CONSUME_PARTIAL)).map(EventResultHolder::interrupt).orElseGet(EventResultHolder::pass);
            } else {
                if (!level.isClientSide) {
                    MobLassos.NETWORKING.sendToAllTracking(entity, new ClientboundVillagerParticlesMessage(entity.getId(), false));
                }
                setVillagerUnhappy(abstractVillager);
                // just an empty stack for no sound to play
                abstractVillager.notifyTradeUpdated(ItemStack.EMPTY);
                player.displayClientMessage(Component.translatable(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".reject", displayName).withStyle(ChatFormatting.RED), true);
                return EventResultHolder.interrupt(InteractionResult.sidedSuccess(level.isClientSide));
            }
        }
        return EventResultHolder.pass();
    }

    private static void setVillagerUnhappy(AbstractVillager villager) {
        villager.setUnhappyCounter(40);
        if (!villager.level().isClientSide()) {
            villager.playSound(SoundEvents.VILLAGER_NO, 1.0F, villager.getVoicePitch());
        }
    }

    public static EventResult onEntityJoinServerLevel(Entity entity, ServerLevel level, @Nullable MobSpawnType spawnType) {
        if (entity instanceof AbstractVillager && ModRegistry.VILLAGER_CONTRACT_CAPABILITY.maybeGet(entity).filter(VillagerContractCapability::hasAcceptedContract).isPresent()) {
            MobLassos.NETWORKING.sendToAllTracking(entity, new ClientboundVillagerContractMessage(entity.getId()));
        }
        return EventResult.PASS;
    }

    public static void addParticlesAroundVillager(@Nullable AbstractVillager villager, ParticleOptions particleOption) {
        if (villager != null) {
            for (int i = 0; i < 5; ++i) {
                double d0 = villager.getRandom().nextGaussian() * 0.02D;
                double d1 = villager.getRandom().nextGaussian() * 0.02D;
                double d2 = villager.getRandom().nextGaussian() * 0.02D;
                villager.level().addParticle(particleOption, villager.getRandomX(1.0D), villager.getRandomY() + 1.0D, villager.getRandomZ(1.0D), d0, d1, d2);
            }
        }
    }
}
