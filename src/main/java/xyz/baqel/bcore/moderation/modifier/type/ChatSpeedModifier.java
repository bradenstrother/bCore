package xyz.baqel.bcore.moderation.modifier.type;

import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.moderation.modifier.ChatModifier;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ChatSpeedModifier implements ChatModifier {
    @Getter
    private static DecimalFormat SECONDS_FORMATTER;

    @Getter
    private Map<UUID, Long> cooldowns;

    @Getter
    private long createdAt;

    @Getter
    private long interval;

    public ChatSpeedModifier(long interval) {
        SECONDS_FORMATTER = new DecimalFormat("#0.0");

        this.interval = interval;

        this.createdAt = System.currentTimeMillis();

        this.cooldowns = new HashMap<>();
    }

    public boolean isAllowed(Player player) { return this.getTimeLeft(player) <= 0.0f; }

    public String getFormattedTimeLeft(Player player) {
        if (this.isAllowed(player)) {
            return "now";
        }
        return ChatSpeedModifier.SECONDS_FORMATTER.format(this.getTimeLeft(player));
    }

    private float getTimeLeft(Player player) {
        if (this.cooldowns.containsKey(player.getUniqueId())) {
            return (this.cooldowns.get(player.getUniqueId()) + this.interval - System.currentTimeMillis()) / 1000.0f;
        }
        return 0.0f;
    }

    public Map<UUID, Long> getCooldowns() { return this.cooldowns; }

    public long getCreatedAt() { return this.createdAt; }

    public long getInterval() { return this.interval; }
}
