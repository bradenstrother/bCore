package xyz.baqel.bcore.profile.cosmetic;

import xyz.baqel.bcore.util.Chat;

public enum CosmeticColor {
	
    DARK_RED("Dark Red", Chat.DARK_RED, 14),
    RED("Red", Chat.RED, 14),
    PURPLE("Purple", Chat.DARK_PURPLE, 10),
    PINK("Pink", Chat.PINK, 2),
    GOLD("Gold", Chat.GOLD, 1),
    YELLOW("Yellow", Chat.YELLOW, 4),
    GREEN("Green", Chat.GREEN, 5),
    DARK_GREEN("Dark Green", Chat.DARK_GREEN, 13),
    LIGHT_BLUE("Light Blue", Chat.AQUA, 11),
    AQUA("Aqua", Chat.DARK_AQUA, 9),
    BLUE("Blue", Chat.BLUE, 3),
	WHITE("White", Chat.WHITE, 0);
    
    private String name;
    private String display;
    private int variant;
    
    CosmeticColor(String name, String display, int variant) {
        this.name = name;
        this.display = display;
        this.variant = variant;
    }
    
    public String getName() { return this.name; }
    
    public String getDisplay() { return this.display; }
    
    public int getVariant() { return this.variant; }
}
