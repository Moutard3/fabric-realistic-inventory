package me.moutarde.realisticinventory.mixin.Hud;

import com.mojang.blaze3d.systems.RenderSystem;
import me.moutarde.realisticinventory.mixin.Hud.InGameHudAccessor;
import me.moutarde.realisticinventory.mixin.Hud.InGameHudInvoker;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static net.minecraft.client.gui.widget.ClickableWidget.WIDGETS_TEXTURE;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    private void renderHotbar(float tickDelta, DrawContext context) {
        PlayerEntity playerEntity = ((InGameHudInvoker) this).invokeGetCameraPlayer();
        if (playerEntity != null) {
            ItemStack itemStack = playerEntity.getOffHandStack();
            Arm arm = playerEntity.getMainArm().getOpposite();
            int i = context.getScaledWindowWidth() / 2;
            int j = 1;
            int k = 1;
            context.getMatrices().push();
            context.getMatrices().translate(0.0F, 0.0F, -90.0F);
            context.drawTexture(WIDGETS_TEXTURE, i - 91, context.getScaledWindowHeight() - 22, 0, 0, 182, 22);
            context.drawTexture(WIDGETS_TEXTURE, i - 91 - 1 + playerEntity.getInventory().selectedSlot * 20 + 4 * 20, context.getScaledWindowHeight() - 22 - 1, 0, 22, 24, 22);
            if (!itemStack.isEmpty()) {
                if (arm == Arm.LEFT) {
                    context.drawTexture(WIDGETS_TEXTURE, i - 91 - 29, context.getScaledWindowHeight() - 23, 24, 22, 29, 24);
                } else {
                    context.drawTexture(WIDGETS_TEXTURE, i + 91, context.getScaledWindowHeight() - 23, 53, 22, 29, 24);
                }
            }

            context.getMatrices().pop();
            int l = 1;

            int m;
            int n;
            int o;
            final int offset = 4 * 20;
            for(m = 0; m < HOTBAR_SIZE; ++m) {
                n = i - 90 + m * 20 + 2 + offset;
                o = context.getScaledWindowHeight() - 16 - 3;
                ((InGameHudInvoker) this).invokeRenderHotbarItem(context, n, o, tickDelta, playerEntity, (ItemStack)playerEntity.getInventory().main.get(m), l++);
            }

            if (!itemStack.isEmpty()) {
                m = context.getScaledWindowHeight() - 16 - 3;
                if (arm == Arm.LEFT) {
                    ((InGameHudInvoker) this).invokeRenderHotbarItem(context, i - 91 - 26, m, tickDelta, playerEntity, itemStack, l++);
                } else {
                    ((InGameHudInvoker) this).invokeRenderHotbarItem(context, i + 91 + 10, m, tickDelta, playerEntity, itemStack, l++);
                }
            }

            RenderSystem.enableBlend();


            if (((InGameHudAccessor) this).getClient().options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR) {
                float f = ((InGameHudAccessor) this).getClient().player.getAttackCooldownProgress(0.0F);
                if (f < 1.0F) {
                    n = context.getScaledWindowHeight() - 20;
                    o = i + 91 + 6;
                    if (arm == Arm.RIGHT) {
                        o = i - 91 - 22;
                    }

                    int p = (int)(f * 19.0F);
                    context.drawTexture(((InGameHudAccessor) this).getICONS(), o, n, 0, 94, 18, 18);
                    context.drawTexture(((InGameHudAccessor) this).getICONS(), o, n + 18 - p, 18, 112 - p, 18, p);
                }
            }


            RenderSystem.disableBlend();
        }
    }
}
