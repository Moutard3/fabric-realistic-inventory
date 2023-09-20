package me.moutarde.realisticinventory.mixin.GrindingStone;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {
    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 39))
    public int injectedConstant(int value, PlayerEntity player) {
        return 3 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 30))
    public int injectedConstant2(int value, PlayerEntity player) {
        return 3 + player.realistic_inventory$getInventorySlots();
    }
}
