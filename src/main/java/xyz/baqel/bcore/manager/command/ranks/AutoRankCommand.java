package xyz.baqel.bcore.manager.command.ranks;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import xyz.baqel.bcore.profile.rank.Rank;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class AutoRankCommand extends CustomCommand {

	public AutoRankCommand() { super("autorank", "Automatics ranks", new String[] {}, "Automatically generates a set of pre-loaded ranks.", "bcore.cmd.autorank"); }

	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(!this.isConsole(sender)) {
			Chat.sendMessage(sender, "&cThis command is not executable for players.");
			return ResultCommand.SUCCESS;
		}

		Rank owner = new Rank(UUID.randomUUID(), "Owner", "&4&lOwner &4", ChatColor.DARK_RED, 1000, false);
		owner.addPermission("*");
		owner.save();

		Rank developer = new Rank(UUID.randomUUID(), "Developer", "&b&lDev &b", ChatColor.DARK_AQUA, 600, false);
		developer.addPermission("*");
		developer.save();

		Rank admin = new Rank(UUID.randomUUID(), "Admin", "&c&lAdmin &c", ChatColor.RED, 500, false);
		admin.addPermission("*");
		admin.save();

		Rank srmod = new Rank(UUID.randomUUID(), "SMod", "&5&lSMod &5", ChatColor.DARK_PURPLE, 400, false);
		srmod.addPermission("bcore.staff.bypass");
		srmod.save();

		Rank mod = new Rank(UUID.randomUUID(), "Mod", "&5&oMod &5", ChatColor.DARK_PURPLE, 300, false);
		mod.addPermission("bcore.staff.bypass");
		mod.save();

		Rank jmod = new Rank(UUID.randomUUID(), "JMod", "&5JMod &5", ChatColor.DARK_PURPLE, 200, false);
		mod.addPermission("bcore.staff.bypass");
		jmod.save();

		Rank famous = new Rank(UUID.randomUUID(), "Famous", "&d&oFamous &d&o", ChatColor.LIGHT_PURPLE, 90, false);
		famous.save();

		Rank soldier = new Rank(UUID.randomUUID(), "Soldier", "&e&oSoldier &e", ChatColor.YELLOW, 80, false);
		soldier.save();

		Rank sniper = new Rank(UUID.randomUUID(), "Sniper", "&a&oSniper &a", ChatColor.GREEN, 70, false);
		sniper.save();

		Rank hunter = new Rank(UUID.randomUUID(), "Hunter", "&3&oHunter &3", ChatColor.BLUE, 60, false);
		hunter.save();

		Rank medic = new Rank(UUID.randomUUID(), "Medic", "&c&oMedic &c", ChatColor.RED, 50, false);
		medic.save();

		Rank normal = new Rank(UUID.randomUUID(), "Normal", "&7", ChatColor.GRAY, 0, true);
		normal.addPermission("bcore.cosmetic.color");
		normal.addPermission("bcore.cosmetic.tag");
		normal.addPermission("bcore.command.cosmetic");
		return getResultCommand(normal);
	}

	private static ResultCommand getResultCommand(Rank normal) {
		normal.save();

		Rank defaultRank = Rank.getDefaultRank();
		defaultRank.setColor(ChatColor.GRAY);
		defaultRank.setPrefix(ChatColor.translateAlternateColorCodes('&', "&7"));
		defaultRank.setWeight(10);
		defaultRank.save();
		return ResultCommand.SUCCESS;
	}

}
