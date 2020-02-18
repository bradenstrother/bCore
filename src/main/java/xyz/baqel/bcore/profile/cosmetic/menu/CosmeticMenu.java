package xyz.baqel.bcore.profile.cosmetic.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.profile.cosmetic.type.CosmeticEditType;
import xyz.baqel.bcore.util.Chat;
import xyz.baqel.bcore.util.item.ItemMaker;
import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.Menu;

public class CosmeticMenu extends Menu {
	
	@Override
	public int getSize() { return 9 * 5; }
	
    @Override
    public String getTitle(Player player) { return Chat.AQUA + "Choose a Cosmetic"; }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(12 -1, new SelectTagButton());
        buttons.put(16 -1, new SelectColorButton());
        buttons.put(32 -1, new ExitButton());
        return buttons;
    }
    
    private class SelectTagButton extends Button {

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemMaker(Material.NAME_TAG).setTitle("&e&lSelect a Tags").setLore(" ", "&dClick here to open tags menu.").build();
		}
		
		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
			new EditTagMenu().openMenu(player);
		}
    	
    }
    
    private class SelectColorButton extends Button {
    	
    	@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemMaker(Material.PAPER).setTitle("&b&lSelect a Colors").setLore(" ", "&dClick here to open colors menu.").build();
		}
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new SelectColorMenu(CosmeticEditType.COLOR).openMenu(player);
        }
    }
    
    private class ExitButton extends Button {
    	
    	@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemMaker(Material.NETHER_STAR).setTitle("&b&lExit").setLore(" ", "&dClick here to exit of cosmetics.").build();
		}
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) { player.closeInventory(); }
    }
}
