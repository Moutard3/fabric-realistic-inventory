package me.moutarde.realisticinventory.mixin.BrewingStand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BrewingStandScreenHandler.class)
public class BrewingStandScreenHandlerMixin {
    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 41))
    public int injectedConstant(int value, PlayerEntity player) {
        return 5 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 32))
    public int injectedConstant2(int value, PlayerEntity player) {
        return 5 + player.realistic_inventory$getInventorySlots();
    }
}
