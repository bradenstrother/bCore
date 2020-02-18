package xyz.baqel.bcore.manager.command.general.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class VanishCommand extends CustomCommand {
    public VanishCommand() { super("vanish", "Hide from other players.", new String[]{"v", "invis"}); }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }
        Player player = (Player) sender;
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.isVanished()) {
            profile.setVanished(false);
            Chat.sendMessage(player, "&bYou are now visible.");
            return ResultCommand.SUCCESS;
        }
        profile.setVanished(true);
        Chat.sendMessage(player, "&cYou are now only visible to staff members.");
        return ResultCommand.SUCCESS;
    }
}
