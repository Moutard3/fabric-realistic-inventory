package me.moutarde.realisticinventory.client;

import me.moutarde.realisticinventory.Realistic_inventory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

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
                    Realistic_inventory.changeInventorySize(inventorySize, client.player);
                }
            });
        });
    }
}
