package xyz.baqel.bcore.server.state;

import org.bukkit.ChatColor;

public enum ServerState {

	ONLINE("Online", ChatColor.GREEN),
	OFFLINE("Offline", ChatColor.RED),
	DEVELOPMENT("Development", ChatColor.AQUA),
	WHITELISTED("Whitelisted", ChatColor.WHITE);
	
	private String name;
	private ChatColor color;
	
	private ServerState(String name, ChatColor color) {
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public static ServerState getServerStateOrDefault(String stateName) {
		ServerState state;
		try {
			state = ServerState.valueOf(stateName.toUpperCase());
		} catch (Exception e) {
			state = ServerState.OFFLINE;
		}
		return state;
	}
	
	public ChatColor getColor() {
		return color;
	}
}
