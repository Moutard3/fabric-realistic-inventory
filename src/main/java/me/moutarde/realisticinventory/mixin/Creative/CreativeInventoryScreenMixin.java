package me.moutarde.realisticinventory.mixin.Creative;

import me.moutarde.realisticinventory.mixin.HandledScreenAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static me.moutarde.realisticinventory.Realistic_inventory.SLOT_TEXTURE;
import static net.minecraft.screen.PlayerScreenHandler.*;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin {
    @Shadow public abstract boolean isInventoryTabSelected();

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void injected(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {

        if (this.isInventoryTabSelected()) {
            int i = ((HandledScreenAccessor) this).getX();
            int j = ((HandledScreenAccessor) this).getY();

            for (int k = 0; k < INVENTORY_END - INVENTORY_START; ++k) {
                context.drawTexture(SLOT_TEXTURE, i + 9 - 1 + (k % 9) * 18, j + 54 - 1 + (k / 9) * 18, 0, 0.0f, 0.0f, 18, 18, 18, 18);
            }
        }
    }

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

    @ModifyArgs(method = "setSelectedTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeSlot;<init>(Lnet/minecraft/screen/slot/Slot;III)V"))
    private void injectedOffset(Args args) {
        int i = args.get(1);
        int n = args.get(2);

        final int offset = 18 * 4;
        if (i >= HOTBAR_START && i < OFFHAND_ID) {
            args.set(2, 9 + (i - HOTBAR_START) % 9 * 18 + offset);
        }
    }

    @ModifyArg(method = "onHotbarKeyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickCreativeStack(Lnet/minecraft/item/ItemStack;I)V"))
    private static int injectedCall(int slotId) {
        return HOTBAR_START;
    }

}
