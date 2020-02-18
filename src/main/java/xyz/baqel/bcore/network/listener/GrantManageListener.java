package xyz.baqel.bcore.network.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.database.redis.handler.IncomingPacketHandler;
import xyz.baqel.bcore.database.redis.listener.PacketListener;
import xyz.baqel.bcore.network.packet.PacketAddGrant;
import xyz.baqel.bcore.network.packet.PacketDeleteGrant;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.grant.Grant;
import xyz.baqel.bcore.profile.grant.event.GrantAppliedEvent;
import xyz.baqel.bcore.profile.grant.event.GrantExpireEvent;

@Getter
@NoArgsConstructor
public class GrantManageListener implements PacketListener {

	@Getter 
	private bCore plugin;
	
	public GrantManageListener(bCore plugin) {
		this.plugin = plugin;
	}
	
	@IncomingPacketHandler
    public void onAddGrant(PacketAddGrant packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        Grant grant = packet.getGrant();
        if(player == null) {
        	return;
        }
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getGrants().removeIf(other -> other.getUuid().equals(grant.getUuid()));
        profile.getGrants().add(grant);
        new GrantAppliedEvent(player, grant);
    }
	
	@IncomingPacketHandler
    public void onDeleteGrant(PacketDeleteGrant packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        Grant grant = packet.getGrant();
        if (player != null) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());    
            
            profile.getGrants().removeIf(other -> other.getUuid().equals(grant.getUuid()));
            new GrantExpireEvent(player, grant);
        }
    }
}
