package xyz.baqel.bcore.util.menu.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.item.ItemMaker;
import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.Menu;

public class BackButton extends Button {
	
    private Menu back;
    
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemMaker(Material.REDSTONE).setTitle(Chat.RED + Chat.BOLD + "Back").setLore(Chat.RED + "Click here to return to", Chat.RED + "the previous menu.").build();
    }
    
    @Override
    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        this.back.openMenu(player);
    }
    
    public BackButton(Menu back) {
        this.back = back;
    }
}
