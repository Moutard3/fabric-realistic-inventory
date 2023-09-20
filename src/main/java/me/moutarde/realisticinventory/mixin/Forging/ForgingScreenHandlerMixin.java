package me.moutarde.realisticinventory.mixin.Forging;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin {
    @Shadow @Final protected PlayerEntity player;

    @ModifyConstant(method = "getPlayerInventoryEndIndex", constant = @Constant(intValue = 27))
    public int injected(int value) {
        return this.player.realistic_inventory$getInventorySlots();
    }

    @ModifyConstant(method = "getPlayerHotbarEndIndex", constant = @Constant(intValue = 9))
    public int injected2(int value) {
        return this.player.realistic_inventory$getHotbarSlots();
    }
}
