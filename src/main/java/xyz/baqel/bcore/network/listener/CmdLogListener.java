package xyz.baqel.bcore.network.listener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.database.redis.handler.IncomingPacketHandler;
import xyz.baqel.bcore.database.redis.listener.PacketListener;
import xyz.baqel.bcore.network.packet.PacketCmdLog;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;

@Getter
@NoArgsConstructor
public class CmdLogListener implements PacketListener {

    @Getter
    private bCore plugin;

    public CmdLogListener(bCore plugin) { this.plugin = plugin; }

    @IncomingPacketHandler
    public void onChatAt(PacketCmdLog packet) {
        Bukkit.getOnlinePlayers().forEach(players -> {
            Profile profile = Profile.getProfiles().get(players.getUniqueId());
            if(players.hasPermission("bcore.staff.log")) {
                Chat.sendMessage(players, "&b[C] &7[" + packet.getServer() + "] &r" + packet.getSenderName() + "&f " + packet.getMessage());
            }
        });
    }
}
