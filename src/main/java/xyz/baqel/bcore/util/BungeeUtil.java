package xyz.baqel.bcore.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import xyz.baqel.bcore.util.chat.Chat;

public final class BungeeUtil {

	private BungeeUtil() {
		throw new RuntimeException("Cannot instantiate a utility class.");
	}

	public static void executeCommand(Plugin plugin, CommandSender sender, String[] message) {
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("DispatchCommand");
			out.writeUTF(sender.getName());
			out.writeUTF(StringUtils.join(message, ' ', 0, message.length));
			PluginMessageRecipient messageRecipient;
			if(sender instanceof PluginMessageRecipient) {
				messageRecipient = (PluginMessageRecipient) sender;
			} else {
				messageRecipient = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
				if(messageRecipient == null) {
					Chat.sendMessage(sender, "&cUnable to send plugin message, no players are online.");
					return;
				}
			}
			messageRecipient.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void kickPlayer(Plugin plugin, Player source, String target, String reason) {
//		Validate.notNull(source, target, reason, "Input values cannot be null!");
//
//		try {
//			ByteArrayDataOutput out = ByteStreams.newDataOutput();
//			out.writeUTF("KickPlayer");
//			out.writeUTF(target);
//			out.writeUTF(reason);
//
//			source.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public static void sendToServer(Plugin plugin, Player player, String server) {
//		Validate.notNull(player, server, "Input values cannot be null!");
//
//		try {
//			ByteArrayDataOutput out = ByteStreams.newDataOutput();
//			out.writeUTF("Connect");
//			out.writeUTF(server);
//
//			player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
