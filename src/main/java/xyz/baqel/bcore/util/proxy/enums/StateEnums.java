package xyz.baqel.bcore.util.proxy.enums;

public enum StateEnums {

	ONLINE("Online", 0),
	OFFLINE("Offline", 1);
	
	private String name;
	private int value;
	
	private StateEnums(String name, int value) {
		 this.name = name;
		 this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
}
