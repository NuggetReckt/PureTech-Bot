package fr.nuggetreckt.puretech.config.entity;

import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

public class RoleConfig extends JDAEntity {

    public RoleConfig(String id, String discordId) {
        super(id, discordId);
    }

    @Override
    public Object getJDAEntity(@NotNull JDA jda) {
        return jda.getRoleById(discordId);
    }
}
