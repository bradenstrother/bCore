package xyz.baqel.bcore.network.listener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.database.redis.handler.IncomingPacketHandler;
import xyz.baqel.bcore.database.redis.listener.PacketListener;
import xyz.baqel.bcore.network.packet.PacketDeleteRank;
import xyz.baqel.bcore.network.packet.PacketRefreshRank;
import xyz.baqel.bcore.profile.rank.Rank;

@Getter
@NoArgsConstructor
public class RankManageListener implements PacketListener {

	@Getter private bCore plugin;
	
	public RankManageListener(bCore plugin) {
		this.plugin = plugin;
	}
	
	@IncomingPacketHandler
    public void onRankRefresh(PacketRefreshRank packet) {
        Rank rank = Rank.getRankByUuid(packet.getUuid());
        if (rank == null) {
            rank = new Rank(packet.getUuid(), packet.getName());
        } 
        rank.load();
    }
    
    @IncomingPacketHandler
    public void onRankDelete(PacketDeleteRank packet) {
        Rank.getRanks().remove(packet.getUuid());
    }
}
