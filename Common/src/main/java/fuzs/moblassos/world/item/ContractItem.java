package fuzs.moblassos.world.item;

import fuzs.moblassos.MobLassos;
import fuzs.moblassos.capability.VillagerContractCapability;
import fuzs.moblassos.init.ModRegistry;
import fuzs.moblassos.networking.ClientboundVillagerContractMessage;
import fuzs.moblassos.networking.ClientboundVillagerParticlesMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Predicate;

public class ContractItem extends Item {

    public ContractItem(Properties properties) {
        super(properties);
    }

    public static Optional<InteractionResult> onEntityInteract(Player player, Level level, InteractionHand hand, Entity entity) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof ContractItem item && entity instanceof AbstractVillager villager && entity.isAlive()) {
            if (VillagerContractCapability.canAcceptContract(entity)) {
                return ModRegistry.VILLAGER_CONTRACT_CAPABILITY.maybeGet(entity).filter(Predicate.not(VillagerContractCapability::hasAcceptedContract)).map(capability -> {
                    capability.acceptContract();
                    if (!level.isClientSide) {
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        MobLassos.NETWORKING.sendToAllTracking(new ClientboundVillagerParticlesMessage(entity.getId(), true), entity);
                    }
                    // must just not be empty for yes sound to play, so any stack is ok basically
                    villager.notifyTradeUpdated(stack);
                    player.displayClientMessage(Component.translatable(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".accept", entity.getDisplayName()).withStyle(ChatFormatting.GREEN), true);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                });
            } else {
                if (!level.isClientSide) {
                    MobLassos.NETWORKING.sendToAllTracking(new ClientboundVillagerParticlesMessage(entity.getId(), false), entity);
                }
                setVillagerUnhappy(villager);
                // just an empty stack for no sound to play
                villager.notifyTradeUpdated(ItemStack.EMPTY);
                player.displayClientMessage(Component.translatable(ModRegistry.CONTRACT_ITEM.get().getDescriptionId() + ".reject", entity.getDisplayName()).withStyle(ChatFormatting.RED), true);
            }
            return Optional.of(InteractionResult.CONSUME_PARTIAL);
        }
        return Optional.empty();
    }

    private static void setVillagerUnhappy(AbstractVillager villager) {
        villager.setUnhappyCounter(40);
        if (!villager.level.isClientSide()) {
            villager.playSound(SoundEvents.VILLAGER_NO, 1.0F, villager.getVoicePitch());
        }
    }

    public static void onEntityJoinServerLevel(Entity entity, ServerLevel level) {
        if (entity instanceof AbstractVillager && ModRegistry.VILLAGER_CONTRACT_CAPABILITY.maybeGet(entity).filter(VillagerContractCapability::hasAcceptedContract).isPresent()) {
            MobLassos.NETWORKING.sendToAllTracking(new ClientboundVillagerContractMessage(entity.getId()), entity);
        }
    }

    public static void addParticlesAroundVillager(AbstractVillager villager, ParticleOptions particleOption) {
        for (int i = 0; i < 5; ++i) {
            double d0 = villager.getRandom().nextGaussian() * 0.02D;
            double d1 = villager.getRandom().nextGaussian() * 0.02D;
            double d2 = villager.getRandom().nextGaussian() * 0.02D;
            villager.level.addParticle(particleOption, villager.getRandomX(1.0D), villager.getRandomY() + 1.0D, villager.getRandomZ(1.0D), d0, d1, d2);
        }
    }
}
