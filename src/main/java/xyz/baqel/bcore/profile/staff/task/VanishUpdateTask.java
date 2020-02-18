package xyz.baqel.bcore.profile.staff.task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.baqel.bcore.profile.Profile;

public class VanishUpdateTask extends BukkitRunnable {

	@Override
	public void run() {
		Bukkit.getOnlinePlayers().forEach(players -> {
			if(players.isOnline()) {
				Profile profile = Profile.getProfiles().get(players.getUniqueId());
				if(profile == null) {
					return;
				}
				profile.updateVanishedState(players, profile.isVanished());
			}
		});
	}
}
