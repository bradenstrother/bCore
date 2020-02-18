package xyz.baqel.bcore.network.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.database.redis.handler.IncomingPacketHandler;
import xyz.baqel.bcore.database.redis.listener.PacketListener;
import xyz.baqel.bcore.network.packet.PacketBroadcastPunishment;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.punishment.Punishment;

@Getter
@NoArgsConstructor
public class BroadcastPunishListener implements PacketListener {

	@Getter 
	private bCore plugin;
	
	public BroadcastPunishListener(bCore plugin) { this.plugin = plugin; }
	
	@IncomingPacketHandler
    public void onBroadcastPunishment(PacketBroadcastPunishment packet) {
        Punishment punishment = packet.getPunishment();
        punishment.broadcast(packet.getStaff(), packet.getTarget(), packet.isSilent());
        Player player = Bukkit.getPlayer(packet.getTargetUuid());
        if(player == null) {
        	return;
        }
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if(profile == null) {
        	return;
        }
        profile.getPunishments().removeIf(other -> other.getUuid().equals(punishment.getUuid())); 
        profile.getPunishments().add(punishment);
        if (punishment.getType().isBan()) {
            new BukkitRunnable() {
            	@Override
            	public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(this.plugin);
        }
    }
}
