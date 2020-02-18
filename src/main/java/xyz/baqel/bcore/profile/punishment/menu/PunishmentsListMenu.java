package xyz.baqel.bcore.profile.punishment.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.punishment.Punishment;
import xyz.baqel.bcore.profile.punishment.PunishmentType;
import xyz.baqel.bcore.profile.punishment.procedure.PunishmentProcedure;
import xyz.baqel.bcore.profile.punishment.procedure.PunishmentProcedureStage;
import xyz.baqel.bcore.profile.punishment.procedure.PunishmentProcedureType;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.TimeUtil;
import xyz.baqel.bcore.util.item.ItemMaker;
import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.pagination.PaginatedMenu;

public class PunishmentsListMenu extends PaginatedMenu {
	
    private Profile profile;
    
    private PunishmentType punishmentType;
    
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&c" + this.punishmentType.getTypeData().getReadable() + " &7- &f" + this.profile.getColoredUsername();
    }
    
    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (Punishment punishment : this.profile.getPunishments()) {
            if (punishment.getType() == this.punishmentType) {
                buttons.put(buttons.size(), new PunishmentInfoButton(punishment));
            }
        }
        return buttons;
    }
    
    PunishmentsListMenu(Profile profile, PunishmentType punishmentType) {
        this.profile = profile;
        this.punishmentType = punishmentType;
    }
    
    private class PunishmentInfoButton extends Button {
    	
        private Punishment punishment;
        
        @Override
        public ItemStack getButtonItem(Player player) {
            String addedBy = "Console";
            if (this.punishment.getAddedBy() != null) {
                try {
                    Profile addedByProfile = Profile.getByUuid(this.punishment.getAddedBy());
                    addedBy = addedByProfile.getUsername();
                }
                catch (Exception e) {
                    addedBy = "Could not fetch...";
                }
            }
            List<String> lore = new ArrayList<String>();
            lore.add(Chat.MENU_BAR);
            lore.add("&cIssuer: &f" + addedBy);
            lore.add("&cReason: &f" + this.punishment.getAddedReason());
            if (this.punishment.isActive() && !this.punishment.isPermanent() && this.punishment.getDuration() != -1L) {
                lore.add("&cTime Left: &f" + this.punishment.getTimeRemaining());
            }
            lore.add("&cDate added: &f" + this.punishment.getAddedAtFormatted());
            if (this.punishment.isPardoned()) {
                String removedBy = "Console";
                if (this.punishment.getPardonedBy() != null) {
                    try {
                        Profile removedByProfile = Profile.getByUuid(this.punishment.getPardonedBy());
                        removedBy = removedByProfile.getUsername();
                    } catch (Exception e) {
                        removedBy = "Could not fetch...";
                    }
                }
                lore.add(Chat.MENU_BAR);
                lore.add("&a[Removed] &bIssuer: &f" + removedBy);
                lore.add("&a[Removed] &bReason: &f" + this.punishment.getPardonedReason());
                lore.add("&a[Removed] &bDate removed: &f" + TimeUtil.dateToString(new Date(this.punishment.getPardonedAt())));
            }
            lore.add(Chat.MENU_BAR);
            if (!this.punishment.isPardoned() && this.punishment.getType().isCanBePardoned()) {
                lore.add("&dRight click to pardon this punishment");
                lore.add(Chat.MENU_BAR);
            }
            return new ItemMaker(Material.PAPER).setTitle("&b" + TimeUtil.dateToString(new Date(this.punishment.getAddedAt()))).setLore(lore).build();
        }
        
        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType == ClickType.RIGHT && !this.punishment.isPardoned() && this.punishment.getType().isCanBePardoned()) {
                PunishmentProcedure procedure = new PunishmentProcedure(player, PunishmentsListMenu.this.profile, PunishmentProcedureType.PARDON, PunishmentProcedureStage.REQUIRE_TEXT);
                procedure.setPunishment(this.punishment);
                Chat.sendMessage(player, "&aType a reason for pardoning this punishment in chat...");
                player.closeInventory();
            }
        }
        
        PunishmentInfoButton(Punishment punishment) { this.punishment = punishment; }
    }
}
