package me.moutarde.realisticinventory.mixin.Hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Accessor("client")
    MinecraftClient getClient();

    @Accessor("ICONS")
    Identifier getICONS();

}
