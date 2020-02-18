package xyz.baqel.bcore.util.item;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import xyz.baqel.bcore.bCore;

public class ItemData implements ConfigurationSerializable {
	
    public static ItemData fromItemName(String string) {
        ItemStack stack = bCore.getPlugin().getManager().getItemDb().getItem(string);
        return new ItemData(stack.getType(), stack.getData().getData());
    }

    public static ItemData fromStringValue(String value) {
        int firstBracketIndex = value.indexOf(40);
        if (firstBracketIndex == -1) {
            return null;
        }
        int otherBracketIndex = value.indexOf(41);
        if (otherBracketIndex == -1) {
            return null;
        }
        String itemName = value.substring(0, firstBracketIndex);
        String itemData = value.substring(firstBracketIndex + 1, otherBracketIndex);
        Material material = Material.getMaterial(itemName);
        return new ItemData(material, Short.parseShort(itemData));
    }
    
    private Material material;
    private short itemData;

    public ItemData(MaterialData data) {
        this(data.getItemType(), data.getData());
    }

    public ItemData(ItemStack stack) {
        this(stack.getType(), stack.getData().getData());
    }

    public ItemData(Material material, short itemData) {
        this.material = material;
        this.itemData = itemData;
    }

    public ItemData(Map map) {
        Object object = map.get("itemType");
        if (!(object instanceof String)) {
            throw new AssertionError((Object) "Incorrectly configurised");
        }
        this.material = Material.getMaterial((String) object);
        if ((object = map.get("itemData")) instanceof Short) {
            this.itemData = (short) object;
            return;
        }
        throw new AssertionError((Object) "Incorrectly configurised");
    }

    public Map serialize() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("itemType", this.material.name());
        map.put("itemData", this.itemData);
        return map;
    }

    public Material getMaterial() {
        return this.material;
    }

    @Deprecated
    public short getItemData() {
        return this.itemData;
    }

    public String getItemName() {
        return bCore.getPlugin().getManager().getItemDb().getName(new ItemStack(this.material, 1, this.itemData));
    }
}
