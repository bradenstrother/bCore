package xyz.baqel.bcore.manager.command.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.profile.cosmetic.menu.EditTagMenu;
import xyz.baqel.bcore.profile.cosmetic.menu.SelectColorMenu;
import xyz.baqel.bcore.profile.cosmetic.type.CosmeticEditType;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class TagCommand extends CustomCommand {

    public TagCommand() { super("tag", "Open cosmetic menus."); }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(this.isConsole(sender)) {
            return ResultCommand.CONSOLE_ONLY;
        }

        if(!this.hasPermission(sender, "bcore.cmd.cosmetic")) {
            return ResultCommand.INVALID_PERMISSION;
        }

        Player player = (Player) sender;
        if (!(args.length > 1)) {
            new EditTagMenu().openMenu(player);
        }

        if(args[0].equalsIgnoreCase("color")) {
            new SelectColorMenu(CosmeticEditType.COLOR).openMenu(player);
        } else {
            this.setArgument(args[0]);
            return ResultCommand.INVALID_ARGUMENT;
        }
        return ResultCommand.SUCCESS;
    }
}
