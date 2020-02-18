package xyz.baqel.bcore.profile.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.ProfileAPI;
import xyz.baqel.bcore.profile.punishment.Punishment;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.chat.Chat.Level;

@Getter
@NoArgsConstructor
public class ProfileListener implements Listener {

	@Getter 
	private bCore plugin;
	
    public ProfileListener(bCore plugin) {
    	this.plugin = plugin;
    }
 
    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Player player = this.plugin.getServer().getPlayer(event.getUniqueId());
        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(Chat.formatMessages("&cYou tried to login too quickly after disconnecting.\n&cTry again in a few seconds."));
            
            this.plugin.getServer().getScheduler().runTask(this.plugin, () -> player.kickPlayer(Chat.formatMessages("&cDuplicate login kick")));
            return;
        }
        
        Profile profile = Profile.getByUuid(event.getUniqueId());
        try {
            if (!profile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Chat.formatMessages("&cAn error has occurred, when loading your profile"));
                return;
            }
            
            if (profile.getActiveBan() != null) {
                this.handleBan(event, profile.getActiveBan());
                return;
            }
            
            profile.setUsername(event.getName());
            
            if (profile.getFirstSeen() == null) {
                profile.setFirstSeen(System.currentTimeMillis());
            }
            
            profile.setLastSeen(System.currentTimeMillis());
            
            if (profile.getCurrentAddress() == null) {
                profile.setCurrentAddress(event.getAddress().getHostAddress());
            }
            profile.save();
        } catch (Exception e) {
        	Chat.log(Level.EXCEPTION, "&cFailed to load profile for " + event.getName());
        }
        
        if (profile == null || !profile.isLoaded()) {
        	event.setKickMessage(Chat.formatMessages("&cAn error has occurred, when loading your profile"));
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }
        
        Profile.getProfiles().put(profile.getUuid(), profile);
        this.plugin.getProfileManager().getUuidCache().update(event.getName(), event.getUniqueId());
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.setupPermissionsAttachment(this.plugin, event.getPlayer());
        profile.refreshDisplayName();
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	
    	Profile profile = Profile.getProfiles().remove(player.getUniqueId());
    	if(profile != null) {
    		profile.refreshDisplayName();
    		profile.setLastSeen(System.currentTimeMillis());
    		profile.setNickName(null);
    		
    		profile.setTag(profile.getTag());
    		profile.setColor(profile.getColor());
    		profile.setTagColor(profile.getTagColor());
    		
    		profile.save();
    	}
    	event.setQuitMessage(null);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChatAt(AsyncPlayerChatEvent event) {
    	event.setFormat(ProfileAPI.getPrefixedPlayer(event.getPlayer()) + Chat.GRAY + ": " + Chat.WHITE + "%2$s");
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent event) { event.setLeaveMessage(null); }
    
    private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        
        event.setKickMessage(punishment.getKickMessage());
    }
}
