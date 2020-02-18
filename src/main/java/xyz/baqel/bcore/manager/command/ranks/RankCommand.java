package xyz.baqel.bcore.manager.command.ranks;

import java.util.Comparator;
import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import xyz.baqel.bcore.profile.rank.Rank;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.ParseUtil;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class RankCommand extends CustomCommand {

	public RankCommand() {
		super("rank", "Manage players ranks and permissions.", new String[] {}, "rank", "bcore.cmd.rank");
	}

	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(!this.hasPermission(sender, "bcore.cmd.rank")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		if(args.length == 0) {
			Chat.sendMessage(sender, Chat.CHAT_BAR);
			Chat.sendMessage(sender, "&aRank Commands Help");
			Chat.sendMessage(sender, "&a/" + label + " create [rank] &7- &fCreate a Rank.");
			Chat.sendMessage(sender, "&a/" + label + " delete (rank) &7- &fDelete a Rank.");
			Chat.sendMessage(sender, "&a/" + label + " setprefix (rank) [prefix] &7- &fSet prefix of a Rank.");
			Chat.sendMessage(sender, "&a/" + label + " setcolor (rank) [COLOR] &7- &fSet color of a Rank.");
			Chat.sendMessage(sender, "&a/" + label + " setweight (rank) [weight] &7- &fSet weight of a Rank.");
			Chat.sendMessage(sender, "&a/" + label + " addperm (rank) [permission] &7- &fAdded permissions to a Rank.");
			Chat.sendMessage(sender, "&a/" + label + " delperm (rank) [permission] &7- &fDeleted permissions to a Rank.");
			Chat.sendMessage(sender, "&a/" + label + " inhert (parent) [child] &7- &fInherit a child Rank.");
			Chat.sendMessage(sender, "&a/" + label + " dump (rank) &7- &fDump a Rank's permissions.");
			Chat.sendMessage(sender, "&a/" + label + " list &7- &fList of the Rank's.");
			Chat.sendMessage(sender, Chat.CHAT_BAR);
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("init")) {
			if(args.length != 2) {
				Chat.sendMessage(sender, "&cInvalid usage: /" + label + " create [name]");
				return ResultCommand.SUCCESS;
			}
			String rankName = args[1];
			
			if(Rank.getRankByDisplayName(rankName) != null) {
				Chat.sendMessage(sender, "&cA rank with that name already exists.");
				return ResultCommand.SUCCESS;
			}
			
			Rank rank = new Rank(rankName);
			rank.save();
			Chat.sendMessage(sender, "&bYou created a new global rank&7: &r" + rank.getColoredName());
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
			if(args.length != 2) {
				Chat.sendMessage(sender, "&cInvalid usage: /" + label + " delete [name]");
				return ResultCommand.SUCCESS;
			}
			String rankName = args[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Chat.sendMessage(sender, "&cA rank with that name does not exist.");
				return ResultCommand.SUCCESS;
			}
			rank.delete();
			Chat.sendMessage(sender, "&bYou deleted &r" + rank.getColoredName());
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("setprefix")) {
			if(args.length != 3) {
				Chat.sendMessage(sender, "&cInvalid usage: /" + label + " setprefix (rank) [prefix]");
				return ResultCommand.SUCCESS;
			}
			String rankName = args[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Chat.sendMessage(sender, "&cA rank with that name does not exist.");
				return ResultCommand.SUCCESS;
			}
			
			String prefix = Chat.formatMessages(args[2]);
			
			rank.setPrefix(prefix);
			rank.save();
			Chat.sendMessage(sender, "&bYou updated the prefix of &r" + rank.getColoredName() + " &bto &r" + rank.getPrefix() + sender.getName());
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("setcolor")) {
			if(args.length != 3) {
				Chat.sendMessage(sender, "&cInvalid usage: /" + label + " setcolor (rank) [color]");
				return ResultCommand.SUCCESS;
			}
			String rankName = args[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Chat.sendMessage(sender, "&cA rank with that name does not exist.");
				return ResultCommand.SUCCESS;
			}
			String oldcolor = rank.getColoredName();
			
			String color = args[2];
			try {
				rank.setColor(ChatColor.valueOf(color));
				rank.save();
			} catch (Exception e) {
				Chat.sendMessage(sender, "&cThis color is not valid");
				Chat.sendMessage(sender, "&cExample: RED, BLUE, AQUA, GREEN, YELLOW, GOLD, GRAY, WHITE, LIGHT_PURPLE, BLACK and DARK_(COLOR)");
				return ResultCommand.SUCCESS;
			}
			Chat.sendMessage(sender, "&bYou updated the color of " + oldcolor + " &bto " + rank.getColoredName());
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("setweight")) {
			if(args.length != 3) {
				Chat.sendMessage(sender, "&cInvalid usage: /" + label + " setweight (rank) [weight]");
				return ResultCommand.SUCCESS;
			}
			String rankName = args[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Chat.sendMessage(sender, "&cA rank with that name does not exist.");
				return ResultCommand.SUCCESS;
			}
			
			Integer weight = ParseUtil.tryParseInt(args[2]);
			if(weight == null) {
				Chat.sendMessage(sender, "This is not valid number.");
				return ResultCommand.SUCCESS;
			}
			rank.setWeight(weight);
			rank.save();
			Chat.sendMessage(sender, "&bYou updated the weight of &r" + rank.getColoredName() + " &bto &d" + rank.getWeight());
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("addpermission") || args[0].equalsIgnoreCase("addperm")) {
			if(args.length != 3) {
				Chat.sendMessage(sender, "&cInvalid usage: /" + label + " addpermission (rank) [permission]");
				return ResultCommand.SUCCESS;
			}
			String rankName = args[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Chat.sendMessage(sender, "&cA rank with that name does not exist.");
				return ResultCommand.SUCCESS;
			}
			
			String permission = args[2];
			if (rank.getPermissions().contains(permission)) {
				Chat.sendMessage(sender, "&c" + rankName +" already had the '" + permission + "' permission.");
				return ResultCommand.SUCCESS;
			}
			
			rank.getPermissions().add(permission);
			rank.save();
			Chat.sendMessage(sender, "&bYou added '&d" + permission + "&b' to &r" + rank.getColoredName() + "&b's permissions.");
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("removepermission") || args[0].equalsIgnoreCase("removeperm") || args[0].equalsIgnoreCase("delperm") || args[0].equalsIgnoreCase("deletepermission")) {
			if(args.length != 3) {
				Chat.sendMessage(sender, "&cInvalid usage: /" + label + " removepermission (rank) [permission]");
				return ResultCommand.SUCCESS;
			}
			String rankName = args[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Chat.sendMessage(sender, "&cA rank with that name does not exist.");
				return ResultCommand.SUCCESS;
			}
			
			String permission = args[2];
			if (!rank.getPermissions().remove(permission)) {
				Chat.sendMessage(sender, "&c" + rankName +" did not have the '" + permission + "' permission.");
				return ResultCommand.SUCCESS;
			}
			
			rank.save();
			Chat.sendMessage(sender, "&bYou removed '&d" + permission + "&b' from &r" + rank.getColoredName() + "&b's permissions.");
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("who")) {
			Chat.sendMessage(sender, Chat.CHAT_BAR);
			Chat.sendMessage(sender, "&9&lList of the ranks.");
			Rank.getRanks().values().stream().sorted(Comparator.comparingInt(Rank::getWeight)).forEach(ranks -> Chat.sendMessage(sender, " &f- &r" + ranks.getColoredName() + "&r" + (ranks.isDefaultRank() ? " (Default)" : "") + " (Weight: " + ranks.getWeight() + ")"));
			Chat.sendMessage(sender, Chat.CHAT_BAR);
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("inheritance") || args[0].equalsIgnoreCase("inherit")) {
			if(args.length != 3) {
				Chat.sendMessage(sender, "&cInvalid usage: /" + label + " inheritance (parent) [child]");
				return ResultCommand.SUCCESS;
			}
			String parentName = args[1];
			
			Rank parent = Rank.getRankByDisplayName(parentName);
			if(parent == null) {
				Chat.sendMessage(sender, "&cParent rank with that name does not exist.");
				return ResultCommand.SUCCESS;
			}
			String childName = args[2];
			
			Rank child = Rank.getRankByDisplayName(childName);
			if(child == null) {
				Chat.sendMessage(sender, "&cChild rank with that name does not exist.");
				return ResultCommand.SUCCESS;
			}
			
			boolean removed = false;
			if(parent.getInherited().remove(child)) {
				removed = true;
			} else {
				parent.getInherited().add(child);
			}
			parent.save();
			Chat.sendMessage(sender, "&bYou made &r" + parent.getColoredName() + (removed ? " &cno longer inherit" : " &ainherit") + " &r" + child.getColoredName());
			return ResultCommand.SUCCESS;
		}
		if(args[0].equalsIgnoreCase("dump") || args[0].equalsIgnoreCase("info")) {
			if(args.length != 2) {
				Chat.sendMessage(sender, "&cInvalid usage: /" + label + " dump (rank)");
				return ResultCommand.SUCCESS;
			}
			String rankName = args[1];
			
			Rank rank = Rank.getRankByDisplayName(rankName);
			if(rank == null) {
				Chat.sendMessage(sender, "&cA rank with that name does not exist.");
				return ResultCommand.SUCCESS;
			}
			
			Chat.sendMessage(sender, Chat.CHAT_BAR);
			Chat.sendMessage(sender, "&9Rank Information &7(&r" + rank.getColoredName() + "&7)");
			Chat.sendMessage(sender, "&bWeight&7: &r" + rank.getWeight());
			Chat.sendMessage(sender, "&bPrefix&7: &r" + rank.getPrefix() + "Example");
			Chat.sendMessage(sender, "&bColor&7: &r" + rank.getColor().name());
			Chat.sendMessage(sender, " ");
			Chat.sendMessage(sender, "&bPermissions: &r(" + rank.getAllPermissions().size() + ")");
			StringJoiner perm = new StringJoiner(", ");
			for(String permissions : rank.getAllPermissions()) {
				perm.add(permissions);
			}
			if(rank.getAllPermissions().size() > 0) {
				Chat.sendMessage(sender, "&r" + perm.toString());
			}
			Chat.sendMessage(sender, " ");
			Chat.sendMessage(sender, "&bInherits: &r(" + rank.getInherited().size() + ")");
			for(Rank inherit : rank.getInherited()) {
				Rank other = Rank.getRankByDisplayName(inherit.getDisplayName());
				if(other == null) {
					Chat.sendMessage(sender, " &f* &r" + inherit.getColoredName());
				} else {
					Chat.sendMessage(sender, " &f* &r" + other.getColoredName());
				}
			}
			Chat.sendMessage(sender, Chat.CHAT_BAR);
		} else {
			Chat.sendMessage(sender, "&cInvalid sub-command");
		}
		return ResultCommand.SUCCESS;
	}
}
