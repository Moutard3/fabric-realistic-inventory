package me.moutarde.realisticinventory.mixin.Creative;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.minecraft.screen.PlayerScreenHandler.*;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @ModifyConstant(method = "onMouseClick", constant = @Constant(intValue = 36))
    private int injectedConstant(int value) {
        return INVENTORY_END - INVENTORY_START + HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "onMouseClick", constant = @Constant(intValue = 9))
    private int injectedConstant2(int value) {
        return HOTBAR_END - HOTBAR_START;
    }

    @ModifyConstant(method = "setSelectedTab", constant = @Constant(intValue = 45))
    private int injectedConstantOffhand(int value) {
        return OFFHAND_ID;
    }

    @ModifyConstant(method = "setSelectedTab", constant = @Constant(intValue = 36))
    private int injectedConstantHotbar(int value) {
        return HOTBAR_START;
    }

//    @ModifyConstant(method = "setSelectedTab", constant = @Constant(intValue = 9, ordinal = 6))
//    private int injectedConstantX(int value) {
//        return 18 * 4 + value;
//    }

    @ModifyArgs(method = "setSelectedTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeSlot;<init>(Lnet/minecraft/screen/slot/Slot;III)V"))
    private void injectedOffset(Args args) {
        int i = args.get(1);
        int n = args.get(2);

        final int offset = 18 * 4;
        if (i >= HOTBAR_START && i < OFFHAND_ID) {
            args.set(2, 9 + (i - HOTBAR_START) % 9 * 18 + offset);
        }
    }

    @ModifyConstant(method = "onHotbarKeyPress", constant = @Constant(intValue = 36))
    private static int injectedConstantHotbarPress(int value) {
        return HOTBAR_START;
    }

}
