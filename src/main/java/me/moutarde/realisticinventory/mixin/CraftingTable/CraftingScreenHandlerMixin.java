package me.moutarde.realisticinventory.mixin.CraftingTable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.CraftingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin {
    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 46))
    public int injectedConstant(int value, PlayerEntity player) {
        return 10 + player.realistic_inventory$getInventorySlots() + player.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 37))
    public int injectedConstant2(int value, PlayerEntity player) {
        return 10 + player.realistic_inventory$getInventorySlots();
    }
}
