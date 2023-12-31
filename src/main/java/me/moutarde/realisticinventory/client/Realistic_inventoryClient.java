package me.moutarde.realisticinventory.client;

import me.moutarde.realisticinventory.Realistic_inventory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;

import static me.moutarde.realisticinventory.Realistic_inventory.BACKPACK_ITEM;
import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;

public class Realistic_inventoryClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Realistic_inventory.CHANGE_INVENTORY_SIZE_PACKET_ID, (client, handler, buf, responseSender) -> {
            int inventorySize = buf.readInt();

            client.execute(() -> {
                if (client.player != null) {
                    ItemStack backpackStack = client.player.getInventory().getStack(client.player.realistic_inventory$getInventorySlots() + client.player.realistic_inventory$getHotbarSlots());

                    client.player.realistic_inventory$setHasBackpack(backpackStack.isOf(BACKPACK_ITEM));

                    Realistic_inventory.changeInventorySize(inventorySize, client.player, client.player.getInventory().getStack(client.player.realistic_inventory$getInventorySlots() + client.player.realistic_inventory$getHotbarSlots()));
                    if (client.currentScreen instanceof InventoryScreen || client.currentScreen instanceof CreativeInventoryScreen) {
                        MinecraftClient.getInstance().setScreen(new InventoryScreen(client.player));
                    }
                }
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            client.execute(() -> {
                PlayerScreenHandler.INVENTORY_END = PlayerScreenHandler.INVENTORY_START + 27;
                PlayerScreenHandler.HOTBAR_START = PlayerScreenHandler.INVENTORY_END;
                PlayerScreenHandler.HOTBAR_END = PlayerScreenHandler.HOTBAR_START + HOTBAR_SIZE;
                PlayerScreenHandler.OFFHAND_ID = PlayerScreenHandler.HOTBAR_END;
                PlayerInventory.MAIN_SIZE = PlayerScreenHandler.HOTBAR_END - PlayerScreenHandler.INVENTORY_START + 1;
            });
        });
    }
}
