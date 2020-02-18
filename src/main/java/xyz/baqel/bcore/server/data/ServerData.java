package xyz.baqel.bcore.server.data;

import xyz.baqel.bcore.server.state.ServerState;
import xyz.baqel.bcore.server.type.ServerType;

public class ServerData {
	
	private String name;
	private ServerType type;
	private ServerState state;
	private int count;
	private int maxCount;
	private long lastUpdate;
	private boolean whitelisted;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ServerType getType() {
		return type;
	}
	
	public void setType(ServerType type) {
		this.type = type;
	}
	
	public ServerState getState() {
		return state;
	}
	
	public void setState(ServerState state) {
		this.state = state;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getMaxCount() {
		return maxCount;
	}
	
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	
	public long getLastUpdate() {
		return lastUpdate;
	}
	
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public boolean isWhitelisted() {
		return whitelisted;
	}
	
	public void setWhitelisted(boolean whitelisted) {
		this.whitelisted = whitelisted;
	}
}
