package me.moutarde.realisticinventory.mixin;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ScreenHandler.class)
public interface ScreenHandlerInvoker {
    @Invoker("addSlot")
    public Slot invokeAddSlot(Slot slot);
}
