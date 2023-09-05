package me.moutarde.realisticinventory.mixin.Stonecutter;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static me.moutarde.realisticinventory.Realistic_inventory.INVENTORY_SIZE;

@Mixin(StonecutterScreenHandler.class)
public class StonecutterScreenHandlerMixin {
    @Redirect(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/StonecutterScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/screen/StonecutterScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                            ordinal = 2
                    ),
                    to = @At(value = "TAIL")
            )
    )
    public Slot onAddSlot(StonecutterScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At(value = "TAIL"))
    public void injected(int syncId, PlayerInventory inventory, ScreenHandlerContext context, CallbackInfo ci) {
        int i;
        for(i = 0; i < INVENTORY_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(inventory, HOTBAR_SIZE + i, 8 + (i%9) * 18, 84 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < HOTBAR_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(inventory, i, offset + 8 + i * 18, 142));
        }
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 38))
    public int injectedConstant(int value) {
        return 2 + INVENTORY_SIZE + HOTBAR_SIZE;
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 29))
    public int injectedConstant2(int value) {
        return 2 + INVENTORY_SIZE;
    }
}
