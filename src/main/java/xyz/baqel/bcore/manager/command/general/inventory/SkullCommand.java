package xyz.baqel.bcore.manager.command.general.inventory;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;
import xyz.baqel.bcore.util.item.ItemMaker;

public class SkullCommand extends CustomCommand {
    public SkullCommand() {
        super("skull", "Gets the specified players head.", new String[] {}, "skull <player>", "bcore.cmd.skull");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }
        Optional<ItemMaker.SkullType> skullType = ((args.length > 0) ? Enums.getIfPresent(ItemMaker.SkullType.class, args[0]) : Optional.absent());
        ItemStack stack;
        if (skullType.isPresent()) {
            stack = new ItemStack(Material.SKULL_ITEM, 1, (short) skullType.get().getData());
        } else {
            stack = new ItemStack(Material.SKULL_ITEM, 1, (short) ItemMaker.SkullType.PLAYER.getData());
            String ownerName = (args.length > 0) ? args[0] : sender.getName();
            SkullMeta meta = (SkullMeta)stack.getItemMeta();
            meta.setOwner(ownerName);
            stack.setItemMeta(meta);
        }
        ((Player) sender).getInventory().addItem(stack);
        Chat.sendMessage(sender, "&aSuccessfully retrieved "+args[0]+"'s skull.");
        return ResultCommand.SUCCESS;
    }
}
