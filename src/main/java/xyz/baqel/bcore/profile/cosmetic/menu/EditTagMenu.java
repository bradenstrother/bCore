package xyz.baqel.bcore.profile.cosmetic.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.cosmetic.type.CosmeticEditType;
import xyz.baqel.bcore.util.Chat;
import xyz.baqel.bcore.util.item.ItemMaker;
import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.Menu;

public class EditTagMenu extends Menu {
    
	@Override
	public int getSize() { return 9 * 6; }
	
    @Override
    public String getTitle(Player player) { return Chat.AQUA + Chat.BOLD + "Edit your Tag"; }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(12 -1, new EditButton(CosmeticEditType.TAG));
        buttons.put(16 -1, new EditButton(CosmeticEditType.COLOR));
        buttons.put(41 -1, new ResetTagButton());
        buttons.put(46 -1, new BackButton());
        return buttons;
    }
    
    private class EditButton extends Button {
        private CosmeticEditType editType;
        
        @Override
        public ItemStack getButtonItem(Player player) { return this.editType.getItemStack(); }
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (this.editType == CosmeticEditType.TAG) {
                new SelectTagMenu().openMenu(player);
            }
            else {
                new SelectColorMenu(CosmeticEditType.TAG).openMenu(player);
            }
        }
        
        EditButton(CosmeticEditType editType) { this.editType = editType; }
    }
    
    private class ResetTagButton extends Button {
    	
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemMaker(Material.NETHER_STAR).setTitle(Chat.RED + Chat.BOLD + "Reset Tag").setLore("", Chat.DARK_RED + "Click here to reset your tag.").build();
        }
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.setTag(null);
            profile.setTagColor(null);
            player.closeInventory();
            player.sendMessage(Chat.RED + "You reset your tag.");
        }
    }
    
    private class BackButton extends Button {
    	
    	@Override
    	public ItemStack getButtonItem(Player player) {
    		return new ItemMaker(Material.CARPET).setTitle("&b&lGo Back").setLore(" ", "&5Click here to go back main page.").build();
    	}
    	
    	@Override
    	public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
    		new CosmeticMenu().openMenu(player);
    	}
    }
}
