package me.moutarde.realisticinventory.mixin.Beacon;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.BeaconScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BeaconScreenHandler.class)
public class BeaconScreenHandlerMixin {
    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 37))
    public int injectedConstant(int value, PlayerEntity player) {
        return 1 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 28))
    public int injectedConstant2(int value, PlayerEntity player) {
        return 1 + player.realistic_inventory$getInventorySlots();
    }

}
