package xyz.baqel.bcore.manager.command.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.server.ServerManager;
import xyz.baqel.bcore.server.data.ServerData;
import xyz.baqel.bcore.server.state.ServerState;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class ServerStatusCommand extends CustomCommand {

	public ServerStatusCommand() {
		super("serverstatus", "Manage a servers.", new String[] {"status"}, "serverstatus <info|setstate> <server> <state>", "bcore.cmd.serverstatus");
	}

	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(!this.hasPermission(sender, "bcore.cmd.serverstatus")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		if(args.length == 0) {
			Chat.sendMessage(sender,
					Chat.CHAT_BAR,
					"&cServer Information &7- &cCommands.",
					"&b/" + label + " info <server>",
					"&b/" + label + " setstate <server> <state>",
					Chat.CHAT_BAR);
			return ResultCommand.INVALID_USAGE;
		}
		if(args[0].equalsIgnoreCase("info")) {
			if(args.length != 2) {
				this.setUsage(label + " info <server>");
				return ResultCommand.INVALID_USAGE;
			}
			ServerManager serverManager = bCore.getPlugin().getServerManager();
			ServerData serverData = serverManager.getServerDataByName(args[1]);
			if(serverData == null) {
				Chat.sendMessage(sender, "&cServer '" + args[1] + "' could be found.");
				return ResultCommand.SUCCESS;
			}
			Chat.sendMessage(sender,
					Chat.CHAT_BAR,
					"&9Information of &f" + serverData.getName(),
					"&bPlayers&7: &f" + serverData.getCount(),
					"&bMaxPlayers&7: &f" + serverData.getMaxCount(),
					"&bType&7: &f" + serverData.getType().getName(),
					"&bState&7: &f" + serverData.getState().getName(),
					"&bWhitelisted&7: &f" + serverData.isWhitelisted(),
					Chat.CHAT_BAR);
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("setstate")) {
			if(args.length != 3) {
				this.setUsage(label + " setstate <server> <state>");
				return ResultCommand.INVALID_USAGE;
			}
			ServerManager serverManager = bCore.getPlugin().getServerManager();
			ServerData serverData = serverManager.getServerDataByName(args[1]);
			if(serverData == null) {
				Chat.sendMessage(sender, "&cServer '" + args[1] + "' could be found.");
				return ResultCommand.SUCCESS;
			}
			ServerState state = ServerState.getServerStateOrDefault(args[2]);
			if(state == null) {
				Chat.sendMessage(sender, "&cState '" + args[2] + "' is invalid.");
				return ResultCommand.SUCCESS;
			}
			serverData.setState(state);
			Chat.sendMessage(sender, "&bSet state of '&9" + serverData.getName() + "&b' to &9" + serverData.getState().getName());
			return ResultCommand.SUCCESS;
		}
		return ResultCommand.SUCCESS;
	}
}
