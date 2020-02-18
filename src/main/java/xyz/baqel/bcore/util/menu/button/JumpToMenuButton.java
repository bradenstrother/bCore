package xyz.baqel.bcore.util.menu.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.Menu;

public class JumpToMenuButton extends Button {
	
    private Menu menu;
    private ItemStack itemStack;
    
    public JumpToMenuButton(Menu menu, ItemStack itemStack) {
        this.menu = menu;
        this.itemStack = itemStack;
    }
    
    @Override
    public ItemStack getButtonItem(Player player) {
        return this.itemStack;
    }
    
    @Override
    public void clicked(Player player, ClickType clickType) {
        this.menu.openMenu(player);
    }
}
