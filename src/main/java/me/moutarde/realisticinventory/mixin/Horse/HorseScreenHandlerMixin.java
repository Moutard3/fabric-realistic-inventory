package me.moutarde.realisticinventory.mixin.Horse;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.HorseScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HorseScreenHandler.class)
public class HorseScreenHandlerMixin {
    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 27))
    public int injected1(int value, PlayerEntity player) {
        return player.realistic_inventory$getInventorySlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 9))
    public int injected2(int value, PlayerEntity player) {
        return player.realistic_inventory$getHotbarSlots();
    }
}
