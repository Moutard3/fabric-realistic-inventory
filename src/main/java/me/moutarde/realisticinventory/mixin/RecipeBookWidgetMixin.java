package me.moutarde.realisticinventory.mixin;

import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetMixin {
    @Shadow private int leftOffset;

    @Inject(method = "reset", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;leftOffset:I"))
    private void injected(CallbackInfo ci) {
        leftOffset = 110;
    }
}
