package me.moutarde.realisticinventory;

import me.moutarde.realisticinventory.items.BackpackItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Realistic_inventory implements ModInitializer {
    public final static String MOD_ID = "realistic-inventory";

    public final static int INVENTORY_SIZE = 0;
    public final static int HOTBAR_SIZE = 1;

    public static final Identifier SLOT_TEXTURE = new Identifier("textures/gui/container/slot.png");
    public static final Identifier CHANGE_INVENTORY_SIZE_PACKET_ID = new Identifier(MOD_ID, "change_inventory_size");

    public static final Item BACKPACK_ITEM = new BackpackItem(
            new FabricItemSettings()
                    .maxCount(1)
                    .equipmentSlot(stack -> EquipmentSlot.CHEST)
    );

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(BACKPACK_ITEM))
            .displayName(Text.translatable("itemGroup.realistic_inventory.group"))
            .build();

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "group"), ITEM_GROUP);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "backpack"), BACKPACK_ITEM);

        ItemGroupEvents.modifyEntriesEvent(Registries.ITEM_GROUP.getKey(ITEM_GROUP).get()).register(content -> {
            content.add(BACKPACK_ITEM);
        });

        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity.isPlayer()) {
                Realistic_inventory.changeInventorySize(INVENTORY_SIZE, (PlayerEntity) entity);
            }
        });

        ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
            if (!livingEntity.isPlayer() || !equipmentSlot.isArmorSlot()) return;

            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;

            int currentSize = player.realistic_inventory$getInventorySlots();

            if (equipmentSlot == EquipmentSlot.LEGS) {
                int newSize = currentSize + (previousStack.getCount() > currentStack.getCount() ? -2 : 2);

                if (previousStack.getCount() != currentStack.getCount()) {
                    Realistic_inventory.changeInventorySize(newSize, (PlayerEntity) livingEntity);
                }
            } else if (equipmentSlot == EquipmentSlot.CHEST
                    && (previousStack.isOf(BACKPACK_ITEM) || currentStack.isOf(BACKPACK_ITEM))
            ) {
                int newSize = currentSize + (previousStack.getCount() > currentStack.getCount() ? -9 : 9);
                if (previousStack.getCount() != currentStack.getCount()) {
                    Realistic_inventory.changeInventorySize(newSize, (PlayerEntity) livingEntity);
                }
            }
        });
    }

    public static void changeInventorySize(int size, PlayerEntity player) {
        boolean isClient = player.getWorld().isClient;

        player.realistic_inventory$setInventorySlots(size);

        if (!isClient) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(size);
            ServerPlayNetworking.send(serverPlayer, CHANGE_INVENTORY_SIZE_PACKET_ID, buf);

            serverPlayer.realistic_inventory$refreshPlayerScreenHandler();

            serverPlayer.onSpawn();
        } else {
            PlayerScreenHandler.INVENTORY_END = PlayerScreenHandler.INVENTORY_START + size;
            PlayerScreenHandler.HOTBAR_START = PlayerScreenHandler.INVENTORY_END;
            PlayerScreenHandler.HOTBAR_END = PlayerScreenHandler.HOTBAR_START + HOTBAR_SIZE;
            PlayerScreenHandler.OFFHAND_ID = PlayerScreenHandler.HOTBAR_END;
            PlayerInventory.MAIN_SIZE = PlayerScreenHandler.HOTBAR_END - PlayerScreenHandler.INVENTORY_START;

            player.realistic_inventory$refreshPlayerScreenHandler();
        }
    }
}
