package me.moutarde.realisticinventory.mixin.Player;

import me.moutarde.realisticinventory.items.BackpackItem;
import net.minecraft.entity.EquipmentSlot;
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

            if (slotId >= INVENTORY_START &&
                    slotId < INVENTORY_START + 9 &&
                    player.realistic_inventory$hasBackpack()
            ) {
                BackpackItem.saveBackpackSlot(player.getEquippedStack(EquipmentSlot.CHEST), slotId - INVENTORY_START, stack);
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