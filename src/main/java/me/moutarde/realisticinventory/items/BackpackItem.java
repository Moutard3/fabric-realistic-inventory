package me.moutarde.realisticinventory.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;

public class BackpackItem extends Item {
    private static final String ITEMS_KEY = "Items";

    public BackpackItem(Settings settings) {
        super(settings);
    }

    public static void saveBackpack(ItemStack backpack, List<ItemStack> stacks) {
        backpack.removeSubNbt(ITEMS_KEY);

        NbtCompound backpackNbt = backpack.getOrCreateNbt();
        if (!backpackNbt.contains(ITEMS_KEY)) {
            backpackNbt.put(ITEMS_KEY, new NbtList());
        }

        NbtList nbtItemsList = backpackNbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        for (ItemStack stack : stacks) {
            ItemStack stackCopy = stack.copy();
            NbtCompound nbtCompound = new NbtCompound();
            stackCopy.writeNbt(nbtCompound);
            nbtItemsList.add(nbtCompound);
        }
    }

    public static void saveBackpackSlot(ItemStack backpack, int slotId, ItemStack stack) {
        NbtCompound backpackNbt = backpack.getOrCreateNbt();
        if (!backpackNbt.contains(ITEMS_KEY)) {
            backpackNbt.put(ITEMS_KEY, new NbtList());
        }

        NbtList nbtItemsList = backpackNbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        ItemStack stackCopy = stack.copy();
        NbtCompound nbtCompound = new NbtCompound();
        stackCopy.writeNbt(nbtCompound);
        nbtItemsList.set(slotId, nbtCompound);
    }

    public static List<ItemStack> getContent(ItemStack backpack) {
        NbtCompound backpackNbt = backpack.getOrCreateNbt();

        if (!backpackNbt.contains(ITEMS_KEY)) {
            backpackNbt.put(ITEMS_KEY, new NbtList());
        }

        List<ItemStack> itemStackList = new ArrayList<>();

        NbtList nbtItemsList = backpackNbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < 9; ++i) {
            if (i < nbtItemsList.size()) {
                NbtCompound itemCompound = nbtItemsList.getCompound(i);
                ItemStack itemStack = ItemStack.fromNbt(itemCompound);
                itemStackList.add(itemStack);
            } else {
                itemStackList.add(ItemStack.EMPTY);
            }
        }

        return itemStackList;
    }
}
