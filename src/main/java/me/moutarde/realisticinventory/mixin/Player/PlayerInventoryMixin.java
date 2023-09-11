package me.moutarde.realisticinventory.mixin.Player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static net.minecraft.screen.PlayerScreenHandler.HOTBAR_END;
import static net.minecraft.screen.PlayerScreenHandler.INVENTORY_START;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Final
    @Shadow
    public static int MAIN_SIZE = HOTBAR_END - INVENTORY_START;

    @Shadow
    public DefaultedList<ItemStack> main = DefaultedList.ofSize(MAIN_SIZE, ItemStack.EMPTY);

    @Shadow @Final public PlayerEntity player;

    @ModifyConstant(method = "getHotbarSize", constant = @Constant(intValue = 9))
    private static int injected3(int value) {
        return HOTBAR_SIZE;
    }

    @ModifyConstant(method = "getOccupiedSlotWithRoomForStack", constant = @Constant(intValue = 40))
    private int injected4(int value) {
        return INVENTORY_START + this.player.realistic_inventory$getInventorySlots() + this.player.realistic_inventory$getHotbarSlots() - 5;
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
}