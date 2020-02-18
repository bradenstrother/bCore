package xyz.baqel.bcore.util.config;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class ConfigCursor {
	
    private BukkitConfigHelper bukkitConfig;
    private String path;
    
    public boolean exists() {
        return this.exists(null);
    }
    
    public boolean exists(String path) {
        return this.bukkitConfig.getConfiguration().contains(this.path + ((path == null) ? "" : ("." + path)));
    }
    
    public Set<String> getKeys() {
        return this.getKeys(null);
    }
    
    private Set<String> getKeys(String path) {
        return this.bukkitConfig.getConfiguration().getConfigurationSection(this.path + ((path == null) ? "" : ("." + path))).getKeys(false);
    }
    
    public String getString(String path) {
        return this.bukkitConfig.getConfiguration().getString(((this.path == null) ? "" : (this.path + ".")) + path);
    }
    
    public boolean getBoolean(String path) {
        return this.bukkitConfig.getConfiguration().getBoolean(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }
    
    public int getInt(String path) {
        return this.bukkitConfig.getConfiguration().getInt(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }
    
    public long getLong(String path) {
        return this.bukkitConfig.getConfiguration().getLong(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }
    
    public List<String> getStringList(String path) {
        return this.bukkitConfig.getConfiguration().getStringList(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }
    
    public UUID getUuid(String path) {
        return UUID.fromString(this.bukkitConfig.getConfiguration().getString(this.path + "." + path));
    }
    
    public World getWorld(String path) {
        return Bukkit.getWorld(this.bukkitConfig.getConfiguration().getString(this.path + "." + path));
    }
    
    public void set(Object value) {
        this.set(null, value);
    }
    
    public void set(String path, Object value) {
        this.bukkitConfig.getConfiguration().set(this.path + ((path == null) ? "" : ("." + path)), value);
    }
    
    public void save() {
        this.bukkitConfig.save();
    }
    
    public ConfigCursor(BukkitConfigHelper bukkitConfig, String path) {
        this.bukkitConfig = bukkitConfig;
        this.path = path;
    }
    
    public BukkitConfigHelper getFileConfig() {
		return bukkitConfig;
	}
    
    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
}
