package me.moutarde.realisticinventory.mixin.Forging;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.screen.PlayerScreenHandler.*;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin {
    @Shadow @Final protected PlayerEntity player;

    @Inject(method = "addPlayerInventorySlots", at = @At(value = "HEAD"), cancellable = true)
    public void onAddPlayerInventorySlots(PlayerInventory playerInventory, CallbackInfo ci) {
        int i;
        for(i = 0; i < playerInventory.player.realistic_inventory$getInventorySlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, playerInventory.player.realistic_inventory$getHotbarSlots() + i, 8 + (i%9) * 18, 84 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < playerInventory.player.realistic_inventory$getHotbarSlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, offset + 8 + i * 18, 142));
        }

        ci.cancel();
    }

    @ModifyConstant(method = "getPlayerInventoryEndIndex", constant = @Constant(intValue = 27))
    public int injected(int value) {
        return this.player.realistic_inventory$getInventorySlots();
    }

    @ModifyConstant(method = "getPlayerHotbarEndIndex", constant = @Constant(intValue = 9))
    public int injected2(int value) {
        return this.player.realistic_inventory$getHotbarSlots();
    }
}
