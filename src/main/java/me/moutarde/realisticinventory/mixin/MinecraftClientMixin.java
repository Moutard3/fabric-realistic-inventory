package me.moutarde.realisticinventory.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static me.moutarde.realisticinventory.mixin.Player.PlayerScreenHandlerMixin.HOTBAR_START;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @ModifyConstant(method = "handleInputEvents", constant = @Constant(intValue = 9))
    private int injectedConstant2(int value) {
        return HOTBAR_SIZE;
    }

    @ModifyConstant(method = "doItemPick", constant = @Constant(intValue = 36))
    private int injectedConstant(int value) {
        return HOTBAR_START;
    }
}
