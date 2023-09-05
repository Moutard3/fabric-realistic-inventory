package me.moutarde.realisticinventory.mixin.Hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Accessor("client")
    MinecraftClient getClient();

    @Accessor("ICONS")
    Identifier getICONS();

}
