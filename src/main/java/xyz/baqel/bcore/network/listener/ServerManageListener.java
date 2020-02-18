package xyz.baqel.bcore.network.listener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.database.redis.handler.IncomingPacketHandler;
import xyz.baqel.bcore.database.redis.listener.PacketListener;
import xyz.baqel.bcore.network.packet.PacketServerManage;
import xyz.baqel.bcore.server.data.ServerData;
import xyz.baqel.bcore.server.state.ServerState;
import xyz.baqel.bcore.server.type.ServerType;

@Getter
@NoArgsConstructor
public class ServerManageListener implements PacketListener {

	@Getter private bCore plugin;
	
	public ServerManageListener(bCore plugin) {
		this.plugin = plugin;
	}
	
	@IncomingPacketHandler
	public void onServerManage(PacketServerManage packet) {
		ServerData serverData = this.plugin.getServerManager().getServerData().get(packet.getName());
		if(serverData == null) {
			serverData = new ServerData();
		}
		
		String name = packet.getName();
		serverData.setName(name);
		
		ServerType type;
		if(packet.getType() != null) {
			 type = ServerType.getServerTypeOfDefault(packet.getType().getName());
		} else {
			type = ServerType.OTHERS;
		}
		ServerState state;
		if(packet.getState() != null) {
			state = ServerState.getServerStateOrDefault(packet.getState().getName());
		} else {
			state = ServerState.OFFLINE;
		}
		serverData.setType(type);
		serverData.setState(state);
		
		int count = packet.getCount();
		serverData.setCount(count);
		
		int maxCount = packet.getMaxCount();
		serverData.setMaxCount(maxCount);
		
		boolean whitelisted = packet.isWhitelisted();
		serverData.setWhitelisted(whitelisted);
		
		long lastUpdate = packet.getLastUpdate();
		serverData.setLastUpdate(lastUpdate);
		
		this.plugin.getServerManager().getServerData().put(packet.getName(), serverData);
	}
}
