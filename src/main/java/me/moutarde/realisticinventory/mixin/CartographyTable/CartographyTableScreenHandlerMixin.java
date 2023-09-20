package me.moutarde.realisticinventory.mixin.CartographyTable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.CartographyTableScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CartographyTableScreenHandler.class)
public class CartographyTableScreenHandlerMixin {
    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 39))
    public int injectedConstant(int value, PlayerEntity player) {
        return 3 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 30))
    public int injectedConstant2(int value, PlayerEntity player) {
        return 3 + player.realistic_inventory$getInventorySlots();
    }
}
