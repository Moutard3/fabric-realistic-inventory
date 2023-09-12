package me.moutarde.realisticinventory.mixin;

import me.moutarde.realisticinventory.items.BackpackItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Inject(method = "onClosed", at = @At("TAIL"))
    private void injected(PlayerEntity player, CallbackInfo ci)
    {
        if (player instanceof ServerPlayerEntity && player.realistic_inventory$hasBackpack()) {
            BackpackItem.saveBackpack(
                    player.getInventory().getStack(player.realistic_inventory$getHotbarSlots() + player.realistic_inventory$getInventorySlots()),
                    player.getInventory().main.subList(player.realistic_inventory$getHotbarSlots(), player.realistic_inventory$getHotbarSlots() + 9)
            );
        }
    }
}
