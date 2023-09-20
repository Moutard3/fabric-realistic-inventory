package me.moutarde.realisticinventory.mixin.plugins.LibGui;

import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WItemSlot.class)
public interface WItemSlotAccessor {
    @Accessor
    Inventory getInventory();
}
