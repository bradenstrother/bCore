package xyz.baqel.bcore.profile.cosmetic;

import org.apache.commons.lang.StringEscapeUtils;

import xyz.baqel.bcore.util.Chat;

public enum CosmeticTag {
	
    TAG_GOD("God", Chat.BOLD + "God", false, "bcore.tag.god"),
    TAG_PRO("Pro", Chat.BOLD + "Pro", false, "bcore.tag.pro"),
    TAG_MASTER("Master", Chat.BOLD + "Master", false, "bcore.tag.master"),
    TAG_NOOB("Noob", Chat.BOLD + "Noob", false, "bcore.tag.noob"),
    TAG_CHEATER("Cheater", Chat.BOLD + "Cheater", false, "bcore.tag.cheater"),
    TAG_HACKER("Hacker", Chat.BOLD + "Hacker", false, "bcore.tag.hacker"),
    TAG_EZ("eZ", Chat.BOLD + "eZ", false, "bcore.tag.ez"),
    TAG_L("L", Chat.BOLD + "L", false, "bcore.tag.l"),
    TAG_FOUNDER("#iKl4b", Chat.BOLD + "#iKl4b", false, "bcore.tag.founder"),

    ARABIC_STAR("Fancy Star", StringEscapeUtils.unescapeJava("\u06de"), true, "bcore.tag.arabicstar"),
    YIN_YANG("Yin Yang", StringEscapeUtils.unescapeJava("\u0fca"), true, "bcore.tag.yinyang"),
    RADIOACTIVE("Radioactive", StringEscapeUtils.unescapeJava("\u2622"), true, "bcore.tag.radioactive"),
    BIOHAZARD("Biohazard", StringEscapeUtils.unescapeJava("\u2623"), true, "bcore.tag.biohazard"),
    GEAR("Gear", Chat.BOLD + StringEscapeUtils.unescapeJava("\u2699"), true, "bcore.tag.gear"),
    CHECK_MARK("Check Mark", Chat.BOLD + StringEscapeUtils.unescapeJava("\u2713"), true, "bcore.tag.checkmark"),
    X_MARK("X Mark", StringEscapeUtils.unescapeJava("\u2717"), true, "bcore.tag.xmark"),
    STAR_OF_DAVID("Star of David", StringEscapeUtils.unescapeJava("\u2721"), true, "bcore.tag.starofdavid"),
    MALTESE_CROSS("Maltese Cross", StringEscapeUtils.unescapeJava("\u2720"), true, "bcore.tag.maltesecross"),
    CIRCLED_STAR("Circled Star", StringEscapeUtils.unescapeJava("\u272a"), true, "bcore.tag.circledstar"),
    POINTED_STAR("Pointed Star", StringEscapeUtils.unescapeJava("\u2726"), true, "bcore.tag.pointedstar"),
    FLORETTE("Florette", Chat.BOLD + StringEscapeUtils.unescapeJava("\u273f"), true, "bcore.tag.florette");
    
    private String name;
    private String display;
    private boolean icon;
    private String permission;
    
    CosmeticTag(String name, String display, boolean icon, String permission) {
        this.name = name;
        this.display = display;
        this.icon = icon;
        this.permission = permission;
    }
    
    public String getSelectionDisplay() {
        return Chat.formatMessages("&b" + this.name + (this.icon ? (" &7(&d" + this.display + "&7)") : ""));
    }
    
    public String getName() { return this.name; }
     
    public String getDisplay() { return this.display; }
    
    public boolean isIcon() { return this.icon; }

    public String tagPermission() { return this.permission; }
}
