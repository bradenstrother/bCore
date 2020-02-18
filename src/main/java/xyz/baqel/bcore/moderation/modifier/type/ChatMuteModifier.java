package xyz.baqel.bcore.moderation.modifier.type;

import lombok.Getter;
import xyz.baqel.bcore.moderation.modifier.ChatModifier;

@Getter
public class ChatMuteModifier implements ChatModifier {
    @Getter
    private long createdAt;

    @Getter
    private long duration;

    public ChatMuteModifier() { this(Long.MAX_VALUE); }

    public ChatMuteModifier(long duration) {
        this.duration = duration;

        this.createdAt = System.currentTimeMillis();
    }

    public long getCreatedAt() { return this.createdAt; }

    public long getDuration() { return this.duration; }
}
