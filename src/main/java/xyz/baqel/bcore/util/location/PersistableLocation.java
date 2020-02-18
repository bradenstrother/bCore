package xyz.baqel.bcore.util.location;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.google.common.base.Preconditions;

public class PersistableLocation implements ConfigurationSerializable, Cloneable {

	private Location location;
    private World world;
    private String worldName;
    private UUID worldUID;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public void presaveMethod(){
        if(worldName == null && world != null){
            worldName = world.getName();
        }
    }

    public void postloadMethod(){
        if(worldName != null){
            world = Bukkit.getWorld(worldName);
            if(world != null){
                worldUID = world.getUID();
            }
        }
    }

    public PersistableLocation() {

    }

    public PersistableLocation(Location location) {
        Preconditions.checkNotNull((Object) location, (Object) "Location cannot be null");
        Preconditions.checkNotNull((Object) location.getWorld(), (Object) "Locations' world cannot be null");
        this.world = location.getWorld();
        this.worldName = this.world.getName();
        this.worldUID = this.world.getUID();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public PersistableLocation(World world, double x, double y, double z) {
        this.worldName = world.getName();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public PersistableLocation(String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public PersistableLocation(Map map) {
        this.worldName = (String) map.get("worldName");
        this.worldUID = UUID.fromString((String) map.get("worldUID"));
        Object o = map.get("x");
        this.x = ((o instanceof String) ? Double.parseDouble((String) o) : ((Number) o).doubleValue());
        o = map.get("y");
        this.y = ((o instanceof String) ? Double.parseDouble((String) o) : ((Number) o).doubleValue());
        o = map.get("z");
        this.z = ((o instanceof String) ? Double.parseDouble((String) o) : ((Number) o).doubleValue());
        this.yaw = Float.parseFloat((String) map.get("yaw"));
        this.pitch = Float.parseFloat((String) map.get("pitch"));
    }

    public Map serialize() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("worldName", this.worldName);
        map.put("worldUID", this.worldUID.toString());
        map.put("x", this.x);
        map.put("y", this.y);
        map.put("z", this.z);
        map.put("yaw", Float.toString(this.yaw));
        map.put("pitch", Float.toString(this.pitch));
        return map;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public UUID getWorldUID() {
        return this.worldUID;
    }

    public World getWorld() {
        Preconditions.checkNotNull((Object) this.worldUID, (Object) "World UUID cannot be null");
        Preconditions.checkNotNull((Object) this.worldName, (Object) "World name cannot be null");
        if (this.world == null) {
            this.world = Bukkit.getWorld(this.worldUID);
        }
        return this.world;
    }

    public void setWorld(World world) {
        this.worldName = world.getName();
        this.worldUID = world.getUID();
        this.world = world;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Location getLocation() {
        if (this.location == null) {
            this.location = new Location(this.getWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
        }
        return this.location;
    }

    public PersistableLocation clone() throws CloneNotSupportedException {
        try {
            return (PersistableLocation) super.clone();
        } catch (CloneNotSupportedException var2) {
            var2.printStackTrace();
            throw new RuntimeException();
        }
    }

}
