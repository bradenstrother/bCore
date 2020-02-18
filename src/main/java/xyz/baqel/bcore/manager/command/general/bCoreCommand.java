package xyz.baqel.bcore.manager.command.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

import static xyz.baqel.bcore.util.chat.Chat.sendMessage;

public class bCoreCommand extends CustomCommand {
    public bCoreCommand() { super("bcore", "Sends a list of commands and helpful information.", new String[] {"help"}, "bcore (page)", "bcore.cmd.bcore"); }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendMessage(sender, Chat.GREEN + Chat.CHAT_BAR);
            sendMessage(sender, "&aCommand List &7- &c() &7= &cRequired &b<> &7= &bOptional");
        }
        return ResultCommand.SUCCESS;
    }
}
