package xyz.baqel.bcore.server.type;

public enum ServerType {

	HUB("Hub", 1),
	ZOMBIES("Zombies", 2),
	KITPVP("KitPvP", 3),
	SURVIVAL("Survival", 4),
	SKYBLOCK("Skyblock", 5),
	BUILD("Build", 6),
	TEST("Test", 7),
	OTHERS("Other", 8);
	
	private String name;
	private int value;
	
	ServerType(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() { return name; }
	
	public static ServerType getServerTypeOfDefault(String typeName) {
		ServerType types;
		try {
			types = ServerType.valueOf(typeName.toUpperCase());
		} catch (Exception e) {
			types = ServerType.OTHERS;
		}	
		return types;
	}
	
	public int getValue() { return value; }
}
