package me.moutarde.realisticinventory.mixin.Player;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static net.minecraft.screen.PlayerScreenHandler.*;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow
    public static int MAIN_SIZE = HOTBAR_END - INVENTORY_START;

    @Shadow
    public DefaultedList<ItemStack> main = DefaultedList.ofSize(MAIN_SIZE, ItemStack.EMPTY);

    @ModifyConstant(method = "getHotbarSize", constant = @Constant(intValue = 9))
    private static int injected3(int value) {
        return HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "getOccupiedSlotWithRoomForStack", constant = @Constant(intValue = 40))
    private static int injected4(int value) {
        return OFFHAND_ID;
    }

    @ModifyConstant(method = "isValidHotbarIndex", constant = @Constant(intValue = 9))
    private static int isValidHotbarIndex(int value) {
        return HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "scrollInHotbar", constant = @Constant(intValue = 9))
    public int injected(int value) {
        return HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "getSwappableHotbarSlot", constant = @Constant(intValue = 9))
    public int injected2(int value) {
        return HOTBAR_END - HOTBAR_START;
    }
}