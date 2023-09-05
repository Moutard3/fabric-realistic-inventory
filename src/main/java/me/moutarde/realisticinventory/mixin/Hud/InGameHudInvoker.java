package me.moutarde.realisticinventory.mixin.Hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InGameHud.class)
public abstract interface InGameHudInvoker {
    @Invoker("getCameraPlayer")
    PlayerEntity invokeGetCameraPlayer();

    @Invoker("renderHotbarItem")
    void invokeRenderHotbarItem(DrawContext context, int x, int y, float f, PlayerEntity player, ItemStack stack, int seed);

}
