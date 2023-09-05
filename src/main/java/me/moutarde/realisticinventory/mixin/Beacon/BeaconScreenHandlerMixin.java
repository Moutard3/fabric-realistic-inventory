package me.moutarde.realisticinventory.mixin.Beacon;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static me.moutarde.realisticinventory.Realistic_inventory.INVENTORY_SIZE;

@Mixin(BeaconScreenHandler.class)
public class BeaconScreenHandlerMixin {
    @Redirect(
            method = "<init>(ILnet/minecraft/inventory/Inventory;Lnet/minecraft/screen/PropertyDelegate;Lnet/minecraft/screen/ScreenHandlerContext;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/BeaconScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/screen/BeaconScreenHandler;addProperties(Lnet/minecraft/screen/PropertyDelegate;)V"
                    ),
                    to = @At(value = "TAIL")
            )
    )
    public Slot onAddSlot(BeaconScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>(ILnet/minecraft/inventory/Inventory;Lnet/minecraft/screen/PropertyDelegate;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At(value = "TAIL"))
    public void injected(int syncId, Inventory inventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context, CallbackInfo ci) {
        int i;
        for(i = 0; i < INVENTORY_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(inventory, HOTBAR_SIZE + i, 36 + (i%9) * 18, 137 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < HOTBAR_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(inventory, i, offset + 36 + i * 18, 195));
        }
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 37))
    public int injectedConstant(int value) {
        return 1 + INVENTORY_SIZE + HOTBAR_SIZE;
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 28))
    public int injectedConstant2(int value) {
        return 1 + INVENTORY_SIZE;
    }

}
