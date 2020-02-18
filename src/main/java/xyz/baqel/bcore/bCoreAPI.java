package xyz.baqel.bcore;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.cosmetic.Tag;
import xyz.baqel.bcore.profile.rank.Rank;
import xyz.baqel.bcore.util.chat.ColorText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.UUID;

public class bCoreAPI {
    public static Profile getProfile(final Player player) {
        return getProfile(player.getUniqueId());
    }

    public static Profile getProfile(final UUID uuid) {
        Profile profile = Profile.getProfileMap().get(uuid);
        if (profile == null) {
            profile = new Profile(uuid);
        }
        return profile;
    }

    public static Rank getRank(final Player player) {
        return getProfile(player).getRank();
    }

    public static Rank getRankByUUID(final UUID uuid) {
        for (final Rank rank : Rank.getRanks()) {
            if (rank.getId().equals(uuid)) {
                return rank;
            }
        }
        return null;
    }

    public static Rank getRankByName(final String name) {
        for (final Rank rank : Rank.getRanks()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    public static Rank getDefaultRank() {
        for (final Rank rank : Rank.getRanks()) {
            if (rank.isDefaultRank()) {
                return rank;
            }
        }
        final Rank newRank = new Rank("User");
        newRank.setDefaultRank();
        newRank.save();
        return newRank;
    }

    public static int getPing(final Player player) {
        return ((CraftPlayer)player).getHandle().ping;
    }

    public static void sendToServer(final Player player, final String server) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ConnectOther");
        output.writeUTF(player.getName());
        output.writeUTF(server);
        player.sendPluginMessage(bCore.getPlugin(), "BungeeCord", output.toByteArray());
    }

    public static void broadcast(final String message, final String permission) {
        for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.hasPermission(permission)) {
                online.sendMessage(ColorText.translate(message));
            }
        }
    }

    public static void executeCommand(final String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static void sendToConsole(final String message) {
        Bukkit.getConsoleSender().sendMessage(ColorText.translate(message));
    }

    public static Location getSpawnPoint(final World world) {
        return world.getSpawnLocation();
    }

    public static boolean isLiked(final UUID uuid) {
        try {
            final URL url = new URL("https://api.namemc.com/server/" + bCoreConfig.getString("address") + "/likes?profile=" + uuid);
            final URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();
                if (line.contains("true")) {
                    return true;
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
        return false;
    }

    public static Cooldown getCooldown(final String name) {
        final Cooldown cooldown = Cooldown.getCooldownMap().get(name);
        return cooldown;
    }

    public static Cooldown getCooldownByPlayerID(final UUID uuid) {
        for (final Map.Entry<String, Cooldown> cooldown : Cooldown.getCooldownMap().entrySet()) {
            if (cooldown.getValue().getLongMap().containsKey(uuid)) {
                return cooldown.getValue();
            }
        }
        return null;
    }

    public static Tag getTagByName(final String name) {
        for (final Tag tag : Tag.getTags()) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }
}
