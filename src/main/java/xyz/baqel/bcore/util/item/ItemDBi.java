package xyz.baqel.bcore.util.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ItemDBi {
	
    void reloadItemDatabase();

    ItemStack getPotion(String string);

    ItemStack getPotion(String string, int value);

    ItemStack getItem(String string);

    ItemStack getItem(String string, int value);

    String getName(ItemStack item);

    @Deprecated
    String getPrimaryName(ItemStack item);

    String getNames(ItemStack item);

    List getMatching(Player player, String[] strings);
}
