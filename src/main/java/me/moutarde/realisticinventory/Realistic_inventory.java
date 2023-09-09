package me.moutarde.realisticinventory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class Realistic_inventory implements ModInitializer {
    public final static String MOD_ID = "realistic-inventory";

    public final static int INVENTORY_SIZE = 0;
    public final static int HOTBAR_SIZE = 1;

    public static final Identifier SLOT_TEXTURE = new Identifier("textures/gui/container/slot.png");
    public static final Identifier CHANGE_INVENTORY_SIZE_PACKET_ID = new Identifier(MOD_ID, "change_inventory_size");

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity.isPlayer()) {
                Realistic_inventory.changeInventorySize(INVENTORY_SIZE, (PlayerEntity) entity);
            }
        });

        ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
            if (!livingEntity.isPlayer() || !equipmentSlot.isArmorSlot() || equipmentSlot != EquipmentSlot.LEGS) return;

            int currentSize = PlayerScreenHandler.INVENTORY_END - PlayerScreenHandler.INVENTORY_START;
            int newSize = currentSize + (previousStack.getCount() > currentStack.getCount() ? -2 : 2);
            Realistic_inventory.changeInventorySize(newSize, (PlayerEntity) livingEntity);
        });
    }

    public static void changeInventorySize(int size, PlayerEntity player) {
        boolean isClient = player.getWorld().isClient;

        PlayerScreenHandler.INVENTORY_END = PlayerScreenHandler.INVENTORY_START + size;
        PlayerScreenHandler.HOTBAR_START = PlayerScreenHandler.INVENTORY_END;
        PlayerScreenHandler.HOTBAR_END = PlayerScreenHandler.HOTBAR_START + HOTBAR_SIZE;
        PlayerScreenHandler.OFFHAND_ID = PlayerScreenHandler.HOTBAR_END;
        PlayerInventory.MAIN_SIZE = PlayerScreenHandler.HOTBAR_END - PlayerScreenHandler.INVENTORY_START;

        if (!isClient) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(size);
            ServerPlayNetworking.send(serverPlayer, CHANGE_INVENTORY_SIZE_PACKET_ID, buf);

            serverPlayer.realistic_inventory$refreshPlayerScreenHandler();

            serverPlayer.onSpawn();
        } else {
            player.realistic_inventory$refreshPlayerScreenHandler();
        }
    }
}
