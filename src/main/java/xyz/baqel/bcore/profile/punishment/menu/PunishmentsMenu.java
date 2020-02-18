package xyz.baqel.bcore.profile.punishment.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.punishment.PunishmentType;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.item.ItemMaker;
import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.Menu;

public class PunishmentsMenu extends Menu {
	
    private Profile profile;
    
    @Override
    public String getTitle(Player player) {
        return "&cPunishments of " + this.profile.getColoredUsername();
    }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(9, new SelectPunishmentTypeButton(this.profile, PunishmentType.BLACKLIST));
        buttons.put(11, new SelectPunishmentTypeButton(this.profile, PunishmentType.BAN));
        buttons.put(13, new SelectPunishmentTypeButton(this.profile, PunishmentType.MUTE));
        buttons.put(15, new SelectPunishmentTypeButton(this.profile, PunishmentType.WARN));
        buttons.put(17, new SelectPunishmentTypeButton(this.profile, PunishmentType.KICK));
        return buttons;
    }
    
    @Override
    public int getSize() {
        return 27;
    }

    public PunishmentsMenu(Profile profile) {
        this.profile = profile;
    }
    
    private class SelectPunishmentTypeButton extends Button {
    	
        private Profile profile;
        private PunishmentType punishmentType;
        
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemMaker(Material.WOOL).setTitle(this.punishmentType.getTypeData().getColor() + Chat.BOLD + this.punishmentType.getTypeData().getReadable()).setLore(Chat.GRAY + this.profile.getPunishmentCountByType(this.punishmentType) + " " + this.punishmentType.getTypeData().getReadable().toLowerCase() + " on record").setData(this.punishmentType.getTypeData().getDurability()).build();
        }
        
        @Override
        public void clicked(Player player, ClickType clickType) {
            new PunishmentsListMenu(this.profile, this.punishmentType).openMenu(player);
        }
        
        SelectPunishmentTypeButton(Profile profile, PunishmentType punishmentType) {
            this.profile = profile;
            this.punishmentType = punishmentType;
        }
    }
}
