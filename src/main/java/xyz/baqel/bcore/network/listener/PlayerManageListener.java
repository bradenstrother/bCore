package xyz.baqel.bcore.network.listener;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.database.redis.handler.IncomingPacketHandler;
import xyz.baqel.bcore.database.redis.listener.PacketListener;
import xyz.baqel.bcore.network.packet.PacketReport;
import xyz.baqel.bcore.network.packet.PacketRequest;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;

@Getter
@NoArgsConstructor
public class PlayerManageListener implements PacketListener {

	@Getter
	private bCore plugin;
	
	public PlayerManageListener(bCore plugin) {
		this.plugin = plugin;
	}
	
	@IncomingPacketHandler
	public void onRequest(PacketRequest packet) {
		String senderName = packet.getSenderName();
		String message = packet.getMessage();
		String server = this.plugin.getRootConfig().getConfiguration().getString("server.id");
		Bukkit.getOnlinePlayers().forEach(players -> {
			Profile profile = Profile.getProfiles().get(players.getUniqueId());
			if(profile.isReceiveRequestEnabled() && players.hasPermission("bcore.staff")) {
				Chat.sendMessage(players, Chat.CHAT_BAR);
				Chat.sendMessage(players, "&5[Request] &r" + senderName + " &arequires assistance from &7" + server + "&a.");
				Chat.sendMessage(players, "&5[Request] &cReason: &b" + message);
				Chat.sendMessage(players, Chat.CHAT_BAR);

//				HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Chat.formatMessages("&cReason: &7" + message)));
//				BaseComponent[] baseComponents = new ComponentBuilder(Chat.formatMessages("&e[Request] &r" + senderName + " &arequires assistance from &7" + server + "")).event(hoverEvent).create();
//				players.sendMessage(String.valueOf(baseComponents));
			}
		});
	}
	
	@IncomingPacketHandler
	public void onReport(PacketReport packet) {
		String senderName = packet.getSenderName();
		String targetName = packet.getTargetName();
		String message = packet.getMessage();
		String server = this.plugin.getRootConfig().getConfiguration().getString("server.id");
		Bukkit.getOnlinePlayers().forEach(players -> {
			Profile profile = Profile.getProfiles().get(players.getUniqueId());
			if(profile.isReceiveReportEnabled() && players.hasPermission("bcore.staff")) {
				Chat.sendMessage(players, Chat.CHAT_BAR);
				Chat.sendMessage(players, "&c[Report] &r" + targetName + " &awas reported by &r" + senderName + " &afrom &7" + server + "&a.");
				Chat.sendMessage(players, "&c[Report] &cReason: &b" + message);
				Chat.sendMessage(players, Chat.CHAT_BAR);
//				HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Chat.formatMessages("&cReason: &7" + message)));
//				BaseComponent[] baseComponents = new ComponentBuilder(Chat.formatMessages("&c[Report] &r" + targetName + " &awas reported by &r" + senderName + " &afrom &7" + server + "")).event(hoverEvent).create();
//				players.sendMessage(String.valueOf(baseComponents));
			}
		});
	}
}
