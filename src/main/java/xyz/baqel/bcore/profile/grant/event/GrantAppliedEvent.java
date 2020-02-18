package xyz.baqel.bcore.profile.grant.event;

import org.bukkit.entity.Player;

import xyz.baqel.bcore.profile.grant.Grant;
import xyz.baqel.bcore.util.event.BaseEvent;

public class GrantAppliedEvent extends BaseEvent {
	
    private Player player;
    private Grant grant;
    
    public GrantAppliedEvent(Player player, Grant grant) {
        this.player = player;
        this.grant = grant;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Grant getGrant() {
        return this.grant;
    }
}
