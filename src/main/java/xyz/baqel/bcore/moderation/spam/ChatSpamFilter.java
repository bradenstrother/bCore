package xyz.baqel.bcore.moderation.spam;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.baqel.bcore.bCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ChatSpamFilter {

    @Getter
    private Map<UUID, Map.Entry<Integer, Long>> data;

    @Getter
    private Map<UUID, Map.Entry<Integer, Long>> blocked;

    public ChatSpamFilter() {
        this.data = new HashMap<>();

        this.blocked = new HashMap<>();

        new BukkitRunnable() {

            @Override
            public void run() {
                ChatSpamFilter.this.data.clear();
            }
        }.runTaskTimer(bCore.getPlugin(), 100L, 100L);
    }

    public int getSecondsRemaining(Player player) {
        if (this.blocked.containsKey(player.getUniqueId())) {
            return (int)(this.blocked.get(player.getUniqueId()).getValue() + 60000L - System.currentTimeMillis()) / 1000;
        }
        return 0;
    }

    public Map<UUID, Map.Entry<Integer, Long>> getData() { return this.data; }

    public Map<UUID, Map.Entry<Integer, Long>> getBlocked() { return this.blocked; }
}
