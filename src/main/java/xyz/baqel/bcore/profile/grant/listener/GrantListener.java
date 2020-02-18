package xyz.baqel.bcore.profile.grant.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketDeleteGrant;
import xyz.baqel.bcore.profile.grant.Grant;
import xyz.baqel.bcore.profile.grant.event.GrantAppliedEvent;
import xyz.baqel.bcore.profile.grant.event.GrantExpireEvent;
import xyz.baqel.bcore.profile.grant.procedure.GrantProcedure;
import xyz.baqel.bcore.profile.grant.procedure.GrantProcedureStage;
import xyz.baqel.bcore.profile.grant.procedure.GrantProcedureType;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.TimeUtil;
import xyz.baqel.bcore.util.menu.callback.TypeCallback;
import xyz.baqel.bcore.util.menu.menus.ConfirmMenu;

@Getter
@NoArgsConstructor
public class GrantListener implements Listener {
	
	@Getter private bCore plugin;
	
    public GrantListener(bCore plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onGrantAppliedEvent(GrantAppliedEvent event) {
        Player player = event.getPlayer();
        
        Grant grant = event.getGrant();
        Chat.sendMessage(player, "&aYou have been applied in grant &r" + grant.getRank().getColoredName() + "&a for &5" + (grant.getDuration() == 2147483647L ? "forever" : TimeUtil.millisToRoundedTime(System.currentTimeMillis() - grant.getAddedAt() + grant.getDuration())));
    }
    
    @EventHandler
    public void onGrantExpireEvent(GrantExpireEvent event) {
        Player player = event.getPlayer();
        
        Grant grant = event.getGrant();
        
        Chat.sendMessage(player, "&cYour &r" + grant.getRank().getColoredName() + "&c grant has expired.");
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
    	Player player = event.getPlayer();
        if (!player.hasPermission("bcore.staff.grant")) {
            return;
        }
        
        GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());
        if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);
            
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                GrantProcedure.getProcedures().remove(procedure);
                Chat.sendMessage(player, "&cYou have cancelled the grant procedure.");
                return;
            }
            
            if (procedure.getType() == GrantProcedureType.REMOVE) {
                new ConfirmMenu(Chat.RED + "Delete this grant?", new TypeCallback<Boolean>() {
                    
					private static final long serialVersionUID = -988701755050783420L;

					@Override
                    public void callback(Boolean data) {
                        if (data) {
                            procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
                            procedure.getGrant().setRemovedAt(System.currentTimeMillis());
                            procedure.getGrant().setRemovedReason(event.getMessage());
                            procedure.getGrant().setRemoved(true);
                            
                            procedure.finish();
                            
                            if(procedure.getRecipient().getPlayer() != null) {
                            	procedure.getRecipient().refreshDisplayName();
                            	procedure.getRecipient().getPlayer().kickPlayer(Chat.formatMessages("&cYou have removed the rank " + procedure.getGrant().getRank().getDisplayName() + ", please re-join the server."));
                            }
                            
                            Chat.sendMessage(player, "&cThe grant has been removed.");
                            
                            GrantListener.this.plugin.getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketDeleteGrant(procedure.getRecipient().getUuid(), procedure.getGrant()));
                        } else {
                            procedure.cancel();
                            Chat.sendMessage(player, "&cYou did not confirm to remove the grant.");
                        }
                    }
                }, true) {
                    
                	@Override
                    public void onClose(Player player) {
                        if (!this.isClosedByMenu()) {
                            procedure.cancel();
                            
                            Chat.sendMessage(player, "&cYou did not confirm to remove the grant.");
                        }
                    }
                }.openMenu(event.getPlayer());
            }
        }
    }
}
