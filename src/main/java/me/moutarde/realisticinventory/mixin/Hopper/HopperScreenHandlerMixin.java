package me.moutarde.realisticinventory.mixin.Hopper;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static me.moutarde.realisticinventory.Realistic_inventory.INVENTORY_SIZE;

@Mixin(HopperScreenHandler.class)
public class HopperScreenHandlerMixin {
    @Redirect(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/HopperScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/screen/HopperScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                            ordinal = 1
                    ),
                    to = @At(value = "TAIL")
            )
    )
    public Slot addSlot(HopperScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V", at = @At("TAIL"))
    public void injected(int syncId, PlayerInventory playerInventory, Inventory inventory, CallbackInfo ci) {
        int i;
        for(i = 0; i < INVENTORY_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, HOTBAR_SIZE + i, 8 + (i%9) * 18, 51 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < HOTBAR_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, offset + 8 + i * 18, 109));
        }
    }
}
