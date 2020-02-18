package xyz.baqel.bcore.util.menu.pagination;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.baqel.bcore.util.menu.Button;

public class JumpToPageButton extends Button {
	
    private int page;
    private PaginatedMenu menu;
    private boolean current;
    
    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Page " + this.page);
        if (this.current) {
            itemMeta.setLore(Arrays.asList("", ChatColor.GREEN + "Current page"));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    @Override
    public void clicked(Player player, ClickType clickType) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        Button.playNeutral(player);
    }
    
    public JumpToPageButton(int page, PaginatedMenu menu, boolean current) {
        this.page = page;
        this.menu = menu;
        this.current = current;
    }
}
