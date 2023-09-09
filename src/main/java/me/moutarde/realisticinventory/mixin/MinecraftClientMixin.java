package me.moutarde.realisticinventory.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static net.minecraft.screen.PlayerScreenHandler.HOTBAR_END;
import static net.minecraft.screen.PlayerScreenHandler.HOTBAR_START;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @ModifyConstant(method = "handleInputEvents", constant = @Constant(intValue = 9))
    private int injectedConstant2(int value) {
        return HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "doItemPick", constant = @Constant(intValue = 36))
    private int injectedConstant(int value) {
        return HOTBAR_START;
    }
}
