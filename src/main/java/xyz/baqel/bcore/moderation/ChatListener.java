package xyz.baqel.bcore.moderation;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.moderation.modifier.type.ChatMuteModifier;
import xyz.baqel.bcore.moderation.modifier.type.ChatSpeedModifier;
import xyz.baqel.bcore.moderation.spam.ChatSpamFilter;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.DateUtil;

public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChatAt(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        xyz.baqel.bcore.moderation.Chat chat = bCore.getPlugin().getChat();
        if (chat != null) {
            if (chat.getByClass(ChatMuteModifier.class) != null && !player.hasPermission("bcore.staff.bypass")) {
                ChatMuteModifier modifier = (ChatMuteModifier)chat.getByClass(ChatMuteModifier.class);
                if (modifier.getDuration() != Long.MAX_VALUE) {
                    if (modifier.getCreatedAt() + modifier.getDuration() - System.currentTimeMillis() <= 0L) {
                        chat.getModifiers().remove(modifier);
                        return;
                    }
                    Chat.sendMessage(player, "&cThe chat is currently disabled for " + DateUtil.readableTime(modifier.getCreatedAt() + modifier.getDuration() - System.currentTimeMillis()).trim() + ".");
                } else {
                    Chat.sendMessage(player, "&cThe chat is currently disabled.");
                }
                event.setCancelled(true);
                return;
            }
            ChatSpeedModifier modifier = (ChatSpeedModifier)chat.getByClass(ChatSpeedModifier.class);
            if (modifier != null && !player.hasPermission("bcore.staff.bypass")) {
                if (!modifier.isAllowed(player)) {
                    event.setCancelled(true);
                    Chat.sendMessage(player, "&cYou must wait " + modifier.getFormattedTimeLeft(player) + " seconds before sending another message.");
                    return;
                }
                modifier.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
            }
        }
    }

//    @EventHandler (priority = EventPriority.HIGH)
//    public void onChatFilter(AsyncPlayerChatEvent event) {
//        Player player = event.getPlayer();
//        Chat chat = bCore.getPlugin().getChat();
//        ChatSpamFilter filter = chat.getFilter();
//        if (!player.hasPermission("bcore.staff.bypass")) {
//            if (filter.getBlocked().containsKey(player.getUniqueId())) {
//                if (System.currentTimeMillis() - filter.getBlocked().get(player.getUniqueId()).getValue() <= 60000L) {
//                    if (filter.getBlocked().get(player.getUniqueId()).getKey() >= 10) {
//                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + player.getName() + " 10m Excessive spam");
//                        event.setCancelled(true);
//                        return;
//                    }
//                    Chat.sendMessage(player, "&cYou are currently silenced for spam &l" + filter.getSecondsRemaining(player) + "&c seconds remaining.");
//                    Chat.sendMessage(player, "&cIf you continue to use public chat while silenced, you will be banned.");
//                    Chat.sendMessage(player, "&cYou have " + (10 - filter.getBlocked().get(player.getUniqueId()).getKey()) + " warnings reamining.");
//                    filter.getBlocked().put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(filter.getBlocked().get(player.getUniqueId()).getKey() + 1, filter.getBlocked().get(player.getUniqueId()).getValue()));
//                    event.setCancelled(true);
//                    return;
//                } else {
//                    filter.getBlocked().remove(player.getUniqueId());
//                }
//            }
//
//            if (filter.getData().containsKey(player.getUniqueId())) {
//                if (filter.getData().get(player.getUniqueId()).getKey() >= 3 && System.currentTimeMillis() - filter.getData().get(player.getUniqueId()).getValue() <= 4000L) {
//                    Chat.sendMessage(player, "&cYou are currently silenced for spam &l60 &cseconds remaining.");
//                    Chat.sendMessage(player, "&cIf you continue to use public chat while silenced, you will be banned.");
//                    Chat.sendMessage(player, "&cYou have 10 warnings remaining.");
//                    filter.getBlocked().put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(1, System.currentTimeMillis()));
//                    event.setCancelled(true);
//                    return;
//                }
//                filter.getData().put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(filter.getData().get(player.getUniqueId()).getKey() + 1, filter.getData().get(player.getUniqueId()).getValue()));
//            } else {
//                filter.getData().put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(1, System.currentTimeMillis()));
//            }
//        }
//    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onDisconnect(PlayerQuitEvent event) {
        xyz.baqel.bcore.moderation.Chat chat = bCore.getPlugin().getChat();
        ChatSpeedModifier modifier = (ChatSpeedModifier)chat.getByClass(ChatSpeedModifier.class);
        if (modifier != null) {
            modifier.getCooldowns().remove(event.getPlayer().getUniqueId());
        }
        ChatSpamFilter filter = chat.getFilter();
        filter.getData().remove(event.getPlayer().getUniqueId());
        filter.getBlocked().remove(event.getPlayer().getUniqueId());
    }
}
