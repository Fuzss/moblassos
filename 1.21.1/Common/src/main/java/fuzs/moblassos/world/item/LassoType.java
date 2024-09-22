package fuzs.moblassos.world.item;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import fuzs.moblassos.MobLassos;
import fuzs.moblassos.config.ServerConfig;
import fuzs.moblassos.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public enum LassoType implements StringRepresentable {
    GOLDEN("golden", entity -> entity instanceof Animal || entity instanceof AmbientCreature,
            () -> MobLassos.CONFIG.get(ServerConfig.class).goldenLassoTime
    ),
    AQUA("aqua", entity -> entity instanceof WaterAnimal, () -> MobLassos.CONFIG.get(ServerConfig.class).aquaLassoTime),
    DIAMOND("diamond",
            entity -> entity instanceof Animal || entity instanceof AmbientCreature || entity instanceof WaterAnimal,
            () -> MobLassos.CONFIG.get(ServerConfig.class).diamondLassoTime
    ),
    EMERALD("emerald", entity -> entity instanceof AbstractVillager,
            () -> MobLassos.CONFIG.get(ServerConfig.class).emeraldLassoTime
    ) {
        @Override
        protected Either<MutableComponent, Unit> isValidMob(Mob mob) {
            Either<MutableComponent, Unit> result = super.isValidMob(mob);
            if (!MobLassos.CONFIG.get(ServerConfig.class).villagersRequireContract) {
                return result;
            } else if (result.left().isEmpty() && !ModRegistry.VILLAGER_CONTRACT_ATTACHMENT_TYPE.has(mob)) {
                return Either.left(Component.translatable(this.getFailureTranslationKey(), mob.getDisplayName()));
            } else {
                return result;
            }
        }
    },
    HOSTILE("hostile", entity -> entity instanceof Enemy,
            () -> MobLassos.CONFIG.get(ServerConfig.class).hostileLassoTime, true
    ) {
        @Override
        protected Either<MutableComponent, Unit> isValidMob(Mob mob) {
            Either<MutableComponent, Unit> result = super.isValidMob(mob);
            if (result.left().isEmpty()) {
                double hostileMobHealth = MobLassos.CONFIG.get(ServerConfig.class).hostileMobHealth;
                if (mob.getHealth() / mob.getMaxHealth() >= hostileMobHealth) {
                    MutableComponent component = Component.translatable(this.getFailureTranslationKey(),
                            mob.getDisplayName(), String.format("%.0f", hostileMobHealth * mob.getMaxHealth()),
                            String.format("%.0f", mob.getHealth())
                    );
                    return Either.left(component);
                }
            }
            return result;
        }
    },
    CREATIVE("creative", entity -> true, () -> MobLassos.CONFIG.get(ServerConfig.class).creativeLassoTime, true);

    private final String name;
    private final Predicate<Mob> filter;
    private final IntSupplier holdingTime;

    LassoType(String name, Predicate<Mob> filter, IntSupplier holdingTime) {
        this(name, filter, holdingTime, false);
    }

    LassoType(String name, Predicate<Mob> filter, IntSupplier holdingTime, boolean allowHostile) {
        this.name = name;
        this.filter = (Mob mob) -> {
            return filter.test(mob) && (allowHostile || !(mob instanceof Enemy));
        };
        this.holdingTime = holdingTime;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean hasMaxHoldingTime() {
        return this.holdingTime.getAsInt() != -1;
    }

    public int getMaxHoldingTime() {
        return this.holdingTime.getAsInt() * 20;
    }

    public TagKey<EntityType<?>> getEntityTypeTagKey() {
        return TagKey.create(Registries.ENTITY_TYPE, MobLassos.id("forbidden_in_" + this.name + "_lasso"));
    }

    public boolean canPlayerPickUp(Player player, Mob mob) {
        return this.isValidMob(mob).ifLeft(component -> {
            player.displayClientMessage(component.withStyle(ChatFormatting.RED), true);
        }).right().isPresent();
    }

    protected Either<MutableComponent, Unit> isValidMob(Mob mob) {
        if (!CommonAbstractions.INSTANCE.isBossMob(mob.getType())) {
            if (!mob.getType().is(this.getEntityTypeTagKey()) && this.filter.test(mob)) {
                return Either.right(Unit.INSTANCE);
            }
        }
        return Either.left(Component.translatable(GOLDEN.getFailureTranslationKey(), mob.getDisplayName()));
    }

    public String getFailureTranslationKey() {
        return "item." + MobLassos.MOD_ID + ".failure." + this.name;
    }
}
