package xyz.baqel.bcore.profile.punishment.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.punishment.procedure.PunishmentProcedure;
import xyz.baqel.bcore.profile.punishment.procedure.PunishmentProcedureStage;
import xyz.baqel.bcore.profile.punishment.procedure.PunishmentProcedureType;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.menu.callback.TypeCallback;
import xyz.baqel.bcore.util.menu.menus.ConfirmMenu;

@Getter
@NoArgsConstructor
public class PunishmentListener implements Listener {
	
	@Getter private bCore plugin;
	
    public PunishmentListener(bCore plugin) { this.plugin = plugin; }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatAt(AsyncPlayerChatEvent event) {
    	Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if(profile == null) {
        	return;
        }
        if(profile.getActiveMute() != null) {
        	Chat.sendMessage(player, "&cYou are currently muted. Reason: &a" + profile.getActiveMute().getAddedReason() + "&c. Expires: &a" + profile.getActiveMute().getTimeRemaining() + "&a.");
        	event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
    	Player player = event.getPlayer();
        if (!player.hasPermission("cookie.staff.bypass")) {
            return;
        }
        PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(event.getPlayer());
        if (procedure != null && procedure.getStage() == PunishmentProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                PunishmentProcedure.getProcedures().remove(procedure);
                Chat.sendMessage(player, "&cYou have cancelled the punishment procedure.");
                return;
            }
            if (procedure.getType() == PunishmentProcedureType.PARDON) {
                new ConfirmMenu(Chat.GREEN + "Pardon this punishment?", new TypeCallback<Boolean>() {
                   
					private static final long serialVersionUID = -5035170385811433861L;

					@Override
                    public void callback(Boolean data) {
                        if (data) {
                            procedure.getPunishment().setPardonedBy(event.getPlayer().getUniqueId());
                            procedure.getPunishment().setPardonedAt(System.currentTimeMillis());
                            procedure.getPunishment().setPardonedReason(event.getMessage());
                            procedure.getPunishment().setPardoned(true);
                            
                            procedure.finish();
                            
                            Chat.sendMessage(player, "&aThe punishment has been pardoned.");
                        } else {
                        	Chat.sendMessage(player, "&cYou did not confirm to pardon the punishment.");
                        }
                    }
                }, true) {
                    
                	@Override
                    public void onClose(Player player) {
                        if (!this.isClosedByMenu()) {
                        	Chat.sendMessage(player, "&cYou did not confirm to pardon the punishment.");
                        }
                    }
                }.openMenu(event.getPlayer());
            }
        }
    }
}
