package me.moutarde.realisticinventory.mixin;

import me.moutarde.realisticinventory.items.BackpackItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Shadow @Final private @Nullable ScreenHandlerType<?> type;

    @Shadow @Final public DefaultedList<Slot> slots;
    @Shadow @Final private DefaultedList<ItemStack> previousTrackedStacks;
    @Shadow @Final private DefaultedList<ItemStack> trackedStacks;
    @Unique int playerStartIndex = 0;
    @Unique PlayerEntity player;

    @Inject(method = "onClosed", at = @At("TAIL"))
    private void injected(PlayerEntity player, CallbackInfo ci)
    {
        if (player instanceof ServerPlayerEntity && player.realistic_inventory$hasBackpack()) {
            BackpackItem.saveBackpack(
                    player.getInventory().getStack(player.realistic_inventory$getHotbarSlots() + player.realistic_inventory$getInventorySlots()),
                    player.getInventory().main.subList(player.realistic_inventory$getHotbarSlots(), player.realistic_inventory$getHotbarSlots() + 9)
            );
        }
    }

    @Inject(method = "addSlot", at = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/Slot;id:I"), cancellable = true)
    private void injectedCancelAddSlot(Slot slot, CallbackInfoReturnable<Slot> cir)
    {
        if (slot.inventory instanceof PlayerInventory playerInventory && this.type != null) {
            int slotIndex = slot.getIndex();
            int hotbarSize = playerInventory.player.realistic_inventory$getHotbarSlots();
            int inventorySize = playerInventory.player.realistic_inventory$getInventorySlots();

            if (slotIndex >= hotbarSize + inventorySize) {
                cir.setReturnValue(slot);
                return;
            }

            if (slotIndex >= hotbarSize && this.slots.size() > playerStartIndex + slotIndex) {
                this.slots.forEach(slot1 -> {
                    if (slot1.id >= playerStartIndex + slotIndex - hotbarSize) {
                        slot1.id++;
                    }
                });

                slot.id = playerStartIndex + slotIndex - hotbarSize;
                this.slots.add(playerStartIndex + slotIndex - hotbarSize, slot);
                this.trackedStacks.add(playerStartIndex + slotIndex - hotbarSize, ItemStack.EMPTY);
                this.previousTrackedStacks.add(playerStartIndex + slotIndex - hotbarSize, ItemStack.EMPTY);
                cir.setReturnValue(slot);
                return;
            }
        }
    }

    @ModifyVariable(method = "addSlot", at = @At("HEAD"), argsOnly = true)
    private Slot modifySlot(Slot slot)
    {
        boolean slotIsLibGui = false;
        try {
            slotIsLibGui = Class.forName("io.github.cottonmc.cotton.gui.ValidatedSlot").isInstance(slot);
        } catch (ClassNotFoundException ignored) {}

        if (slot.inventory instanceof PlayerInventory playerInventory && this.type != null) {
            if (this.player == null) {
                this.player = ((PlayerInventory) slot.inventory).player;
            }

            int slotIndex = slot.getIndex();
            int hotbarSize = playerInventory.player.realistic_inventory$getHotbarSlots();
            int inventorySize = playerInventory.player.realistic_inventory$getInventorySlots();
            final int hotbarOffset = 18 * 4;

            int destSlotIndex = slotIndex + 8;
            int destRow = destSlotIndex / 9 - 1;
            int destCol = destSlotIndex % 9;

            int row = slotIndex/9 - 1;
            int col = slotIndex%9;

            int offsetX = slot.x - 18 * col;
            int offsetY = slot.y - (slotIndex < 9 ? 58 : 18 * row);

            int x = destCol * 18 + offsetX;
            int y = destRow * 18 + offsetY;

            if (slotIndex < hotbarSize) {
                slot = new Slot(playerInventory, slotIndex, hotbarOffset + slot.x, slot.y);
            } else if (slotIndex < hotbarSize + inventorySize) {
                slot = new Slot(playerInventory, slotIndex, x, y);
            }
        } else if (!slotIsLibGui) {
            playerStartIndex++;
        }

        return slot;
    }

    @ModifyVariable(method = "insertItem", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int injectedInsertItemStartIndex(int startIndex) {
        if (this.player == null) return startIndex;

        if (startIndex == playerStartIndex + 9 + 27) {
            return playerStartIndex + player.realistic_inventory$getHotbarSlots() + player.realistic_inventory$getInventorySlots();
        }

        if (startIndex == playerStartIndex + 27) {
            return playerStartIndex + player.realistic_inventory$getInventorySlots();
        }

        return startIndex;
    }

    @ModifyVariable(method = "insertItem", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private int injectedInsertItemEndIndex(int endIndex) {
        if (this.player == null) return endIndex;

        if (endIndex == playerStartIndex + 9 + 27) {
            return playerStartIndex + player.realistic_inventory$getHotbarSlots() + player.realistic_inventory$getInventorySlots();
        }

        if (endIndex == playerStartIndex + 27) {
            return playerStartIndex + player.realistic_inventory$getInventorySlots();
        }

        return endIndex;
    }
}
