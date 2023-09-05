package me.moutarde.realisticinventory.mixin.Creative;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static me.moutarde.realisticinventory.Realistic_inventory.INVENTORY_SIZE;
import static me.moutarde.realisticinventory.mixin.Player.PlayerScreenHandlerMixin.HOTBAR_START;
import static me.moutarde.realisticinventory.mixin.Player.PlayerScreenHandlerMixin.OFFHAND_ID;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @ModifyConstant(method = "onMouseClick", constant = @Constant(intValue = 36))
    private int injectedConstant(int value) {
        return INVENTORY_SIZE + HOTBAR_SIZE;
    }

    @ModifyConstant(method = "onMouseClick", constant = @Constant(intValue = 9))
    private int injectedConstant2(int value) {
        return HOTBAR_SIZE;
    }

    @ModifyConstant(method = "setSelectedTab", constant = @Constant(intValue = 45))
    private int injectedConstantOffhand(int value) {
        return OFFHAND_ID;
    }

    @ModifyConstant(method = "setSelectedTab", constant = @Constant(intValue = 36))
    private int injectedConstantHotbar(int value) {
        return HOTBAR_START;
    }

    @ModifyConstant(method = "setSelectedTab", constant = @Constant(intValue = 9, ordinal = 6))
    private int injectedConstantX(int value) {
        return 18 * 4 + value;
    }

    @ModifyConstant(method = "onHotbarKeyPress", constant = @Constant(intValue = 36))
    private static int injectedConstantHotbarPress(int value) {
        return HOTBAR_START;
    }

}
