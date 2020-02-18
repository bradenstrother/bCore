package xyz.baqel.bcore.server;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketServerManage;
import xyz.baqel.bcore.server.data.ServerData;
import xyz.baqel.bcore.server.state.ServerState;
import xyz.baqel.bcore.server.task.ServerTimeoutTask;
import xyz.baqel.bcore.server.type.ServerType;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.chat.Chat.Level;

public class ServerManager {

	@Getter private bCore plugin;
	
	@Getter private ServerType serverType;
	@Getter private ServerState serverState;
	
	private String serverId;
	
	private Map<String, ServerData> serverData;
	
	public ServerManager(bCore plugin) {
		this.plugin = plugin;
		this.serverData = new LinkedHashMap<>();
	}
	
	public void init() {
		if(!this.plugin.getRootConfig().getConfiguration().contains("server.type")) {
			Chat.log(Level.EXCEPTION, "&cServer type not set in bcore config.");
			this.plugin.getServer().shutdown();
			return;
		}		
		this.serverType = ServerType.getServerTypeOfDefault(this.plugin.getRootConfig().getConfiguration().getString("server.type"));

		if(!this.plugin.getRootConfig().getConfiguration().contains("server.id")) {
			Chat.log(Level.EXCEPTION, "&cServer id not set in bCore config.");
			this.plugin.getServer().shutdown();
			return;
		}
		this.serverId = this.plugin.getRootConfig().getConfiguration().getString("server.id");
		this.serverState = ServerState.ONLINE;
	}
	
	public void disable() { this.serverState = ServerState.OFFLINE; }

	public void runnable() {
		new BukkitRunnable() {
			@Override
			public void run() {
				ServerManager.this.plugin.getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketServerManage(ServerManager.this.getServerId(), ServerManager.this.getServerType(), ServerManager.this.getServerState(), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers(), System.currentTimeMillis(), Bukkit.hasWhitelist()));
			}
		}.runTaskTimerAsynchronously(this.plugin, 20L, 20L);
		new ServerTimeoutTask(this.plugin).runTaskTimerAsynchronously(this.plugin, 20L, 20L);
	}

	public Map<String, ServerData> getServerData() { return serverData; }
	
	public ServerData getServerDataByName(String name) {
        if (this.getServerData().size() == 0) {
            return null;
        }
        for (String serverKey : this.getServerData().keySet()) {
            if (serverKey.equalsIgnoreCase(name)) {
                return this.getServerData().get(serverKey);
            } else {
                if (this.getServerData().get(serverKey).getName().equalsIgnoreCase(name)) {
                    return this.getServerData().get(serverKey);
                }
            }
        }
        return null;
    }
	
	public ServerType getServerType() { return serverType; }
	public void setServerType(ServerType serverType) { this.serverType = serverType; }

	public ServerState getServerState() { return serverState; }
	public void setServerState(ServerState serverState) { this.serverState = serverState; }

	public String getServerId() { return serverId; }
	
	public int getTotalPlayersOnline() {
        int count = 0;
        for (String serverKey : this.getServerData().keySet()) {
            if (this.getServerData().containsKey(serverKey)) {
                count += this.getServerData().get(serverKey).getCount();
            }
        }
        return count;
    }
}
