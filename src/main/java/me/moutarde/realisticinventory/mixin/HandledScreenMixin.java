package me.moutarde.realisticinventory.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static net.minecraft.screen.PlayerScreenHandler.*;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @ModifyConstant(method = "onMouseClick(I)V", constant = @Constant(intValue = 9))
    public int injected(int value) {
        return HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "onMouseClick(I)V", constant = @Constant(intValue = 40))
    public int injected3(int value) {
        return OFFHAND_ID - CRAFTING_INPUT_END;
    }

    @ModifyConstant(method = "handleHotbarKeyPressed", constant = @Constant(intValue = 9))
    public int injected2(int value) {
        return HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "handleHotbarKeyPressed", constant = @Constant(intValue = 40))
    public int injected4(int value) {
        return OFFHAND_ID - CRAFTING_INPUT_END;
    }
}
