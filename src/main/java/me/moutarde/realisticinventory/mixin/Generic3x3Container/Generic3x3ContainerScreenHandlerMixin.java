package me.moutarde.realisticinventory.mixin.Generic3x3Container;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Generic3x3ContainerScreenHandler.class)
public class Generic3x3ContainerScreenHandlerMixin {
    @Shadow @Final private static int INVENTORY_START;

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 45))
    private int injected2(int value, PlayerEntity player) {
        return INVENTORY_START + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }
}
