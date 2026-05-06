package fr.nuggetreckt.puretech.config.entity;

import net.dv8tion.jda.api.JDA;

public abstract class JDAEntity {

    protected final String id;
    protected final String discordId;

    public JDAEntity(String id, String discordId) {
        this.id = id;
        this.discordId = discordId;
    }

    public String getId() {
        return id;
    }

    public String getDiscordId() {
        return discordId;
    }

    public abstract Object getJDAEntity(JDA jda);
}
