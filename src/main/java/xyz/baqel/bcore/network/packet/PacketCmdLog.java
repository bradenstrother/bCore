package xyz.baqel.bcore.network.packet;

import com.google.gson.JsonObject;
import xyz.baqel.bcore.database.redis.gson.JsonChain;
import xyz.baqel.bcore.database.redis.packet.Packet;

public class PacketCmdLog implements Packet {

    private String server;
    private String senderName;
    private String message;

    public PacketCmdLog() {
        // TODO Auto-generated constructor stub
    }

    public PacketCmdLog(String server, String senderName, String message) {
        this.server = server;
        this.senderName = senderName;
        this.message = message;
    }

    @Override
    public int id() { return 3; }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("server", this.server)
                .addProperty("senderName", this.senderName)
                .addProperty("message", this.message)
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.server = object.get("server").getAsString();
        this.senderName = object.get("senderName").getAsString();
        this.message = object.get("message").getAsString();
    }

    public String getServer() {
        return server;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderName() {
        return senderName;
    }
}
