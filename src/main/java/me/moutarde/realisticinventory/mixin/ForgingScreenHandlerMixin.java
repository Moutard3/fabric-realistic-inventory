package me.moutarde.realisticinventory.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static me.moutarde.realisticinventory.Realistic_inventory.INVENTORY_SIZE;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin {
    @Inject(method = "addPlayerInventorySlots", at = @At(value = "HEAD"), cancellable = true)
    public void onAddPlayerInventorySlots(PlayerInventory playerInventory, CallbackInfo ci) {
        int i;
        for(i = 0; i < INVENTORY_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, HOTBAR_SIZE + i, 8 + (i%9) * 18, 84 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < HOTBAR_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, offset + 8 + i * 18, 142));
        }

        ci.cancel();
    }

    @ModifyConstant(method = "getPlayerInventoryEndIndex", constant = @Constant(intValue = 27))
    public int injected(int value) {
        return INVENTORY_SIZE;
    }

    @ModifyConstant(method = "getPlayerHotbarEndIndex", constant = @Constant(intValue = 9))
    public int injected2(int value) {
        return HOTBAR_SIZE;
    }
}
