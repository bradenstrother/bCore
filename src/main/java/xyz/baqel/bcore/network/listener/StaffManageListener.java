package xyz.baqel.bcore.network.listener;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.database.redis.handler.IncomingPacketHandler;
import xyz.baqel.bcore.database.redis.listener.PacketListener;
import xyz.baqel.bcore.network.packet.PacketStaffChat;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;

@Getter
@NoArgsConstructor
public class StaffManageListener implements PacketListener {

	@Getter 
	private bCore plugin;
	
	public StaffManageListener(bCore plugin) {
		this.plugin = plugin;
	}
	
	@IncomingPacketHandler
	public void onChatAt(PacketStaffChat packet) {
		Bukkit.getOnlinePlayers().forEach(players -> {
			Profile profile = Profile.getProfiles().get(players.getUniqueId());
			if(players.hasPermission("bcore.staff")) {
				Chat.sendMessage(players, "&3[S] &7[" + packet.getServer() + "] &r" + packet.getStaffName() + "&f: " + packet.getMessage());
			}
		});
	}
}
