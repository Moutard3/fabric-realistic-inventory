package me.moutarde.realisticinventory.mixin.plugins.LibGui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BackgroundPainter.class)
public interface BackgroundPainterMixin {
//    @Inject(method = "lambda$static$0", at = @At(value = "INVOKE_ASSIGN", target = "Lio/github/cottonmc/cotton/gui/widget/WItemSlot;getWidth()I", ordinal = 1), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
//    private static void injected(DrawContext context, int left, int top, WWidget panel, CallbackInfo ci, WItemSlot slot, int x, int y)
//    {
//        if (panel instanceof WItemSlot wItemSlot) {
//            Inventory inventory = ((WItemSlotAccessor) wItemSlot).getInventory();
//
//            if (inventory instanceof PlayerInventory playerInventory) {
//                int index = x + y * (slot.getWidth() / 18);
//
//                if (playerInventory.main.size() < index) {
//                    ci.cancel();
//                }
//            }
//        }
//    }

    @Redirect(method = "lambda$static$0", at = @At(value = "INVOKE", target = "Lio/github/cottonmc/cotton/gui/client/ScreenDrawing;texturedRect(Lnet/minecraft/client/gui/DrawContext;IIIILnet/minecraft/util/Identifier;FFFFI)V"))
    private static void redirectDraw(DrawContext context, int sx, int sy, int width, int height, Identifier texture, float u1, float v1, float u2, float v2, int color, DrawContext context2, int left, int top, WWidget panel)
    {
        if (panel instanceof WItemSlot wItemSlot) {
            Inventory inventory = ((WItemSlotAccessor) wItemSlot).getInventory();

            if (inventory instanceof PlayerInventory playerInventory) {
                int x;
                int y;

                if (wItemSlot.isBigSlot()) {
                    x = (sx - left + 4) / 18;
                    y = (sy - top + 4) / 18;
                } else {
                    x = (sx - left) / 18;
                    y = (sy - top) / 18;
                }

                int slotIndex = x + y * (wItemSlot.getWidth() / 18);

                if (wItemSlot.getHeight() / 18 > 1) {
                    slotIndex += 9;
                }

                int hotbarSize = playerInventory.player.realistic_inventory$getHotbarSlots();
                int inventorySize = playerInventory.player.realistic_inventory$getInventorySlots();
                final int hotbarOffset = 18 * 4;

                int destSlotIndex = slotIndex + 8;
                int destRow = destSlotIndex / 9 - 1;
                int destCol = destSlotIndex % 9;

                int row = slotIndex/9 - 1;
                int col = slotIndex%9;

                int offsetX = sx - 18 * col;
                int offsetY = sy - (slotIndex < 9 ? 58 : 18 * row);

                int newSx = destCol * 18 + offsetX;
                int newSy = destRow * 18 + offsetY;

                if (slotIndex < hotbarSize) {
                    sx = hotbarOffset + sx;
                } else if (slotIndex < hotbarSize + inventorySize) {
                    sx = newSx;
                    sy = newSy;
                } else {
                    return;
                }
            }
        }

        ScreenDrawing.texturedRect(context, sx, sy, width, height, texture, u1, v1, u2, v2, color);
    }
}
