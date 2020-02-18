package xyz.baqel.bcore.manager.command.general.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.manager.staffmode.StaffMode;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class StaffModeCommand extends CustomCommand {
    public StaffModeCommand() {
        super("staffmode", "Toggle staff mode", new String[] {"admin", "mod"}, "staffmode", "bcore.staff.mode");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }
        Player player = (Player) sender;
        Profile profile = Profile.getByUuid(player.getUniqueId());
        StaffMode staffMode = profile.getStaffMode();
        if (profile.getStaffMode() == null) {
            profile.setStaffMode(new StaffMode(player));
            Chat.sendMessage(sender, "&bYou are now in &f&oStaff Mode&a.");
            return ResultCommand.SUCCESS;
        }
        staffMode.setVanished(false);
        player.getInventory().setContents(staffMode.getContents());
        player.getInventory().setArmorContents(staffMode.getArmor());
        player.setGameMode(staffMode.getGamemode());
        profile.setStaffMode(null);
        Chat.sendMessage(sender, "&bYou are no longer in &f&oStaff Mode&a.");
        return ResultCommand.SUCCESS;
    }
}
