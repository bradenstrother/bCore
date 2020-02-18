package xyz.baqel.bcore.util.proxy.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.util.proxy.BungeeReader;
import xyz.baqel.bcore.util.proxy.channel.MessagingListener;

@NoArgsConstructor
public class UpdaterCountTask extends BukkitRunnable {

	@Getter private Plugin plugin;
	@Getter private MessagingListener messagingListener;
	@Getter private BungeeReader bungeeReader;
	
	private Long delay;
	private Long period;
	
	public UpdaterCountTask(Plugin plugin, Long delay, Long period) {
		this.plugin = plugin;
		
		this.delay = delay;
		this.period = period;
	}
	
	@Override
	public void run() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF("add servers");
		Bukkit.getServer().sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());
	}
	
	public void start() {
		this.runTaskTimerAsynchronously(this.plugin, this.delay, this.period);
	}
	
	public void stop() {
		this.cancel();
	}
}
