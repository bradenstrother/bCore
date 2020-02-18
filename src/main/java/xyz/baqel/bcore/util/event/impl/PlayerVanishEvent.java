package xyz.baqel.bcore.util.event.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.Collection;

public class PlayerVanishEvent extends PlayerEvent implements Cancellable {
    private static HandlerList handlers;

    public static HandlerList getHandlerList() {
        return PlayerVanishEvent.handlers;
    }

    static {
        handlers = new HandlerList();
    }

    private boolean vanished;
    private Collection viewers;
    private boolean cancelled;

    public PlayerVanishEvent(Player player, Collection viewers, boolean vanished) {
        super(player);
        this.viewers = viewers;
        this.vanished = vanished;
    }

    public Collection getViewers() {
        return this.viewers;
    }

    public boolean isVanished() {
        return this.vanished;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return PlayerVanishEvent.handlers;
    }
}
