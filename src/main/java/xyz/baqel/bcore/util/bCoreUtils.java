package xyz.baqel.bcore.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.bCoreConfig;
import xyz.baqel.bcore.util.chat.ColorText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class bCoreUtils {
    public static String STAFF_PERMISSION;
    public static String DONATOR_PERMISSION;
    public static String DONATOR_TOP_PERMISSION;
    public static String PERMISSION;
    public static String ONLY_PLAYERS;

    public static List<Player> getOnlineStaff() {
        final List<Player> players = new ArrayList<>();
        for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.hasPermission(bCoreUtils.STAFF_PERMISSION)) {
                players.add(online);
            }
        }
        return players;
    }

    public static List<Player> getOnlineOperators() {
        final List<Player> players = new ArrayList<>();
        for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.isOp()) {
                players.add(online);
            }
        }
        return players;
    }

    public static String getPlayerNotFoundMessage(final String name) {
        return ColorText.translate("&6Player with name '&f" + name + "&6' not found.");
    }

    public static boolean isOnline(final OfflinePlayer target) {
        return target != null && target.isOnline();
    }

    public static int getRandomNumber(final int random) {
        return new Random().nextInt(random);
    }

    public static List<String> getCompletions(final String[] args, final List<String> input) {
        return getCompletions(args, input, 80);
    }

    private static List<String> getCompletions(final String[] args, final List<String> input, final int limit) {
        Preconditions.checkNotNull((Object)args);
        Preconditions.checkArgument(args.length != 0);
        final String argument = args[args.length - 1];
        final String s = null;
        return input.stream().filter(string -> string.regionMatches(true, 0, s, 0, s.length())).limit(limit).collect(Collectors.toList());
    }

    static {
        bCoreUtils.STAFF_PERMISSION = bCoreConfig.getString("staff_permission");
        bCoreUtils.DONATOR_PERMISSION = bCoreConfig.getString("donator_permission");
        bCoreUtils.DONATOR_TOP_PERMISSION = bCoreConfig.getString("donator_top_permission");
        bCoreUtils.PERMISSION = "bCore.command.";
        bCoreUtils.ONLY_PLAYERS = bCoreConfig.getString("only_players");
    }
}
