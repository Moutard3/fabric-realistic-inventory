package me.moutarde.realisticinventory;

import net.minecraft.item.ItemStack;

public interface PlayerEntityExtends {
    default int realistic_inventory$getInventorySlots() {return 0;}
    default void realistic_inventory$setInventorySlots(int value) {}
    default int realistic_inventory$getHotbarSlots() {return 0;}
    default void realistic_inventory$setHotbarSlots(int value) {}

    default void realistic_inventory$refreshPlayerScreenHandler(ItemStack stack) {}

    default void realistic_inventory$setHasBackpack(boolean value) {}
    default boolean realistic_inventory$hasBackpack() {return false;}
}
