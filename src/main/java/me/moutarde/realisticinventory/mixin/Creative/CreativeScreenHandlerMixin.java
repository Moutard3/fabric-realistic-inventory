package me.moutarde.realisticinventory.mixin.Creative;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.CreativeScreenHandler.class)
public abstract class CreativeScreenHandlerMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 1))
    public Slot onAddSlot(CreativeInventoryScreen.CreativeScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injected(PlayerEntity player, CallbackInfo ci) {
        int i;
        PlayerInventory playerInventory = player.getInventory();

        final int offset = 18 * 4;
        for (i = 0; i < player.realistic_inventory$getHotbarSlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, 9 + i * 18 + offset, 112));
        }
    }
}
