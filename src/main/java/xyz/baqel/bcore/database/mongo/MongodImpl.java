package xyz.baqel.bcore.database.mongo;

import java.io.Closeable;
import java.util.Collections;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.entity.Player;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.config.ConfigCursor;

@Getter
@RequiredArgsConstructor
public class MongodImpl implements Closeable {

    @Getter
    private bCore plugin;
    @Getter
    private MongoClient client;
    @Getter
    private MongoDatabase mongoDatabase;
    @Getter
    private MongoCollection<Document> profiles;

    public MongodImpl(bCore plugin) {
        this.plugin = plugin;
    }

    public void init() {
        ConfigCursor cursorMongo = new ConfigCursor(this.plugin.getRootConfig(), "database.mongo");
        if (!cursorMongo.exists("host")
                || !cursorMongo.exists("port")
                || !cursorMongo.exists("database")
                || !cursorMongo.exists("authentication.enabled")
                || !cursorMongo.exists("authentication.username")
                || !cursorMongo.exists("authentication.password")
                || !cursorMongo.exists("authentication.database")) {
            throw new RuntimeException("Missing configuration option");
        }
        if (cursorMongo.getBoolean("authentication.enabled")) {
            MongoCredential credential = MongoCredential.createCredential(
                    cursorMongo.getString("authentication.username"),
                    cursorMongo.getString("authentication.database"),
                    cursorMongo.getString("authentication.password").toCharArray()
            );
            this.client = new MongoClient(new ServerAddress(cursorMongo.getString("host"), cursorMongo.getInt("port")), Collections.singletonList(credential));
        } else {
            this.client = new MongoClient(new ServerAddress(cursorMongo.getString("host"), cursorMongo.getInt("port")));
        }
        this.mongoDatabase = this.client.getDatabase("bcore");
        this.profiles = this.mongoDatabase.getCollection("profiles");
    }

    public void disable() {
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            Profile.getByUuid(player.getUniqueId()).save();
        }
        this.close();
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public MongoCollection<Document> getProfiles() {
        return profiles;
    }

    public void dropProfiles() {
        this.profiles.drop();
    }

    public Document getProfile(UUID uuid) {
        return this.profiles.find(Filters.eq("uuid", uuid.toString())).first();
    }

    public void replacePlayer(Profile profile, Document document) {
        this.profiles.replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true) );
    }

    @Override
    public void close() {
        if(this.client != null) {
            this.client.close();
        }
    }
}
