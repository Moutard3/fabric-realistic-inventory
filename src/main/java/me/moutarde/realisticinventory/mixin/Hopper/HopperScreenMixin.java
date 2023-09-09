package me.moutarde.realisticinventory.mixin.Hopper;

import me.moutarde.realisticinventory.mixin.HandledScreenAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.moutarde.realisticinventory.Realistic_inventory.SLOT_TEXTURE;
import static net.minecraft.screen.PlayerScreenHandler.INVENTORY_END;
import static net.minecraft.screen.PlayerScreenHandler.INVENTORY_START;

@Mixin(HopperScreen.class)
public abstract class HopperScreenMixin {
    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void injected(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        int i = ((HandledScreenAccessor) this).getX();
        int j = ((HandledScreenAccessor) this).getY();

        for (int k = 0; k < INVENTORY_END - INVENTORY_START; ++k) {
            context.drawTexture(SLOT_TEXTURE, i + 8 - 1 + (k%9) * 18, j + 51 - 1 + (k/9) * 18, 0, 0.0f, 0.0f, 18, 18, 18, 18);
        }
    }
}
