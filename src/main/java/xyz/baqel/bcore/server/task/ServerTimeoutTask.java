package xyz.baqel.bcore.server.task;

import java.util.Iterator;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.RequiredArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.server.data.ServerData;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.chat.Chat.Level;

@RequiredArgsConstructor
public class ServerTimeoutTask extends BukkitRunnable {

    private static long TIME_OUT_DELAY = 15_000L;
    private bCore plugin;

    public ServerTimeoutTask(bCore plugin) {
		this.plugin = plugin;
	}
    
    @Override
    public void run() {
        Iterator<String> serverIterator = this.plugin.getServerManager().getServerData().keySet().iterator();

        while (serverIterator.hasNext()) {
            ServerData serverData = this.plugin.getServerManager().getServerData().get(serverIterator.next());

            if (serverData != null) {
                if (System.currentTimeMillis() - serverData.getLastUpdate() >= ServerTimeoutTask.TIME_OUT_DELAY) {
                    serverIterator.remove();
                    Chat.log(Level.INFO, "&cThe server &f" + serverData.getName() + "&c was removed due to it exceeding the timeout delay for heartbeats.");
                }
            }
        }
    }
}
