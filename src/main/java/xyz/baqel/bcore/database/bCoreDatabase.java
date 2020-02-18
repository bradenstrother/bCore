package xyz.baqel.bcore.database;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.baqel.bcore.bCoreConfig;
import xyz.baqel.bcore.util.config.Config;

import java.util.Collections;

public class bCoreDatabase {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection profiles;
    private MongoCollection ranks;
    private MongoCollection punishments;
    private MongoCollection tags;

    public bCoreDatabase(final JavaPlugin javaPlugin) {
        final Config configuration = new Config(javaPlugin, "settings.yml");
        if (configuration.getBoolean("database.mongo.authentication.enabled")) {
            this.client = new MongoClient(new ServerAddress(configuration.getString("database.mongo.host"), configuration.getInt("database.mongo.port")), Collections.singletonList(MongoCredential.createCredential(configuration.getString("database.mongo.authentication.user"), configuration.getString("database.mongo.authentication.database"), configuration.getString("database.mongo.authentication.password").toCharArray())));
        }
        else {
            this.client = new MongoClient(new ServerAddress(configuration.getString("database.mongo.host"), configuration.getInt("database.mongo.port")));
        }
        this.database = this.client.getDatabase("bcore");
        this.profiles = this.database.getCollection("profiles");
        this.ranks = this.database.getCollection("ranks");
        this.punishments = this.database.getCollection("punishments");
        this.tags = this.database.getCollection("tags");
    }

    public MongoClient getClient() {
        return this.client;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public MongoCollection getProfiles() {
        return this.profiles;
    }

    public MongoCollection getRanks() {
        return this.ranks;
    }

    public MongoCollection getPunishments() {
        return this.punishments;
    }

    public MongoCollection getTags() {
        return this.tags;
    }
}
