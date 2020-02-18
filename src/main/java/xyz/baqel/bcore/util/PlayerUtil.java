package xyz.baqel.bcore.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerUtil {
    public static Set<String> getConvertedUuidSet(final Set<UUID> uuids) {
        final Set<String> toReturn = new HashSet<>();
        for (final UUID uuid : uuids) {
            toReturn.add(uuid.toString());
        }
        return toReturn;
    }

    public static List<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
    }
}
