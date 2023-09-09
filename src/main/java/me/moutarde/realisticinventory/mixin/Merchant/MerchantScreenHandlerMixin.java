package me.moutarde.realisticinventory.mixin.Merchant;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.village.Merchant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.screen.PlayerScreenHandler.*;

@Mixin(MerchantScreenHandler.class)
public class MerchantScreenHandlerMixin {
    @Redirect(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/village/Merchant;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/MerchantScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/screen/MerchantScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                            ordinal = 3
                    ),
                    to = @At(value = "TAIL")
            )
    )
    public Slot onAddSlot(MerchantScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/village/Merchant;)V", at = @At(value = "TAIL"))
    public void injected(int syncId, PlayerInventory playerInventory, Merchant merchant, CallbackInfo ci) {
        int i;
        for(i = 0; i < INVENTORY_END - INVENTORY_START; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, HOTBAR_END - HOTBAR_START + i, 108 + (i%9) * 18, 84 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < HOTBAR_END - HOTBAR_START; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, offset + 108 + i * 18, 142));
        }
    }

    @ModifyConstant(method = "switchTo", constant = @Constant(intValue = 39))
    public int injectedConstant3(int value) {
        return 3 + INVENTORY_END - INVENTORY_START + HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "autofill", constant = @Constant(intValue = 39))
    public int injectedConstant4(int value) {
        return 3 + INVENTORY_END - INVENTORY_START + HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 39))
    public int injectedConstant(int value) {
        return 3 + INVENTORY_END - INVENTORY_START + HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 30))
    public int injectedConstant2(int value) {
        return 3 + INVENTORY_END - INVENTORY_START;
    }
}
