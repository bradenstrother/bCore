package xyz.baqel.bcore.util.item;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.util.JavaUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ItemDB {
    @Getter
    private Map<String, Integer> items;

    @Getter
    private Map<ItemData, List<String>> names;

    @Getter
    private Map<ItemData, String> primaryName;

    @Getter
    private Map<String, Short> durabilities;

    @Getter
    private Pattern splitPattern;

    public ItemDB() {
        this.items = new HashMap<>();

        this.names = new HashMap<>();

        this.primaryName = new HashMap<>();

        this.durabilities = new HashMap<>();

        this.splitPattern = Pattern.compile("((.*)[:+',;.](\\d+))");

        this.reloadConfig();
    }

    public static List<String> getLines(String filename) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(bCore.getPlugin().getResource(filename)))) {
                List<String> lines = new ArrayList<>();

                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    lines.add(line);
                }
                return lines;
            }
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public static String joinList(Object... list) {
        return joinList(", ", list);
    }

    private static String joinList(String seperator, Object... list) {
        StringBuilder buf = new StringBuilder();

        for (Object each : list) {
            if (buf.length() > 0) {
                buf.append(seperator);
            }

            if (each instanceof Collection) {
                buf.append(joinList(seperator, ((Collection) each).toArray()));
            } else {
                try {
                    buf.append(each.toString());
                } catch (Exception e) {
                    buf.append(each.toString());
                }
            }
        }
        return buf.toString();
    }

    private void reloadConfig() {
        new BukkitRunnable() {

            @Override
            public void run() {
                List<String> lines = ItemDB.getLines("items.txt");

                if (lines.isEmpty()) {
                    return;
                }

                ItemDB.this.durabilities.clear();

                ItemDB.this.items.clear();

                ItemDB.this.names.clear();

                ItemDB.this.primaryName.clear();

                for (String line : lines) {
                    line = line.trim().toLowerCase(Locale.ENGLISH);

                    if (line.length() > 0 && line.charAt(0) == '#') {
                        continue;
                    }

                    String[] parts = line.split("[^a-z0-9]");

                    if (parts.length < 2) {
                        continue;
                    }

                    int numeric = Integer.parseInt(parts[1]);

                    short data = ((parts.length > 2 && !parts[2].equals("0")) ? Short.parseShort(parts[2]) : 0);

                    String itemName = parts[0].toLowerCase(Locale.ENGLISH);

                    ItemDB.this.durabilities.put(itemName, data);

                    ItemDB.this.items.put(itemName, numeric);

                    ItemData itemData = new ItemData(numeric, data);

                    if (ItemDB.this.names.containsKey(itemData)) {
                        List<String> nameList = ItemDB.this.names.get(itemData);

                        nameList.add(itemName);

                        Collections.sort(nameList, new LengthCompare());
                    } else {
                        List<String> nameList = new ArrayList<>();

                        nameList.add(itemName);

                        ItemDB.this.names.put(itemData, nameList);

                        ItemDB.this.primaryName.put(itemData, itemName);
                    }
                }
            }
        }.runTaskAsynchronously(bCore.getPlugin());
    }

    public ItemStack get(String id, int quantity) {
        ItemStack retval = this.get(id.toLowerCase(Locale.ENGLISH));

        if (retval != null) {
            retval.setAmount(quantity);
        }
        return retval;
    }

    public ItemStack get(String id) {
        try {
            int itemid = 0;

            String itemname;

            short metaData = 0;

            Matcher parts = this.splitPattern.matcher(id);

            if (parts.matches()) {
                itemname = parts.group(2);
                metaData = Short.parseShort(parts.group(3));
            } else {
                itemname = id;
            }

            if (JavaUtil.isInteger(itemname)) {
                itemid = Integer.parseInt(itemname);
            } else if (JavaUtil.isInteger(id)) {
                itemid = Integer.parseInt(id);
            } else {
                itemname = itemname.toLowerCase(Locale.ENGLISH);
            }

            if (itemid < 1) {
                if (this.items.containsKey(itemname)) {
                    itemid = this.items.get(itemname);

                    if (this.durabilities.containsKey(itemname) && metaData == 0) {
                        metaData = this.durabilities.get(itemname);
                    }
                } else if (Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH)) != null) {
                    Material bMaterial = Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH));

                    itemid = bMaterial.getId();
                } else {
                    try {
                        Material bMaterial = Material.getMaterial(itemname.toLowerCase(Locale.ENGLISH));
                        itemid = bMaterial.getId();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }

            if (itemid < 1) {
                return null;
            }

            Material mat = Material.getMaterial(String.valueOf(itemid));

            if (mat == null) {
                return null;
            }

            ItemStack retval = new ItemStack(mat);

            retval.setAmount(mat.getMaxStackSize());

            retval.setDurability(metaData);

            return retval;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String names(ItemStack item) {
        ItemData itemData = new ItemData(item.getType().getId(), item.getDurability());

        List<String> nameList = this.names.get(itemData);

        if (nameList == null) {
            itemData = new ItemData(item.getType().getId(), (short) 0);

            nameList = this.names.get(itemData);

            if (nameList == null) {
                return null;
            }
        }

        if (nameList.size() > 15) {
            nameList = nameList.subList(0, 14);
        }
        return joinList(", ", nameList);
    }

    public String name(ItemStack item) {
        ItemData itemData = new ItemData(item.getType().getId(), item.getDurability());

        String name = this.primaryName.get(itemData);

        if (name == null) {
            itemData = new ItemData(item.getType().getId(), (short) 0);

            name = this.primaryName.get(itemData);

        }
        return name;
    }

    @Getter
    public static class ItemData {

        @Getter
        private int itemNo;

        @Getter
        private short itemData;

        ItemData(int itemNo, short itemData) {
            this.itemNo = itemNo;

            this.itemData = itemData;
        }

        public int getItemNo() {
            return this.itemNo;
        }

        public short getItemData() {
            return this.itemData;
        }
    }

    public class LengthCompare implements Comparator<String> {
        @
                Override
        public int compare(String s1, String s2) {
            return s1.length() - s2.length();
        }
    }
}
