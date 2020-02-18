package xyz.baqel.bcore;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.MemorySection;
import xyz.baqel.bcore.util.chat.ColorText;
import xyz.baqel.bcore.util.config.object.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class bCoreConfig {
    private static Map<String, String> stringMap;
    private static Map<String, Boolean> booleanMap;
    private static Map<String, List<String>> listMap;
    private static Map<String, Integer> integerMap;

    static void setUpData(final MemorySection config) {
        final Config lang = new Config(bCore.getPlugin(), "lang.yml");
        bCoreConfig.stringMap.put("key", config.getString("server.key"));
        bCoreConfig.stringMap.put("loading_data", lang.getString("lang.loading_data"));
        bCoreConfig.stringMap.put("only_players", lang.getString("lang.only_players"));
        bCoreConfig.stringMap.put("player_ping", lang.getString("lang.player_ping"));
        bCoreConfig.stringMap.put("staff_permission", config.getString("permissions.staff"));
        bCoreConfig.stringMap.put("donator_permission", config.getString("permissions.donator"));
        bCoreConfig.stringMap.put("donator_top_permission", config.getString("permissions.donator_top"));
        bCoreConfig.stringMap.put("no_permissions", lang.getString("lang.no_permissions"));
        bCoreConfig.stringMap.put("argument_not_found", lang.getString("lang.argument_not_found"));
        bCoreConfig.stringMap.put("clear_chat", lang.getString("lang.public_chat_cleared"));
        bCoreConfig.stringMap.put("mute_chat", lang.getString("lang.public_chat_muted"));
        bCoreConfig.stringMap.put("unmute_chat", lang.getString("lang.public_chat_unmuted"));
        bCoreConfig.stringMap.put("teamspeak", config.getString("server.teamspeak"));
        bCoreConfig.stringMap.put("discord", config.getString("server.discord"));
        bCoreConfig.stringMap.put("address", config.getString("server.server_ip"));
        bCoreConfig.stringMap.put("server_start", lang.getString("lang.server_start"));
        bCoreConfig.stringMap.put("server_stop", lang.getString("lang.server_stop"));
        bCoreConfig.stringMap.put("staff_chat_enabled", lang.getString("lang.staff_chat_enabled"));
        bCoreConfig.stringMap.put("staff_chat_disabled", lang.getString("lang.staff_chat_disabled"));
        bCoreConfig.stringMap.put("staff_chat_format", lang.getString("lang.staff_chat_format"));
        bCoreConfig.stringMap.put("staff_join", lang.getString("lang.staff_join"));
        bCoreConfig.stringMap.put("staff_leave", lang.getString("lang.staff_leave"));
        bCoreConfig.stringMap.put("request", lang.getString("lang.request"));
        bCoreConfig.stringMap.put("report", lang.getString("lang.report"));
        bCoreConfig.stringMap.put("server_lobby", config.getString("server.lobby_name"));
        bCoreConfig.stringMap.put("connecting_to", lang.getString("lang.connecting_to"));
        bCoreConfig.stringMap.put("rank_updated_message", lang.getString("lang.rank_updated"));
        bCoreConfig.stringMap.put("target_rank_updated", lang.getString("lang.target_rank_updated"));
        bCoreConfig.stringMap.put("rank_data_updated", lang.getString("lang.rank_data_updated"));
        bCoreConfig.stringMap.put("ranks_successfully_imported", lang.getString("lang.ranks_successfully_imported"));
        bCoreConfig.stringMap.put("whitelist_kick", lang.getString("lang.whitelist_kick"));
        bCoreConfig.stringMap.put("punishment_format", lang.getString("lang.punishment_format"));
        bCoreConfig.stringMap.put("lang_teamspeak", lang.getString("lang.teamspeak"));
        bCoreConfig.stringMap.put("lang_discord", lang.getString("lang.discord"));
        bCoreConfig.stringMap.put("message_to", lang.getString("lang.message_to"));
        bCoreConfig.stringMap.put("message_from", lang.getString("lang.message_from"));
        bCoreConfig.stringMap.put("tips_enabled", lang.getString("lang.tips_enabled"));
        bCoreConfig.stringMap.put("tips_disabled", lang.getString("lang.tips_disabled"));
        bCoreConfig.stringMap.put("sounds_enabled", lang.getString("lang.sounds_enabled"));
        bCoreConfig.stringMap.put("sounds_disabled", lang.getString("lang.sounds_disabled"));
        bCoreConfig.stringMap.put("messages_enabled", lang.getString("lang.messages_enabled"));
        bCoreConfig.stringMap.put("messages_disabled", lang.getString("lang.messages_disabled"));
        bCoreConfig.stringMap.put("skull_given", lang.getString("lang.skull_given"));
        bCoreConfig.stringMap.put("freeze_message", lang.getString("lang.freeze_message"));
        bCoreConfig.stringMap.put("target_frozen", lang.getString("lang.target_frozen"));
        bCoreConfig.stringMap.put("target_unfrozen", lang.getString("lang.target_unfrozen"));
        bCoreConfig.stringMap.put("sender_target_frozen", lang.getString("lang.sender_target_frozen"));
        bCoreConfig.stringMap.put("sender_target_unfrozen", lang.getString("lang.sender_target_unfrozen"));
        bCoreConfig.stringMap.put("target_frozen_alert", lang.getString("lang.target_frozen_alert"));
        bCoreConfig.stringMap.put("target_unfrozen_alert", lang.getString("lang.target_unfrozen_alert"));
        bCoreConfig.stringMap.put("cant_freeze_staff", lang.getString("lang.cant_freeze_staff"));
        bCoreConfig.stringMap.put("target_left_frozen", lang.getString("lang.target_left_frozen"));
        bCoreConfig.stringMap.put("staff_mode_enabled", lang.getString("lang.staff_mode_enabled"));
        bCoreConfig.stringMap.put("staff_mode_disabled", lang.getString("lang.staff_mode_disabled"));
        bCoreConfig.stringMap.put("vanish_enabled", lang.getString("lang.vanish_enabled"));
        bCoreConfig.stringMap.put("vanish_disabled", lang.getString("lang.vanish_disabled"));
        bCoreConfig.stringMap.put("freeze_chat_enabled", lang.getString("lang.freeze_chat_enabled"));
        bCoreConfig.stringMap.put("freeze_chat_disabled", lang.getString("lang.freeze_chat_disabled"));
        bCoreConfig.stringMap.put("player_muted_alert", lang.getString("lang.player_muted_alert"));
        bCoreConfig.stringMap.put("tag_removed", lang.getString("lang.tag_removed"));
        bCoreConfig.stringMap.put("color_removed", lang.getString("lang.color_removed"));
        bCoreConfig.stringMap.put("player_list", lang.getString("lang.player_list"));
        bCoreConfig.stringMap.put("request_sent", lang.getString("lang.request_sent"));
        bCoreConfig.stringMap.put("report_sent", lang.getString("lang.report_sent"));
        bCoreConfig.stringMap.put("whitelist_running", lang.getString("lang.whitelist_running"));
        bCoreConfig.stringMap.put("whitelist_unwhitelisted", lang.getString("lang.whitelist_unwhitelisted"));
        bCoreConfig.stringMap.put("whitelist_unwhitelisted_tooltip", lang.getString("lang.whitelist_unwhitelisted_tooltip"));
        bCoreConfig.booleanMap.put("announce_enabled", config.getBoolean("announce.enabled"));
        bCoreConfig.booleanMap.put("do_mob_spawn", config.getBoolean("server.do_mob_spawn"));
        bCoreConfig.booleanMap.put("do_leaves_decay", config.getBoolean("server.do_leaves_decay"));
        bCoreConfig.booleanMap.put("do_weather_change", config.getBoolean("server.do_weather_change"));
        bCoreConfig.booleanMap.put("do_thunder_change", config.getBoolean("server.do_thunder_change"));
        bCoreConfig.booleanMap.put("do_block_burn", config.getBoolean("server.do_block_burn"));
        bCoreConfig.booleanMap.put("do_block_spread", config.getBoolean("server.do_block_spread"));
        bCoreConfig.booleanMap.put("do_block_explode", config.getBoolean("server.do_entity_explode"));
        bCoreConfig.booleanMap.put("staff_mode", config.getBoolean("server.staff_mode"));
        bCoreConfig.booleanMap.put("hub_command", config.getBoolean("server.hub_command"));
        bCoreConfig.booleanMap.put("join_command", config.getBoolean("server.join_command"));
        bCoreConfig.booleanMap.put("do_world_load_clean", config.getBoolean("server.do_world_load_clean"));
        bCoreConfig.booleanMap.put("do_world_load_remove_entities", config.getBoolean("server.do_world_load_remove_entities"));
        bCoreConfig.booleanMap.put("cosmetics", config.getBoolean("server.cosmetics"));
        bCoreConfig.booleanMap.put("ranks", config.getBoolean("server.ranks"));
        bCoreConfig.listMap.put("announce_messages", config.getStringList("announce.messages"));
        bCoreConfig.listMap.put("filtered_words", config.getStringList("filtered_words"));
        bCoreConfig.listMap.put("allowed_links", config.getStringList("allowed_links"));
        bCoreConfig.listMap.put("disallowed_commands", config.getStringList("disallowed_commands"));
        bCoreConfig.integerMap.put("announce_delay", config.getInt("announce.delay"));
    }

    public static String getString(final String string) {
        if (bCoreConfig.stringMap.containsKey(string)) {
            return ColorText.translate(bCoreConfig.stringMap.get(string));
        }
        return "String: " + string + " could not be found.";
    }

    public static boolean getBoolean(final String string) {
        return bCoreConfig.booleanMap.containsKey(string) && bCoreConfig.booleanMap.get(string);
    }

    public static List<String> getArray(final String string) {
        if (bCoreConfig.listMap.containsKey(string)) {
            return ColorText.translate(bCoreConfig.listMap.get(string));
        }
        return new ArrayList<>();
    }

    public static int getInteger(final String string) {
        if (bCoreConfig.integerMap.containsKey(string)) {
            return bCoreConfig.integerMap.get(string);
        }
        return 0;
    }

    static {
        bCoreConfig.stringMap = new HashMap<>();
        bCoreConfig.booleanMap = new HashMap<>();
        bCoreConfig.listMap = new HashMap<>();
        bCoreConfig.integerMap = new HashMap<>();
    }
}
