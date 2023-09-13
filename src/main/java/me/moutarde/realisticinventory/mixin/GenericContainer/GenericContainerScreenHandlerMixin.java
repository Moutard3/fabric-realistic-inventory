package me.moutarde.realisticinventory.mixin.GenericContainer;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenericContainerScreenHandler.class)
public abstract class GenericContainerScreenHandlerMixin {
    @Redirect(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GenericContainerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"))
    public Slot onAddSlot(GenericContainerScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V",
            at = @At(value = "TAIL"))
    public void injected(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows, CallbackInfo ci) {
        int j;
        int k;
        int i = (rows - 4) * 18;
        for (j = 0; j < rows; ++j) {
            for (k = 0; k < 9; ++k) {
                ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(j = 0; j < playerInventory.player.realistic_inventory$getInventorySlots(); ++j) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, playerInventory.player.realistic_inventory$getHotbarSlots() + j, 8 + (j%9) * 18, 103 + (j/9) * 18 + i));
        }

        final int offset = 18 * 4;
        for (j = 0; j < playerInventory.player.realistic_inventory$getHotbarSlots(); ++j) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, j, 8 + j * 18 + offset, 161 + i));
        }
    }
}
