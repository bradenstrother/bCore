package xyz.baqel.bcore;

import java.lang.reflect.Type;
import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import xyz.baqel.bcore.database.DatabaseManager;
import xyz.baqel.bcore.manager.Manager;
import xyz.baqel.bcore.moderation.Chat;
import xyz.baqel.bcore.network.NetworkManager;
import xyz.baqel.bcore.profile.ProfileManager;
import xyz.baqel.bcore.server.ServerManager;
import xyz.baqel.bcore.util.config.BukkitConfigHelper;
import xyz.baqel.bcore.util.cuboid.Cuboid;
import xyz.baqel.bcore.util.cuboid.NamedCuboid;
import xyz.baqel.bcore.util.item.ItemDB;
import xyz.baqel.bcore.util.location.PersistableLocation;

public class bCore extends JavaPlugin {

    private BukkitConfigHelper rootConfig;
    private DatabaseManager databaseManager;
    private ServerManager serverManager;
    private NetworkManager networkManager;
    private Manager manager;
    private ProfileManager profileManager;
    private ItemDB itemdb;
    private Chat chat;

    public static Gson GSON;
    public static Type LIST_STRING_TYPE;

    static {
        bCore.GSON = new Gson();
        bCore.LIST_STRING_TYPE = new TypeToken<List<String>>() { }.getType();
    }

    public static bCore getPlugin() { return JavaPlugin.getPlugin(bCore.class); }

    @Override
    public void onEnable() {
        this.saveResource("items.txt", false);
        this.itemdb = new ItemDB();
        this.chat = new Chat(this);
        this.registerConfig();
        this.registerSerelialization();
        this.registerManagers();
        this.registerInitialize();
        this.registerRunnables();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        super.onEnable();
    }

    private void registerConfig() {
        this.rootConfig = new BukkitConfigHelper(this, "config", this.getDataFolder().getAbsolutePath());
    }

    private void registerSerelialization() {
        ConfigurationSerialization.registerClass(PersistableLocation.class);
        ConfigurationSerialization.registerClass(Cuboid.class);
        ConfigurationSerialization.registerClass(NamedCuboid.class);
    }

    private void registerManagers() {
        this.databaseManager = new DatabaseManager(this);
        this.serverManager = new ServerManager(this);
        this.networkManager = new NetworkManager(this);
        this.manager = new Manager(this);
        this.profileManager = new ProfileManager(this);
    }

    private void registerInitialize() {
        this.databaseManager.init();
        this.serverManager.init();
        this.networkManager.init();
        this.manager.init();
        this.profileManager.init();
    }

    private void registerRunnables() {
        this.serverManager.runnable();
        this.profileManager.runnable();
    }

    @Override
    public void onDisable() {
        this.databaseManager.disable();
        this.serverManager.disable();
        this.manager.disable();
        this.profileManager.disable();
        super.onDisable();
    }

    public BukkitConfigHelper getRootConfig() { return rootConfig; }

    public DatabaseManager getDatabaseManager() { return databaseManager; }

    public NetworkManager getNetworkManager() { return networkManager; }

    public ServerManager getServerManager() { return serverManager; }

    public Manager getManager() { return manager; }

    public ProfileManager getProfileManager() { return profileManager; }

    public ItemDB getItemDB() { return this.itemdb; }

    public Chat getChat() { return this.chat; }
}
