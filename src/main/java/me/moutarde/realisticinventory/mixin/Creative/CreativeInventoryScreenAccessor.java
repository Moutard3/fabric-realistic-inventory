package me.moutarde.realisticinventory.mixin.Creative;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.inventory.SimpleInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreativeInventoryScreen.class)
public interface CreativeInventoryScreenAccessor {
    @Accessor("INVENTORY")
    @Final
    SimpleInventory getInventory();
}
