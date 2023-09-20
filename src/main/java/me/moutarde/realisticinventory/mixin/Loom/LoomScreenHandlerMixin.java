package me.moutarde.realisticinventory.mixin.Loom;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoomScreenHandler.class)
public class LoomScreenHandlerMixin {
    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 40))
    public int injectedConstant(int value, PlayerEntity player) {
        return 4 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 31))
    public int injectedConstant2(int value, PlayerEntity player) {
        return 4 + player.realistic_inventory$getInventorySlots();
    }
}
