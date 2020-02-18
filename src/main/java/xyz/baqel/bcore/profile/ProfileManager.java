package xyz.baqel.bcore.profile;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.profile.cached.UUIDCache;
import xyz.baqel.bcore.profile.grant.listener.GrantListener;
import xyz.baqel.bcore.profile.listener.ProfileListener;
import xyz.baqel.bcore.profile.punishment.listener.PunishmentListener;
import xyz.baqel.bcore.profile.rank.Rank;
import xyz.baqel.bcore.profile.staff.listener.StaffListener;
import xyz.baqel.bcore.profile.staff.listener.VanishListener;
import xyz.baqel.bcore.profile.staff.task.VanishUpdateTask;
import xyz.baqel.bcore.util.command.RegisterCommand;

@Getter
@NoArgsConstructor
public class ProfileManager {

 	@Getter 
 	private bCore plugin;
 	private PluginManager pluginManager;
 	@Getter 
 	private UUIDCache uuidCache;
 	@Getter 
 	private RegisterCommand registerCommand;
 	
 	public ProfileManager(bCore plugin) {
 		this.plugin = plugin;
 		this.pluginManager =  this.plugin.getServer().getPluginManager();
	}
 	
 	public void init() {
 		this.registerInits();
 		this.registetPreManagers();
 		this.regiserListeners();
 	}
 	
 	public void disable() {
 		//TODO dont remove this (public void).
 	}
 	
 	public void runnable() {
 		new BukkitRunnable() {
            
        	@Override
        	public void run() {
                for (Profile profile : Profile.getProfiles().values()) {
                    profile.checkGrants();
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 20L, 20L);
        
        new BukkitRunnable() {
			
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(players -> {
					Profile profile = Profile.getProfiles().get(players.getUniqueId());
					profile.setupPermissionsAttachment(ProfileManager.this.plugin, players);
				});
			}
		}.runTaskTimerAsynchronously(this.plugin, 20L, 20L);
		
		new VanishUpdateTask().runTask(this.plugin);
 	}
 	
 	private void registetPreManagers() {
 		this.uuidCache = new UUIDCache(this.plugin);
 		this.registerCommand = new RegisterCommand();
 	}
 	
 	private void regiserListeners() {
 		this.pluginManager.registerEvents(new ProfileListener(this.plugin), this.plugin);
 		this.pluginManager.registerEvents(new GrantListener(this.plugin), this.plugin);
 		this.pluginManager.registerEvents(new PunishmentListener(this.plugin), this.plugin);
 		this.pluginManager.registerEvents(new StaffListener(this.plugin), this.plugin);
 		this.pluginManager.registerEvents(new VanishListener(), this.plugin);
 	}

 	
 	private void registerInits() { Rank.init(); }
 	
 	public UUIDCache getUuidCache() { return uuidCache; }
}
