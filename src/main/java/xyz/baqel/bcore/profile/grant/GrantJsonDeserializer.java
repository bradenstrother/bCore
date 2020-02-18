package xyz.baqel.bcore.profile.grant;

import java.util.UUID;

import com.google.gson.JsonObject;

import xyz.baqel.bcore.database.redis.gson.JsonDeserializer;
import xyz.baqel.bcore.profile.rank.Rank;

public class GrantJsonDeserializer implements JsonDeserializer<Grant> {
	
    @Override
    public Grant deserialize(JsonObject object) {
        Rank rank = Rank.getRankByUuid(UUID.fromString(object.get("rank").getAsString()));
        Grant grant = new Grant(UUID.fromString(object.get("uuid").getAsString()), rank, null, object.get("addedAt").getAsLong(), object.get("addedReason").getAsString(), object.get("duration").getAsLong());
        if (!object.get("addedBy").isJsonNull()) {
            grant.setAddedBy(UUID.fromString(object.get("addedBy").getAsString()));
        }
        if (!object.get("removedBy").isJsonNull()) {
            grant.setRemovedBy(UUID.fromString(object.get("removedBy").getAsString()));
        }
        if (!object.get("removedAt").isJsonNull()) {
            grant.setRemovedAt(object.get("removedAt").getAsLong());
        }
        if (!object.get("removedReason").isJsonNull()) {
            grant.setRemovedReason(object.get("removedReason").getAsString());
        }
        if (!object.get("removed").isJsonNull()) {
            grant.setRemoved(object.get("removed").getAsBoolean());
        }
        return grant;
    }
}
