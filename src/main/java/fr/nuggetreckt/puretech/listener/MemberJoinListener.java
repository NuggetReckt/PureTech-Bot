package fr.nuggetreckt.puretech.listener;

import fr.nuggetreckt.puretech.PureTech;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MemberJoinListener extends ListenerAdapter {

    private final PureTech instance;

    public MemberJoinListener(PureTech instance) {
        this.instance = instance;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) return;

        Member member = event.getMember();
        MessageChannel generalChannel = instance.getConfigHandler().getConfig().getGeneralChannel();
        MessageChannel rulesChannel = instance.getConfigHandler().getConfig().getRulesChannel();
        MessageChannel rolesChannel = instance.getConfigHandler().getConfig().getRoleChannel();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setDescription(String.format("""
            👋 Bienvenue sur le discord communautaire de **Craftime**, %s !
            Passe voir les %s et va prendre tes roles dans %s !
            """, member.getAsMention(), rulesChannel.getAsMention(), rolesChannel.getAsMention()));

        generalChannel.sendMessageEmbeds(builder.build())
            .queue();
    }
}
