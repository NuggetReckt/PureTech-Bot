package fr.nuggetreckt.puretech.listener;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.guild.GuildStats;
import fr.nuggetreckt.puretech.util.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class MessageListener extends ListenerAdapter {

    private final PureTech instance;
    private final Random random;

    public MessageListener(PureTech instance) {
        this.instance = instance;
        random = new Random();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        GuildStats guildStats = instance.getGuildStatsHandler().getGuildStatsById(event.getGuild().getIdLong());

        guildStats.setMessageCount(guildStats.getMessageCount() + 1);
        if (guildStats.getMessageCount() >= Config.TRIGGER_COUNT) {
            event.getChannel().sendMessage("> " + getRandomMessage(guildStats)).queue();
            guildStats.setMessageCount(0);
        }
    }

    private String getRandomMessage(@NotNull GuildStats stats) {
        List<String> messages = instance.getConfig().getMessages();
        String message = messages.get(random.nextInt(messages.size()));
        String lastMessage = stats.getLastMessage();

        while (lastMessage != null && lastMessage.equals(message)) {
            message = messages.get(random.nextInt(messages.size()));
        }
        stats.setLastMessage(message);
        return message;
    }
}
