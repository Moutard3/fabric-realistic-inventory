package me.moutarde.realisticinventory.mixin.Player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static net.minecraft.screen.PlayerScreenHandler.HOTBAR_END;
import static net.minecraft.screen.PlayerScreenHandler.INVENTORY_START;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Final
    @Shadow
    public static int MAIN_SIZE = HOTBAR_END - INVENTORY_START + 1;

    @Shadow
    public DefaultedList<ItemStack> main = DefaultedList.ofSize(MAIN_SIZE, ItemStack.EMPTY);

    @Shadow @Final public PlayerEntity player;

    @ModifyConstant(method = "getHotbarSize", constant = @Constant(intValue = 9))
    private static int injected3(int value) {
        return HOTBAR_SIZE;
    }

    @ModifyConstant(method = "getOccupiedSlotWithRoomForStack", constant = @Constant(intValue = 40))
    private int injected4(int value) {
        return this.player.realistic_inventory$getInventorySlots() + this.player.realistic_inventory$getHotbarSlots() + 1 + 4;
    }

    @ModifyConstant(method = "isValidHotbarIndex", constant = @Constant(intValue = 9))
    private static int isValidHotbarIndex(int value) {
        return HOTBAR_SIZE;
    }

    @ModifyConstant(method = "scrollInHotbar", constant = @Constant(intValue = 9))
    public int injected(int value) {
        return HOTBAR_SIZE;
    }

    @ModifyConstant(method = "getSwappableHotbarSlot", constant = @Constant(intValue = 9))
    public int injected2(int value) {
        return HOTBAR_SIZE;
    }

    @Redirect(method = "getEmptySlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;size()I"))
    private int injectedGetEmptySlot(DefaultedList<ItemStack> instance) {
        return instance.size() - 1;
    }

    @Inject(method = "addStack(ILnet/minecraft/item/ItemStack;)I", at = @At(value = "HEAD"), cancellable = true)
    private void injectedAddStack(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int realSlot = slot;

        if (slot < player.realistic_inventory$getHotbarSlots()) {
            realSlot = INVENTORY_START + player.realistic_inventory$getInventorySlots() + 1 + slot;
        } else if (slot < player.realistic_inventory$getHotbarSlots() + player.realistic_inventory$getInventorySlots()) {
            realSlot = INVENTORY_START - 1 + slot;
        }

        if (!this.player.playerScreenHandler.getSlot(realSlot).canInsert(stack)) {
            cir.setReturnValue(stack.getCount());
        }
    }

    @ModifyArg(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putByte(Ljava/lang/String;B)V", ordinal = 0))
    private byte changePutByte(byte value) {
        if (value == this.player.realistic_inventory$getHotbarSlots() + this.player.realistic_inventory$getInventorySlots()) {
            return (byte) (value + 27 + HOTBAR_SIZE - this.main.size() + 1);
        }

        return value;
    }
}