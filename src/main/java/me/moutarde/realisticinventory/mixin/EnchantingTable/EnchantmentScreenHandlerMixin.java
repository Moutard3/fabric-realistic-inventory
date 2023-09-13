package me.moutarde.realisticinventory.mixin.EnchantingTable;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.screen.PlayerScreenHandler.*;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin {
    @Shadow
    @Final
    private Inventory inventory;

    @Redirect(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"))
    public Slot onAddSlot(EnchantmentScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At(value = "TAIL"))
    public void injected(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
        int i;

        ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(this.inventory, 0, 15, 47){

            @Override
            public boolean canInsert(ItemStack stack) {
                return true;
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });
        ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(this.inventory, 1, 35, 47){

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.LAPIS_LAZULI);
            }
        });

        for(i = 0; i < playerInventory.player.realistic_inventory$getInventorySlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, playerInventory.player.realistic_inventory$getHotbarSlots() + i, 8 + (i%9) * 18, 84 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < playerInventory.player.realistic_inventory$getHotbarSlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, offset + 8 + i * 18, 142));
        }
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 38))
    public int injected2(int value, PlayerEntity player) {
        return 2 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }
}
