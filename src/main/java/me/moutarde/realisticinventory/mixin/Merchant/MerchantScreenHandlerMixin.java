package me.moutarde.realisticinventory.mixin.Merchant;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.MerchantScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MerchantScreenHandler.class)
public class MerchantScreenHandlerMixin {
    @Unique
    PlayerEntity player;

    @ModifyConstant(method = "switchTo", constant = @Constant(intValue = 39))
    public int injectedConstant3(int value) {
        return 3 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "autofill", constant = @Constant(intValue = 39))
    public int injectedConstant4(int value) {
        return 3 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 39))
    public int injectedConstant(int value) {
        return 3 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 30))
    public int injectedConstant2(int value) {
        return 3 + player.realistic_inventory$getInventorySlots();
    }
}
