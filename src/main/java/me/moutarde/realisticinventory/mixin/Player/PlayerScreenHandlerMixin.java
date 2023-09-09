package me.moutarde.realisticinventory.mixin.Player;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static me.moutarde.realisticinventory.Realistic_inventory.INVENTORY_SIZE;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin {
    @Shadow @Final
    public static final int CRAFTING_RESULT_ID = 0;
    @Shadow @Final
    public static final int CRAFTING_INPUT_START = 1;
    @Shadow @Final
    public static final int CRAFTING_INPUT_END = 5;
    @Shadow @Final
    public static final int EQUIPMENT_START = CRAFTING_INPUT_END;
    @Shadow @Final
    public static final int EQUIPMENT_END = 9;
    @Shadow @Final
    public static final int INVENTORY_START = EQUIPMENT_END;
    @Shadow
    public static int INVENTORY_END = INVENTORY_START + INVENTORY_SIZE;
    @Shadow
    public static int HOTBAR_START = INVENTORY_END;
    @Shadow
    public static int HOTBAR_END = HOTBAR_START + HOTBAR_SIZE;
    @Shadow
    public static int OFFHAND_ID = HOTBAR_END;

    @Shadow @Final private PlayerEntity owner;

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                            ordinal = 3
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                            ordinal = 4
                    )
            )
    )
    public Slot onAddSlot(PlayerScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 5, shift = At.Shift.BEFORE))
    public void injected(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        int i;
        for(i = 0; i < owner.realistic_inventory$getInventorySlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(inventory, owner.realistic_inventory$getHotbarSlots() + i, 8 + (i%9) * 18, 84 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < owner.realistic_inventory$getHotbarSlots(); ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(inventory, i, offset + 8 + i * 18, 142));
        }
    }

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 40))
    public int injectedConstant3(int value) {
        return INVENTORY_START + owner.realistic_inventory$getInventorySlots() + owner.realistic_inventory$getHotbarSlots() - 5;
    }

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 39))
    public int injectedConstant4(int value) {
        return owner.realistic_inventory$getHotbarSlots() + owner.realistic_inventory$getInventorySlots() + 4 - 1;
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 45))
    public int injectedConstant(int value) {
        return INVENTORY_START + owner.realistic_inventory$getInventorySlots() + owner.realistic_inventory$getHotbarSlots();
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 46))
    public int injectedConstant5(int value) {
        return INVENTORY_START + owner.realistic_inventory$getInventorySlots() + owner.realistic_inventory$getHotbarSlots() + 1;
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 36))
    public int injectedConstant2(int value) {
        return INVENTORY_START + owner.realistic_inventory$getInventorySlots();
    }

    @ModifyConstant(method = "isInHotbar", constant = @Constant(intValue = 36))
    private static int injectedConstant6(int value) {
        return INVENTORY_END;
    }

    @ModifyConstant(method = "isInHotbar", constant = @Constant(intValue = 45))
    private static int injectedConstant7(int value) {
        return OFFHAND_ID;
    }
}
