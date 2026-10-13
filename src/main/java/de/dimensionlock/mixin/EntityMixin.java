package de.dimensionlock.mixin;

import de.dimensionlock.DimensionLockState;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Blockiert Portal-Teleports in gesperrte Dimensionen (Spieler, Mobs, Items).
 * /tp und Befehle von Admins bleiben davon unberuehrt.
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "canTeleport", at = @At("HEAD"), cancellable = true)
    private void dimensionlock$blockLockedDimensions(Level from, Level to, CallbackInfoReturnable<Boolean> cir) {
        if (from == to) return;
        if (DimensionLockState.isLocked(to.dimension())) {
            if ((Object) this instanceof ServerPlayer player) {
                String name = to.dimension() == Level.NETHER ? "Der Nether" : "Das End";
                player.sendSystemMessage(Component.literal(name + " ist gesperrt."), true);
            }
            cir.setReturnValue(false);
        }
    }
}
