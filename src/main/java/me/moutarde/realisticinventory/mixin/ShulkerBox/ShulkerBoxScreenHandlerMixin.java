package me.moutarde.realisticinventory.mixin.ShulkerBox;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.screen.PlayerScreenHandler.*;

@Mixin(ShulkerBoxScreenHandler.class)
public class ShulkerBoxScreenHandlerMixin {
    @Redirect(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/ShulkerBoxScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/screen/ShulkerBoxScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                            ordinal = 1
                    ),
                    to = @At(value = "TAIL")
            )
    )
    public Slot addSlot(ShulkerBoxScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V", at = @At("TAIL"))
    public void injected(int syncId, PlayerInventory playerInventory, Inventory inventory, CallbackInfo ci) {
        int i;
        for(i = 0; i < playerInventory.player.realistic_inventory$getInventorySlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, playerInventory.player.realistic_inventory$getHotbarSlots() + i, 8 + (i%9) * 18, 84 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < playerInventory.player.realistic_inventory$getHotbarSlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, offset + 8 + i * 18, 142));
        }
    }
}
