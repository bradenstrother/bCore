package xyz.baqel.bcore.profile.cosmetic;

import com.mongodb.client.MongoCursor;
import org.bson.Document;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.util.chat.Chat;

import java.util.List;

public class Tag {

    private static List<Tag> tags;
    private String name;
    private String prefix;
    private String permission;

    public Tag(String name) {
        this.name = name;
        Tag.tags.add(this);
    }

    public void loadTags() {
        try (MongoCursor mongoCursor = bCore.getPlugin().getDatabaseManager().getTags().find().iterator()) {
            while (mongoCursor.hasNext()) {
                Document document = mongoCursor.next();
                Tag tag = new Tag(document.getString("name"));
                if (document.containsKey("prefix")) {
                    tag.prefix = Chat.formatMessages(document.getString("prefix"));
                }
                if (document.containsKey("permissions")) {
                    tag.permission = document.getString("permission");
                }
            }
        }
    }
}
