package me.moutarde.realisticinventory.mixin.Player;

import me.moutarde.realisticinventory.items.BackpackChangeCallback;
import me.moutarde.realisticinventory.items.BackpackItem;
import me.moutarde.realisticinventory.items.BackpackSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.moutarde.realisticinventory.Realistic_inventory.BACKPACK_ITEM;
import static net.minecraft.screen.PlayerScreenHandler.INVENTORY_START;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Unique
    private final ScreenHandlerListener screenHandlerListener = new ScreenHandlerListener(){

        @Override
        public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
            Slot slot = handler.getSlot(slotId);
            if (slot instanceof CraftingResultSlot) {
                return;
            }
            ServerPlayerEntity player = ((ServerPlayerEntity) (Object) ServerPlayerEntityMixin.this);

            if (slot instanceof BackpackSlot) {
                BackpackChangeCallback.EVENT.invoker().onChange(player, stack.isOf(BACKPACK_ITEM));
                return;
            }

            if (slotId >= INVENTORY_START &&
                    slotId < INVENTORY_START + 9 &&
                    player.realistic_inventory$hasBackpack()
            ) {
                BackpackItem.saveBackpackSlot(player.getInventory().getStack(player.realistic_inventory$getHotbarSlots() + player.realistic_inventory$getInventorySlots()), slotId - INVENTORY_START, stack);
            }
        }

        @Override
        public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
        }
    };

    @Inject(method = "onScreenHandlerOpened", at = @At("TAIL"))
    private void injected(ScreenHandler screenHandler, CallbackInfo ci) {
        screenHandler.addListener(screenHandlerListener);
    }
}
