package xyz.baqel.bcore.util.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import xyz.baqel.bcore.util.chat.Chat;

public class RegisterCommand {

    public void registerBukkitCommand(CustomCommand customCommand, Plugin plugin) {
        this.registerBukkitCommand(customCommand, customCommand.getName(), customCommand.getAliases(), plugin);
    }

    public void registerBukkitCommand(CustomCommand customCommand, String name, String[] aliases, Plugin plugin) {
        PluginCommand command = this.getCommand(name, plugin);
        if(aliases != null) {
            command.setAliases(Arrays.asList(aliases));
        }

        if(customCommand.getDescription() != null) {
            command.setDescription(customCommand.getDescription());
        }

        command.setPermissionMessage(Chat.formatMessages("&cI'm sorry but you do not have permission to execute this command, contact to administrator."));

        command.setExecutor(customCommand);
        command.setTabCompleter(customCommand);
        this.getCommandMap().register(plugin.getDescription().getName(), command);

    }

    public void unregisterBukkitCommand(CustomCommand customCommand, Plugin plugin) {
        this.unregisterBukkitCommand(customCommand.getName(), plugin);
    }

    private void unregisterBukkitCommand(String name, Plugin plugin) {
        PluginCommand command = this.getCommand(name, plugin);
        command.unregister(getCommandMap());
    }

    private PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            command = constructor.newInstance(name, plugin);
        } catch (SecurityException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return command;
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap)f.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }
        return commandMap;
    }
}
