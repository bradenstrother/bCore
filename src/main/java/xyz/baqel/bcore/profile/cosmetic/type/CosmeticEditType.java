package xyz.baqel.bcore.profile.cosmetic.type;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.util.Chat;
import xyz.baqel.bcore.util.item.ItemMaker;

public enum CosmeticEditType {
	
    TAG(new ItemMaker(Material.NAME_TAG).setTitle(Chat.AQUA + Chat.BOLD + "Tags").setLore("", Chat.WHITE + "Click here to select a Tag.").build()),
    COLOR(new ItemMaker(Material.WOOL).setData(2).setTitle(Chat.AQUA + Chat.BOLD + "Tags " + Chat.WHITE + "(Colors)").setLore("", Chat.WHITE + "Click here to select a Tag Color.").build());
    
    private ItemStack itemStack;
    
    public ItemStack getItemStack() { return this.itemStack; }
    
    CosmeticEditType(ItemStack itemStack) { this.itemStack = itemStack; }
}
