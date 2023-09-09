package me.moutarde.realisticinventory;

public interface PlayerEntityExtends {
    default int realistic_inventory$getInventorySlots() {return 0;}
    default void realistic_inventory$setInventorySlots(int value) {}
    default int realistic_inventory$getHotbarSlots() {return 0;}
    default void realistic_inventory$setHotbarSlots(int value) {}

    default void realistic_inventory$refreshPlayerScreenHandler() {}
}
