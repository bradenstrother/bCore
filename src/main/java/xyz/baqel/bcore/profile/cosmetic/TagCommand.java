package xyz.baqel.bcore.profile.cosmetic;

import xyz.baqel.bcore.profile.cosmetic.arguments.*;
import xyz.baqel.bcore.util.command.CustomCommand;

public abstract class TagCommand extends CustomCommand {
    public TagCommand() {
        super("tag");
        this.registerArgument(new TagCreateArgument());
        this.registerArgument(new TagDeleteArgument());
        this.registerArgument(new TagListArgument());
        this.registerArgument(new TagPrefixArgument());
        this.registerArgument(new TagPermArgument());
    }
}
