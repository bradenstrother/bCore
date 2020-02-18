package xyz.baqel.bcore.network;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.listener.*;
import xyz.baqel.bcore.network.packet.*;

@Getter
@NoArgsConstructor
public class NetworkManager {

	@Getter 
	private bCore plugin;
	
	public NetworkManager(bCore plugin) { this.plugin = plugin;
	}
	public void init() {
		this.registerPacketListeners();
		this.registerPacket();
	}
	
	private void registerPacket() {
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketAddGrant.class);
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketBroadcastPunishment.class);
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketDeleteGrant.class);
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketDeleteRank.class);
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketRefreshRank.class);
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketServerManage.class);
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketStaffChat.class);
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketRequest.class);
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketReport.class);
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerPacket(PacketCmdLog.class);
	}
	
	private void registerPacketListeners() {
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerListener(new BroadcastPunishListener(this.plugin));
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerListener(new GrantManageListener(this.plugin));
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerListener(new RankManageListener(this.plugin));
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerListener(new ServerManageListener(this.plugin));
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerListener(new StaffManageListener(this.plugin));
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerListener(new PlayerManageListener(this.plugin));
		this.plugin.getDatabaseManager().getRedisImpl().getRedis().registerListener(new CmdLogListener(this.plugin));

	}
}
