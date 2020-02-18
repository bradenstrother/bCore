package xyz.baqel.bcore.network.packet;

import com.google.gson.JsonObject;
import xyz.baqel.bcore.database.redis.gson.JsonChain;
import xyz.baqel.bcore.database.redis.packet.Packet;

public class PacketStaffChat implements Packet {
	
	private String server;
    private String staffName;
    private String message;
    
    public PacketStaffChat() {
		// TODO Auto-generated constructor stub
	}
    
    public PacketStaffChat(String server, String staffName, String message) {
        this.server = server;
        this.staffName = staffName;
        this.message = message;
    }
    
    @Override
    public int id() { return 10; }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain()
        		.addProperty("server", this.server)
        		.addProperty("staffName", this.staffName)
        		.addProperty("message", this.message)
        		.get();
    }
    
    @Override
    public void deserialize(JsonObject object) {
    	this.server = object.get("server").getAsString();
    	this.staffName = object.get("staffName").getAsString();
    	this.message = object.get("message").getAsString();
    }
    
    public String getServer() {
		return server;
	}
    
    public String getMessage() {
		return message;
	}
    
    public String getStaffName() {
		return staffName;
	}
}
