package me.moutarde.realisticinventory.mixin.Horse;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.screen.PlayerScreenHandler.*;

@Mixin(HorseScreenHandler.class)
public class HorseScreenHandlerMixin {
    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/HorseScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/screen/HorseScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                            ordinal = 3
                    ),
                    to = @At(value = "TAIL")
            )
    )
    public Slot onAddSlot(HorseScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void injected(int syncId, PlayerInventory playerInventory, Inventory inventory, AbstractHorseEntity entity, CallbackInfo ci) {
        int i;
        for(i = 0; i < playerInventory.player.realistic_inventory$getInventorySlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, playerInventory.player.realistic_inventory$getHotbarSlots() + i, 8 + (i%9) * 18, 102 + (i/9) * 18 - 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < playerInventory.player.realistic_inventory$getHotbarSlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, offset + 8 + i * 18, 142));
        }
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 27))
    public int injected1(int value, PlayerEntity player) {
        return player.realistic_inventory$getInventorySlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 9))
    public int injected2(int value, PlayerEntity player) {
        return player.realistic_inventory$getHotbarSlots();
    }
}
