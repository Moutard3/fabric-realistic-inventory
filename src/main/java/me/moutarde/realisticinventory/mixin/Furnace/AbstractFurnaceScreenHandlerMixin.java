package me.moutarde.realisticinventory.mixin.Furnace;

import me.moutarde.realisticinventory.mixin.ScreenHandlerInvoker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.moutarde.realisticinventory.Realistic_inventory.HOTBAR_SIZE;
import static me.moutarde.realisticinventory.Realistic_inventory.INVENTORY_SIZE;

@Mixin(AbstractFurnaceScreenHandler.class)
public class AbstractFurnaceScreenHandlerMixin {
    @Redirect(
            method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/recipe/book/RecipeBookCategory;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/screen/PropertyDelegate;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/AbstractFurnaceScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/screen/AbstractFurnaceScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;",
                            ordinal = 3
                    ),
                    to = @At(value = "TAIL")
            )
    )
    public Slot onAddSlot(AbstractFurnaceScreenHandler instance, Slot slot) {
        return slot;
    }

    @Inject(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/recipe/book/RecipeBookCategory;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/screen/PropertyDelegate;)V", at = @At(value = "TAIL"))
    public void injected(ScreenHandlerType<?> type, RecipeType<?> recipeType, RecipeBookCategory category, int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate, CallbackInfo ci) {
        int i;
        for(i = 0; i < INVENTORY_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, HOTBAR_SIZE + i, 8 + (i%9) * 18, 84 + (i/9) * 18));
        }

        final int offset = 18 * 4;
        for (i = 0; i < HOTBAR_SIZE; ++i) {
            ((ScreenHandlerInvoker) this).invokeAddSlot(new Slot(playerInventory, i, offset + 8 + i * 18, 142));
        }
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 39))
    public int injectedConstant(int value) {
        return 3 + INVENTORY_SIZE + HOTBAR_SIZE;
    }

    @ModifyConstant(method = "quickMove", constant = @Constant(intValue = 30))
    public int injectedConstant2(int value) {
        return 3 + INVENTORY_SIZE;
    }
}
