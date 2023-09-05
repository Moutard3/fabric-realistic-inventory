package me.moutarde.realisticinventory.mixin.Player;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static me.moutarde.realisticinventory.Realistic_inventory.INVENTORY_SIZE;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow
    @Final
    public final static int MAIN_SIZE = me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;

    @Shadow
    @Final
    public final DefaultedList<ItemStack> main = DefaultedList.ofSize(INVENTORY_SIZE + HOTBAR_SIZE, ItemStack.EMPTY);

    @Shadow @Final private final static int HOTBAR_SIZE = me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;

    @ModifyConstant(method = "getHotbarSize", constant = @Constant(intValue = 9))
    private static int injected3(int value) {
        return MAIN_SIZE;
    }

    @ModifyConstant(method = "isValidHotbarIndex", constant = @Constant(intValue = 9))
    private static int isValidHotbarIndex(int value) {
        return MAIN_SIZE;
    }

    @ModifyConstant(method = "scrollInHotbar", constant = @Constant(intValue = 9))
    public int injected(int value) {
        return me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
    }

    @ModifyConstant(method = "getSwappableHotbarSlot", constant = @Constant(intValue = 9))
    public int injected2(int value) {
        return me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
    }
}