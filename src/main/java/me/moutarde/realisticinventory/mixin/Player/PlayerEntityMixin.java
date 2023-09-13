package me.moutarde.realisticinventory.mixin.Player;

import com.google.common.collect.ImmutableList;
import me.moutarde.realisticinventory.PlayerEntityExtends;
import me.moutarde.realisticinventory.items.BackpackItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityExtends {
    @Unique
    public int inventorySlots = 0;
    @Unique
    public int hotbarSlots = 1;
    @Unique
    public boolean hasBackpack = false;

    @Shadow @Final private PlayerInventory inventory;

    @Shadow public PlayerScreenHandler playerScreenHandler;

    @Shadow public ScreenHandler currentScreenHandler;

    @Unique private boolean _hasBackpack = false;

    @Override
    public void realistic_inventory$refreshPlayerScreenHandler(ItemStack stack) {
        PlayerEntity player = this.inventory.player;

        DefaultedList<ItemStack> newInventory = DefaultedList.ofSize(inventorySlots + hotbarSlots + 1, ItemStack.EMPTY);
        int addedSlots = newInventory.size() - this.inventory.main.size();
        PlayerScreenHandler oldHandler = this.playerScreenHandler;
        oldHandler.disableSyncing();

        if (addedSlots < 0 && !player.getWorld().isClient) {
            int slotsToThrow = Math.abs(addedSlots);

            if (!hasBackpack && _hasBackpack) {
                slotsToThrow -= 9;
            }

            DefaultedList<ItemStack> itemsToThrow = DefaultedList.ofSize(slotsToThrow, ItemStack.EMPTY);

            for (int i = 0; i < slotsToThrow; ++i) {
                itemsToThrow.set(i, player.getInventory().main.get(newInventory.size() + i - 1));
            }

            ItemScatterer.spawn(((PlayerEntity) (Object) this).getWorld(), ((PlayerEntity) (Object) this).getBlockPos(), itemsToThrow);
        }

        int i = 0;

        if (hasBackpack && !_hasBackpack) {
            List<ItemStack> backpackItems = BackpackItem.getContent(stack);

            for (i = 0; i < backpackItems.size(); ++i) {
                newInventory.set(hotbarSlots + i, backpackItems.get(i));
            }
        }

        if (addedSlots > 0) {
            for (int j = 0; j < this.inventory.main.size(); ++j) {
                if (j < hotbarSlots) {
                    newInventory.set(j, this.inventory.main.get(j));
                } else if (j == hotbarSlots + inventorySlots - addedSlots) {
                    newInventory.set(j + addedSlots, this.inventory.main.get(hotbarSlots + inventorySlots - addedSlots));
                } else {
                    newInventory.set(i + j, this.inventory.main.get(j));
                }
            }
        } else {
            int offset = 0;
            if (!hasBackpack && _hasBackpack) {
                offset = 9;
            }

            for (i = 0; i < newInventory.size(); ++i) {
                if (i < hotbarSlots) {
                    newInventory.set(i, this.inventory.main.get(i));
                } else if (i < hotbarSlots + inventorySlots) {
                    newInventory.set(i, this.inventory.main.get(i + offset));
                } else {
                    newInventory.set(i, this.inventory.main.get(i + Math.abs(addedSlots)));
                }
            }
        }

        this._hasBackpack = hasBackpack;

        this.inventory.main = newInventory;
        this.inventory.combinedInventory = ImmutableList.of(this.inventory.main, this.inventory.armor, this.inventory.offHand);;
        this.playerScreenHandler = new PlayerScreenHandler(this.inventory, !((PlayerEntity) (Object) this).getWorld().isClient(), (PlayerEntity) (Object) this);
        if (this.currentScreenHandler instanceof PlayerScreenHandler) {
            this.currentScreenHandler = ((PlayerEntity) (Object) this).playerScreenHandler;
        }
        this.playerScreenHandler.setCursorStack(oldHandler.getCursorStack().getItem() == stack.getItem() ? stack : oldHandler.getCursorStack());
    }

    @Override
    public int realistic_inventory$getInventorySlots() {
        return this.inventorySlots;
    }

    @Override
    public void realistic_inventory$setInventorySlots(int value) {
        this.inventorySlots = value;
    }

    @Override
    public int realistic_inventory$getHotbarSlots() {
        return this.hotbarSlots;
    }

    @Override
    public void realistic_inventory$setHotbarSlots(int value) {
        this.hotbarSlots = value;
    }

    @Override
    public void realistic_inventory$setHasBackpack(boolean value) {
        this.hasBackpack = value;
    }

    @Override
    public boolean realistic_inventory$hasBackpack() {
        return this.hasBackpack;
    }
}
