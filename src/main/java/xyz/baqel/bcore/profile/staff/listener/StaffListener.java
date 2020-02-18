package xyz.baqel.bcore.profile.staff.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketStaffChat;
import xyz.baqel.bcore.profile.Profile;

@Getter
@NoArgsConstructor
public class StaffListener implements Listener {

	@Getter 
	private bCore plugin;
	
	public StaffListener(bCore plugin) { this.plugin = plugin; }
	
	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		if(player.hasPermission("bcore.staff.bypass")) {
			profile.setStaffChatEnabled(false);
			profile.setReceiveReportEnabled(true);
			profile.setReceiveRequestEnabled(true);
			profile.setVanished(false);
		} else {
			profile.setStaffChatEnabled(false);
			profile.setReceiveReportEnabled(false);
			profile.setReceiveRequestEnabled(false);
			profile.setVanished(false);
		}
	}
	
	@EventHandler
    public void onChatStaff(AsyncPlayerChatEvent event) {
    	Player player = event.getPlayer();
    	Profile profile = Profile.getProfiles().get(player.getUniqueId());
    	String message = event.getMessage();
    	String server = this.plugin.getRootConfig().getConfiguration().getString("server.id");
    	if(profile.isStaffChatEnabled() && player.hasPermission("bcore.staff.bypass")) {
        	this.plugin.getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketStaffChat(server, profile.getColoredUsername(), message));
        	event.setCancelled(true);
    	}
    }
}
