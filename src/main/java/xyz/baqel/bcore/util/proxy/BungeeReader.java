package xyz.baqel.bcore.util.proxy;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.baqel.bcore.util.BungeeUtil;
import xyz.baqel.bcore.util.proxy.channel.MessagingListener;
import xyz.baqel.bcore.util.proxy.enums.StateEnums;
import xyz.baqel.bcore.util.proxy.task.UpdaterCountTask;

@Getter
@RequiredArgsConstructor
public class BungeeReader {
	
	@Getter private Plugin plugin;
	@Getter private MessagingListener messagingListener;
	
	public Map<String, StateEnums> serverState;
	
	@Getter private UpdaterCountTask updaterCountTask;
	
	
	public BungeeReader(Plugin plugin) {
		this.plugin = plugin;
		this.messagingListener = new MessagingListener(this.plugin);
		
		this.serverState = new LinkedHashMap<>();
	}
	
	public void init() {
		this.registerChannelts();
		this.updatePlayerCount();
	}
	
	public void disable() {
		this.updaterCountTask.stop();
	}
	
	private void updatePlayerCount() {
		this.updaterCountTask = new UpdaterCountTask(this.plugin, 20L, 20L);
		this.updaterCountTask.start();
	}
	
	private void registerChannelts() {
		this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, "BungeeCord", new MessagingListener(this.plugin));
		this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
	}
	
//	public void send(Player target, String server) {
//		BungeeUtil.sendToServer(this.plugin, target, server);
//	}
	
	public Integer getCount(String server) {
		return this.messagingListener.playerCount.containsKey(server) ? this.messagingListener.playerCount.get(server) : 0;
	}
	
	public Integer getCountAll() {
		return this.messagingListener.playerCount.containsKey("ALL") ? this.messagingListener.playerCount.get("ALL") : 0;
	}
	
	public boolean isServerOnline(String server) {
		if(this.serverState.containsKey(server)) {
			return true;
		}
		return false;
	}
	
	public String getServerState(String server) {
		return this.serverState.containsKey(server) ? this.serverState.get(server).getName() : "ERROR";
	}

	public boolean checkServer(String server, String direccionIP, int port) {
		try {
			Socket s = new Socket(direccionIP, port);
			s.close();
			
			this.serverState.put(server, StateEnums.ONLINE);
			return true;
		}  catch (UnknownHostException e) {
			this.serverState.put(server, StateEnums.OFFLINE);
			return false;
		} catch (IOException e) {
			this.serverState.put(server, StateEnums.OFFLINE);
			return false;
		}
	}
}
