package me.moutarde.realisticinventory.mixin.Player;

import me.moutarde.realisticinventory.mixin.HandledScreenAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.moutarde.realisticinventory.Realistic_inventory.BACKPACK_SLOT_TEXTURE;
import static me.moutarde.realisticinventory.Realistic_inventory.SLOT_TEXTURE;
import static net.minecraft.screen.PlayerScreenHandler.INVENTORY_END;
import static net.minecraft.screen.PlayerScreenHandler.INVENTORY_START;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {
    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void injected(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        int i = ((HandledScreenAccessor) this).getX();
        int j = ((HandledScreenAccessor) this).getY();

        for (int k = 0; k < INVENTORY_END - INVENTORY_START; ++k) {
            context.drawTexture(SLOT_TEXTURE, i + 8 - 1 + (k%9) * 18, j + 84 - 1 + (k/9) * 18, 0, 0.0f, 0.0f, 18, 18, 18, 18);
        }

        context.drawTexture(BACKPACK_SLOT_TEXTURE, i - 17 - 8, j + 8 + 18 - 8, 0, 0.0f, 0.0f, 28, 32, 28, 32);
    }

    @Inject(method = "isClickOutsideBounds", at = @At("RETURN"), cancellable = true)
    private void injectBound(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> cir) {
        boolean isBackpackSlot = mouseX > (double)left - 17 - 8 && mouseX < (double)left - 17 - 8 + 28 && mouseY > (double)top + 8 + 18 - 8 && mouseY < (double)top + 8 + 18 - 8 + 32;

        cir.setReturnValue(cir.getReturnValue() && !isBackpackSlot);
    }
}
