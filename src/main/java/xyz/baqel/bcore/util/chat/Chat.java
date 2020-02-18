package xyz.baqel.bcore.util.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketCmdLog;

public class Chat {

	public static String BLUE;
    public static String AQUA;
    public static String YELLOW;
    public static String RED;
    public static String GRAY;
    public static String GOLD;
    public static String GREEN;
    public static String WHITE;
    public static String BLACK;
    public static String BOLD;
    public static String ITALIC;
    public static String UNDER_LINE;
    public static String STRIKE_THROUGH;
    public static String RESET;
    public static String MAGIC;
    public static String DARK_BLUE;
    public static String DARK_AQUA;
    public static String DARK_GRAY;
    public static String DARK_GREEN;
    public static String DARK_PURPLE;
    public static String DARK_RED;
    public static String PINK;
    public static String MENU_BAR;
    public static String CHAT_BAR;
    public static String SB_BAR;
    
    static {
        BLUE = ChatColor.BLUE.toString();
        AQUA = ChatColor.AQUA.toString();
        YELLOW = ChatColor.YELLOW.toString();
        RED = ChatColor.RED.toString();
        GRAY = ChatColor.GRAY.toString();
        GOLD = ChatColor.GOLD.toString();
        GREEN = ChatColor.GREEN.toString();
        WHITE = ChatColor.WHITE.toString();
        BLACK = ChatColor.BLACK.toString();
        BOLD = ChatColor.BOLD.toString();
        ITALIC = ChatColor.ITALIC.toString();
        UNDER_LINE = ChatColor.UNDERLINE.toString();
        STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
        RESET = ChatColor.RESET.toString();
        MAGIC = ChatColor.MAGIC.toString();
        DARK_BLUE = ChatColor.DARK_BLUE.toString();
        DARK_AQUA = ChatColor.DARK_AQUA.toString();
        DARK_GRAY = ChatColor.DARK_GRAY.toString();
        DARK_GREEN = ChatColor.DARK_GREEN.toString();
        DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
        DARK_RED = ChatColor.DARK_RED.toString();
        PINK = ChatColor.LIGHT_PURPLE.toString();
        MENU_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------";
        CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------";
        SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "----------------------";
    }
    
    public static net.md_5.bungee.api.ChatColor fromBukkit(ChatColor chatColor){
        return net.md_5.bungee.api.ChatColor.values()[chatColor.ordinal()];
    }
	
	/**
     * Translates the '&' character into its respective colour code.
     *
     * @param message - The original string to be converted.
     * @return string - Translated with Colour Codes
     */
    public static String formatMessages(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static List<String> formatMessages(List<String> messages) {
        List<String> buffered = new ArrayList<>();
        for (String message : messages){
            buffered.add(formatMessages("&r" + message));
        }
        return buffered;
    }
    
    public static String[] formatMessages(String[] messages) {
    	return formatMessages(Arrays.asList(messages)).stream().toArray(String[]::new);
    }
    
    /**
     * Translates the '&' character into its respective colour code.
     * 
     * @param sender - Send message string to be converted.
     * @return string - Translated with Colour Codes.
     * */
    public static void sendMessage(CommandSender sender, String... messages) {
    	for(String message : messages) {
    		sender.sendMessage(formatMessages(message));
    	}
    }
    
    public static void sendMessage(Player player, String... messages) {
    	for(String message : messages) {
    		player.sendMessage(formatMessages(message));
    	}
    }
    
    public static void sendMessage(CommandSender sender, BaseComponent component) {
    	if(sender instanceof Player) {
    		((Player) sender).sendMessage(String.valueOf(component));
    	} else {
    		sender.sendMessage(TextComponent.toPlainText(component));
    	}
    }
    
    /**
     * Translate the '&' character into its recpective colour code.
     * 
     * @param message - Send message to all players, string to be converted.
     * @return string - Translated with Colour Codes.
     * */
    public static void broadcastMessage(String message) {
    	Bukkit.getOnlinePlayers().forEach(players -> {
			players.sendMessage(formatMessages(message));
		});     
    }
    
    /**
     * Translate the '&' character into its recpective colour code.
     * 
     * @param messages - Send message to all players, string to be converted.
     * @permission permission - Only the player who has the permission can receive the message.
     * @return string - Translated with Colour Codes.
     * */
    public static void broadcastMessage(String permission, String... messages) {
    	for(String message  : messages) {
    		Bukkit.getOnlinePlayers().forEach(players -> {
    			if(players.hasPermission(permission)) {
    				players.sendMessage(formatMessages(message));
    			}
    		});     		
    	}
    }

    public static void cmdLogger(Player player, String... messages) {
        for (String message : messages) {
            if (player.hasPermission("bcore.staff.log")) {
                xyz.baqel.bcore.profile.Profile profile = xyz.baqel.bcore.profile.Profile.getProfiles().get(player.getUniqueId());
                String server = bCore.getPlugin().getRootConfig().getConfiguration().getString("server.id");
                bCore.getPlugin().getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketCmdLog(server, profile.getColoredUsername(), message));
            }
        }
    }
    
    /**
     * 
     * @param level - Send message levels to console.
     * 
     * */
    public static void log(Level level, String... messages) {
    	for(String message : messages) {
    		ConsoleCommandSender console = Bukkit.getConsoleSender();
    		
    		String prefix = "&c[&4Log&c] &7[" + level.getColor().toString() +  level.getName() + "&7] &r";
    		
    		switch (level) {
    			case CONSOLE: {
    				console.sendMessage(formatMessages(prefix + message));
    				break;
    			}
    			case INFO: {
    				console.sendMessage(formatMessages(prefix + message));
    				break;
    			}
    			case WARN: {
    				console.sendMessage(formatMessages(prefix + message));
    				break;
    			}
    			case ERROR: {
    				console.sendMessage(formatMessages(prefix + message));
    				break;
    			}
    			case EXCEPTION: {
    				console.sendMessage(formatMessages(prefix + message));
    			}
    			default:
    				break;
    		}
    	}
    }
    
    public enum Level {
    	CONSOLE("Console", ChatColor.WHITE, 0),
    	INFO("Info", ChatColor.GREEN, 1),
    	WARN("Warning", ChatColor.GOLD, 2),
    	ERROR("Error", ChatColor.RED, 3),
    	EXCEPTION("Exception", ChatColor.DARK_RED, 4);
    	
    	private String name;
    	private ChatColor color;
    	private int level;
    	
    	private Level(String name, ChatColor color, int level) {
			this.name = name;
			this.color = color;
			this.level = level;
		}
    	
    	public String getName() { return name; }
    	
    	public ChatColor getColor() { return color; }
    	
    	public int getLevel() { return level; }
    }
}
