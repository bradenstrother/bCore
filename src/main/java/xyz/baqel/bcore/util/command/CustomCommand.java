package xyz.baqel.bcore.util.command;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.ClickableUtil;

import java.util.Arrays;
import java.util.List;

public abstract class CustomCommand implements CommandExecutor, TabCompleter {

    private String name;
    private String description;
    private String[] aliases;

    private String usage;
    private String permission;

    private String player;
    private String subCommand;
    private String argument;

    public CustomCommand(String name) {
        this(name, null, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public CustomCommand(String name, String description) {
        this(name, description, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public CustomCommand(String name, String description, String[] aliases) {
        this(name, description, aliases, null, null);
    }

    public CustomCommand(String name, String description, String[] aliases, String usage, String permission) {
        this.name = name;
        this.description = description;
        this.aliases = Arrays.copyOf(aliases, aliases.length);

        this.usage = usage;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(this.getPermission() != null) {
            if(!sender.hasPermission(this.getPermission())) {
                Chat.sendMessage(sender, command.getPermissionMessage());
                return true;
            }
        }
        try {
            ResultCommand result = execute(sender, command, label, args);
            if(result == ResultCommand.INVALID_USAGE) {
                this.sendActionsUsage(sender, this.getUsage());
            } else if(result == ResultCommand.INVALID_PERMISSION) {
                Chat.sendMessage(sender, command.getPermissionMessage());
            } else if(result == ResultCommand.CONSOLE_ONLY) {
                Chat.sendMessage(sender, "&cThis command can only be executed from the console.");
            } else if(result == ResultCommand.CRITICAL_ERROR) {
                Chat.sendMessage(sender, "&cOops... An error occurred whilst typing that command, please report this to an admin.");
            } else if(result == ResultCommand.INVALID_PLAYER) {
                Chat.sendMessage(sender, "&cPlayer named '" + this.getPlayer() + "' could not be found.");
            } else if(result == ResultCommand.INVALID_SUBCOMMAND) {
                Chat.sendMessage(sender, "&cInvalid sub-command '" + this.getSubCommand() + "' could not be found.");
            } else if(result == ResultCommand.INVALID_ARGUMENT) {
                Chat.sendMessage(sender, "&cInvalid argument '" + this.getArgument() + "' could not be found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Chat.sendMessage(sender, "&cOops... An error occurred whilst typing that command, please report this to an admin.");
        }
        return true;
    }

    public abstract ResultCommand execute(CommandSender sender, Command command, String label, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Chat.formatMessages(description);
    }

    public void setDescription(String description) {
        this.description = Chat.formatMessages(description);
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public String getUsage() {
        return Chat.formatMessages(usage);
    }

    public void sendActionsUsage(CommandSender sender, String value) {
        ClickableUtil clickable = new ClickableUtil("&cInvalid usage: /" + value, "&r" + this.description, '/' + value);
        if(sender instanceof Player) {
            clickable.sendToPlayer((Player) sender);
        } else {
            Chat.sendMessage(sender, "&cInvalid usage: /" + value + " &7- &f" + this.description);
        }
    }

    public void setUsage(String usage) {
        this.usage = Chat.formatMessages(usage);
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Player could not be found.
     *
     * @return player - player not found.
     * */
    public String getPlayer() {
        return player;
    }

    /**
     * Set text to player not found.
     *
     * @param player (0) = player;
     * @return player the player.
     * */
    public void setPlayer(String player) {
        this.player = player;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getSubCommand() {
        return subCommand;
    }

    public void setSubCommand(String subCommand) {
        this.subCommand = subCommand;
    }

    public boolean isConsole(CommandSender sender) {
        if(!(sender instanceof Player)) {
            return true;
        }
        return false;
    }

    public boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }
}