package xyz.baqel.bcore.manager;

import java.io.IOException;

import org.bukkit.plugin.PluginManager;

import lombok.Getter;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.manager.command.general.*;
import xyz.baqel.bcore.manager.command.general.chat.ClearChatCommand;
import xyz.baqel.bcore.manager.command.general.chat.MuteChatCommand;
import xyz.baqel.bcore.manager.command.general.chat.SlowChatCommand;
import xyz.baqel.bcore.manager.command.general.gamemode.AdventureCommand;
import xyz.baqel.bcore.manager.command.general.inventory.ClearInventoryCommand;
import xyz.baqel.bcore.manager.command.general.inventory.GiveCommand;
import xyz.baqel.bcore.manager.command.general.inventory.SkullCommand;
import xyz.baqel.bcore.manager.command.general.staff.*;
import xyz.baqel.bcore.manager.command.general.teleport.TeleportAllCommand;
import xyz.baqel.bcore.manager.command.general.teleport.TeleportCommand;
import xyz.baqel.bcore.manager.command.general.teleport.TeleportHereCommand;
import xyz.baqel.bcore.manager.command.punishment.*;
import xyz.baqel.bcore.manager.command.punishment.check.CheckCommand;
import xyz.baqel.bcore.manager.command.punishment.undo.UnBanCommand;
import xyz.baqel.bcore.manager.command.punishment.undo.UnMuteCommand;
import xyz.baqel.bcore.manager.command.ranks.AutoRankCommand;
import xyz.baqel.bcore.manager.command.ranks.GrantCommand;
import xyz.baqel.bcore.manager.command.ranks.GrantsCommand;
import xyz.baqel.bcore.manager.command.ranks.RankCommand;
import xyz.baqel.bcore.manager.command.general.gamemode.CreativeCommand;
import xyz.baqel.bcore.manager.command.general.gamemode.GameModeCommand;
import xyz.baqel.bcore.manager.command.general.gamemode.SpectatorCommand;
import xyz.baqel.bcore.manager.command.general.gamemode.SurvivalCommand;
import xyz.baqel.bcore.util.command.RegisterCommand;
import xyz.baqel.bcore.util.item.ItemDBi;
import xyz.baqel.bcore.util.item.SimpleItemDb;
import xyz.baqel.bcore.util.lang.HttpMojangLang;
import xyz.baqel.bcore.util.lang.Lang;
import xyz.baqel.bcore.util.lang.MojangLang;
import xyz.baqel.bcore.util.menu.MenuListener;
import xyz.baqel.bcore.util.object.handler.SignHandler;

public class Manager {

	@Getter 
	private bCore plugin;
	@Getter 
	private RegisterCommand registerCommand;
	private ItemDBi itemDb;
	private SignHandler signHandler;
    private MojangLang language;
	
	public Manager(bCore plugin) { this.plugin = plugin; }
	
	public void init() {
		this.registetPreManagers();
		this.registerListeners();
		this.registerCommands();
	}
	
	public void disable() { this.signHandler.cancelTasks(null); }
	
	private void registetPreManagers() {
		//example package: xyz.baqel.bcore.util.command.RegisterCommand
		this.registerCommand = new RegisterCommand();
		//example package: xyz.baqel.bcore.util.item.ItemDBi
		this.itemDb = new SimpleItemDb(this.plugin);
		//example package: xyz.baqel.bcore.util.lang.Lang
		this.language = new HttpMojangLang();
        try {
            Lang.initialize("en_US");
            //this.language.index("1.7.10", Locale.ENGLISH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //example package: me.nebrera.util.object.handler.SignHandler
        this.signHandler = new SignHandler(this.plugin);
	}
	
	private void registerListeners() {
		PluginManager pluginManager = this.plugin.getServer().getPluginManager();
		//Info in package: xyz.baqel.bcore.util.menu.MenuListener
		pluginManager.registerEvents(new MenuListener(), this.plugin);
	}
	
	private void registerCommands() {
		//example package: xyz.baqel.bcore.manager.command.*
		this.registerCommand.registerBukkitCommand(new ServerStatusCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new RequestCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new ReportCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new EnchantCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new ListCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new ClearInventoryCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new TeleportAllCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new TeleportCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new TeleportHereCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new StaffModeCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new ClearChatCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new SlowChatCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new MuteChatCommand(), this.plugin);
 		this.registerCommand.registerBukkitCommand(new ClearChatCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new TagCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new NicknameCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new ProfileCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new StaffChatCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new ToggleRequestCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new ToggleReportCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new VanishCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new GrantCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new GrantsCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new CheckCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new UnBanCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new UnMuteCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new BanCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new BlacklistCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new KickCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new MuteCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new TempBanCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new WarnCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new RankCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new AutoRankCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new GiveCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new SkullCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new GameModeCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new CreativeCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new SurvivalCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new SpectatorCommand(), this.plugin);
		this.registerCommand.registerBukkitCommand(new AdventureCommand(), this.plugin);
	}
	
	public ItemDBi getItemDb() { return itemDb; }
	
	public MojangLang getLanguage() { return language; }
	
	public SignHandler getSignHandler() { return signHandler; }
}
