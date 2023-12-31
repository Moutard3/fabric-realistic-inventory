package me.moutarde.realisticinventory.items;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import static me.moutarde.realisticinventory.Realistic_inventory.BACKPACK_ITEM;

public class BackpackSlot extends Slot {
    public BackpackSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(BACKPACK_ITEM);
    }
}
