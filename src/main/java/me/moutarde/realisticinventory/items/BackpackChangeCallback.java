package me.moutarde.realisticinventory.items;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface BackpackChangeCallback {
    Event<BackpackChangeCallback> EVENT = EventFactory.createArrayBacked(BackpackChangeCallback.class,
        (listeners) -> (player, isAdded) -> {
            for (BackpackChangeCallback listener : listeners) {
                listener.onChange(player, isAdded);
            }
        });

    void onChange(PlayerEntity player, boolean isAdded);
}
