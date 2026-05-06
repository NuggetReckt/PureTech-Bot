package fr.nuggetreckt.puretech.config;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.config.entity.*;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {

    public final String CONFIG_VERSION = "0.1";

    private final PureTech instance;
    private final JSONObject config;

    @Getter
    private boolean isValid;

    @Getter
    private String token;
    @Getter
    private String guildId;
    @Getter
    private String configVersion;
    @Getter
    private boolean debug;
    @Getter
    private String youtubeChannelID;

    @Getter
    private long triggerCount;
    @Getter
    private final List<String> messages;

    private final List<ChannelConfig> channels;
    private final List<RoleConfig> roles;

    public Config(PureTech instance, JSONObject config) {
        this.instance = instance;
        this.config = config;
        this.channels = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.isValid = true;
        init();
    }

    private void init() {
        configVersion = (String) config.get("version");

        if (configVersion == null) {
            instance.getLogger().error("Config version is missing. Please update the config file.");
            isValid = false;
            return;
        } else if (!configVersion.equals(CONFIG_VERSION)) {
            int[] currentVersion = Arrays.stream(configVersion.split("\\.")).mapToInt(Integer::parseInt).toArray();
            int[] requiredVersion = Arrays.stream(CONFIG_VERSION.split("\\.")).mapToInt(Integer::parseInt).toArray();

            if (currentVersion.length != requiredVersion.length || currentVersion.length != 2) {
                instance.getLogger().error("Invalid config version format. Expected: {}, got: {}", CONFIG_VERSION, configVersion);
                isValid = false;
                return;
            }

            int comparison;

            if (currentVersion[0] != requiredVersion[0]) {
                comparison = Integer.compare(currentVersion[0], requiredVersion[0]);
            } else {
                comparison = Integer.compare(currentVersion[1], requiredVersion[1]);
            }

            if (comparison < 0) {
                instance.getLogger().warn("Config version is outdated. Please update the config file. Expected: {}, got: {}", CONFIG_VERSION, configVersion);
            } else if (comparison > 0) {
                instance.getLogger().error("Config version is too new. Please update the bot. Expected: {}, got: {}", CONFIG_VERSION, configVersion);
                isValid = false;
                return;
            }
        }

        token = (String) config.get("token");
        guildId = (String) config.get("guild_id");
        debug = (boolean) config.get("debug");
        triggerCount = (Long) config.get("trigger_count");
        youtubeChannelID = (String) config.get("youtube_channel_id");

        JSONArray jsonChannels = (JSONArray) config.get("channels");
        JSONArray jsonRoles = (JSONArray) config.get("roles");
        JSONArray jsonMessages = (JSONArray) config.get("messages");

        jsonChannels.forEach(m -> {
            JSONObject channel = (JSONObject) m;

            String id = (String) channel.get("id");
            String channelId = (String) channel.get("channel_id");

            if (channelId == null || channelId.isEmpty())
                isValid = false;

            channels.add(new ChannelConfig(id, channelId));
        });

        jsonRoles.forEach(m -> {
            JSONObject role = (JSONObject) m;

            String id = (String) role.get("id");
            String roleId = (String) role.get("role_id");

            if (roleId == null || roleId.isEmpty())
                isValid = false;

            roles.add(new RoleConfig(id, roleId));
        });

        jsonMessages.forEach(m -> messages.add((String) m));

        if (token == null || token.isEmpty()) isValid = false;
        if (guildId == null || guildId.isEmpty()) isValid = false;
    }

    public Guild getGuild() {
        return instance.getJDA().getGuildById(guildId);
    }

    public Role getMemberRole() {
        return getRole("member");
    }

    public MessageChannel getBotChannel() {
        return getChannel("bot");
    }

    public MessageChannel getVerifyChannel() {
        return getChannel("verify");
    }

    public MessageChannel getRoleChannel() {
        return getChannel("role");
    }

    public MessageChannel getAnnouncementsChannel() {
        return getChannel("announcements");
    }

    public MessageChannel getGeneralChannel() {
        return getChannel("general");
    }

    public MessageChannel getRulesChannel() {
        return getChannel("rules");
    }

    public Role getRole(String id) {
        RoleConfig role = getRoleConfig(id);
        if (role == null) return null;
        return (Role) role.getJDAEntity(instance.getJDA());
    }

    public MessageChannel getChannel(String id) {
        ChannelConfig channel = getChannelConfig(id);
        if (channel == null) return null;
        return (MessageChannel) channel.getJDAEntity(instance.getJDA());
    }

    @Nullable
    private ChannelConfig getChannelConfig(String id) {
        for (ChannelConfig channel : channels) {
            if (channel.getId().equals(id))
                return channel;
        }
        return null;
    }

    @Nullable
    private RoleConfig getRoleConfig(String id) {
        for (RoleConfig role : roles) {
            if (role.getId().equals(id))
                return role;
        }
        return null;
    }
}
