package me.moutarde.realisticinventory.mixin.Stonecutter;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StonecutterScreenHandler.class)
public class StonecutterScreenHandlerMixin {
    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 38))
    public int injectedConstant(int value, PlayerEntity player) {
        return 2 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 29))
    public int injectedConstant2(int value, PlayerEntity player) {
        return 2 + player.realistic_inventory$getInventorySlots();
    }
}
