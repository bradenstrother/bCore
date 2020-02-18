package xyz.baqel.bcore.util.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import xyz.baqel.bcore.bCore;

public class BaseEvent extends Event {
	
    private static final HandlerList handlers;
    
    public HandlerList getHandlers() {
        return BaseEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return BaseEvent.handlers;
    }
    
    public void call() {
        bCore.getPlugin().getServer().getPluginManager().callEvent(this);
    }
    
    static {
        handlers = new HandlerList();
    }
}
