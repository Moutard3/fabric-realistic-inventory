package me.moutarde.realisticinventory.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Generic3x3ContainerScreenHandler.class)
public class Generic3x3ContainerScreenHandlerMixin {
    @Unique
    private static final int CONTAINER_SIZE = 9;
    @Unique
    private static final int INVENTORY_START = CONTAINER_SIZE;
    @Unique
    private static final int INVENTORY_END = INVENTORY_START + PlayerScreenHandler.INVENTORY_END - PlayerScreenHandler.INVENTORY_START;
    @Unique
    private static final int HOTBAR_START = INVENTORY_END;
    @Unique
    private static final int HOTBAR_END = HOTBAR_START + PlayerScreenHandler.HOTBAR_END - PlayerScreenHandler.HOTBAR_START;

    @Redirect(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Generic3x3ContainerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"))
    public Slot onAddSlot(Generic3x3ContainerScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V", at = @At(value = "TAIL"))
    public void injected(int syncId, PlayerInventory playerInventory, Inventory inventory, CallbackInfo ci) {
        int j;
        int i;

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(inventory, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }

        for(i = 0; i < INVENTORY_END - INVENTORY_START; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, HOTBAR_END - HOTBAR_START + i, 8 + (i%9) * 18, 84 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < HOTBAR_END - HOTBAR_START; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, offset + 8 + i * 18, 142));
        }
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 45))
    private int injected2(int value) {
        return HOTBAR_END;
    }
}
