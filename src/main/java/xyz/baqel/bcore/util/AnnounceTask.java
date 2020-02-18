package xyz.baqel.bcore.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.bCoreAPI;
import xyz.baqel.bcore.bCoreConfig;
import xyz.baqel.bcore.util.chat.MessageUtil;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AnnounceTask {
    private List<String> MESSAGES;

    public AnnounceTask() {
        this.MESSAGES = bCoreConfig.getArray("announce_messages");
        final Iterator<Player> iterator;
        AtomicReference<Player> online;
        TaskUtil.runTaskTimer(() -> {
            Bukkit.getServer().getOnlinePlayers().iterator();
            while (iterator.hasNext()) {
                online.set(iterator.next());
                if (!bCoreAPI.getProfile(online.get()).isTipsEnabled()) {
                    continue;
                }
                else {
                    online.get().sendMessage(new MessageUtil().setVariable("{0}", "\n").setVariable("%playername%", online.get().getName()).format(this.MESSAGES.get(bCoreUtils.getRandomNumber(this.MESSAGES.size()))));
                }
            }
        }, 20L, bCoreConfig.getInteger("announce_delay") * 20L);
    }
}
