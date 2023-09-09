package me.moutarde.realisticinventory.mixin.Player;

import com.google.common.collect.ImmutableList;
import me.moutarde.realisticinventory.PlayerEntityExtends;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
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


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityExtends {
    @Shadow @Final private PlayerInventory inventory;

    @Shadow public PlayerScreenHandler playerScreenHandler;

    @Shadow public ScreenHandler currentScreenHandler;

    @Shadow protected abstract void dropInventory();

    @Override
    public void realistic_inventory$refreshPlayerScreenHandler() {
        DefaultedList<ItemStack> newInventory = DefaultedList.ofSize(PlayerInventory.MAIN_SIZE, ItemStack.EMPTY);
        int diff = this.inventory.main.size() - newInventory.size();
        PlayerScreenHandler oldHandler = this.playerScreenHandler;

        if (newInventory.size() < this.inventory.main.size()) {
            DefaultedList<ItemStack> itemsToThrow = DefaultedList.ofSize(diff, ItemStack.EMPTY);

            for (int i = 0; i < diff; ++i) {
                itemsToThrow.set(i, this.inventory.main.get(newInventory.size() + i));
            }

            ItemScatterer.spawn(((PlayerEntity) (Object) this).getWorld(), ((PlayerEntity) (Object) this).getBlockPos(), itemsToThrow);
        }

        for (int i = 0; i < (diff > 0 ? newInventory.size() : this.inventory.main.size()); ++i) {
            newInventory.set(i, this.inventory.main.get(i));
        }

        this.inventory.main = newInventory;
        this.inventory.combinedInventory = ImmutableList.of(this.inventory.main, this.inventory.armor, this.inventory.offHand);;
        this.playerScreenHandler = new PlayerScreenHandler(this.inventory, !((PlayerEntity) (Object) this).getWorld().isClient(), (PlayerEntity) (Object) this);
        this.currentScreenHandler = ((PlayerEntity) (Object) this).playerScreenHandler;
        this.playerScreenHandler.setCursorStack(oldHandler.getCursorStack());
        if (((PlayerEntity) (Object) this).getWorld().isClient && MinecraftClient.getInstance().currentScreen instanceof InventoryScreen) {
            MinecraftClient.getInstance().setScreen(new InventoryScreen(((PlayerEntity) (Object) this)));
        }
    }
}
