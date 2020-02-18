package xyz.baqel.bcore.util.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config extends YamlConfiguration {
    private String fileName;
    private JavaPlugin javaPlugin;

    public Config(final JavaPlugin javaPlugin, final String fileName) {
        this.javaPlugin = javaPlugin;
        this.fileName = fileName;
        this.createNewFile();
    }

    private void createNewFile() {
        final File folder = this.javaPlugin.getDataFolder();
        try {
            final File file = new File(folder, this.fileName);
            if (!file.exists()) {
                if (this.javaPlugin.getResource(this.fileName) != null) {
                    this.javaPlugin.saveResource(this.fileName, false);
                }
                else {
                    this.save(file);
                }
            }
            else {
                this.load(file);
                this.save(file);
            }
        }
        catch (Exception ex) {}
    }

    public void save() {
        final File folder = this.javaPlugin.getDataFolder();
        try {
            this.save(new File(folder, this.fileName));
        }
        catch (Exception ex) {}
    }
}
