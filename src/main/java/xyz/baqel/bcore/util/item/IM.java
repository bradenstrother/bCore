package xyz.baqel.bcore.util.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.baqel.bcore.util.chat.ColorText;

import java.util.ArrayList;
import java.util.List;

public class IM {
    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public IM(final Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public IM(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public IM setAmount(final int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public IM setDisplayname(final String name) {
        this.itemMeta.setDisplayName(ColorText.translate(name));
        return this;
    }

    public IM setDurability(final int durability) {
        this.itemStack.setDurability((short)durability);
        return this;
    }

    public IM addLore(final String lore) {
        List<String> object = this.itemMeta.getLore();
        if (object == null) {
            object = new ArrayList<>();
        }
        (object).add(ColorText.translate(lore));
        this.itemMeta.setLore(object);
        return this;
    }

    public IM addLore(final List<String> lore) {
        this.itemMeta.setLore(ColorText.translate(lore));
        return this;
    }

    public IM addLore(final String... lore) {
        final List<String> strings = new ArrayList<>();
        for (final String string : lore) {
            strings.add(ColorText.translate(string));
        }
        this.itemMeta.setLore(strings);
        return this;
    }

    public ItemStack create() {
        if (this.itemMeta != null) {
            this.itemStack.setItemMeta(this.itemMeta);
        }
        return this.itemStack;
    }
}
