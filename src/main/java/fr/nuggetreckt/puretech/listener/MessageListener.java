package fr.nuggetreckt.puretech.listener;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.util.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageListener extends ListenerAdapter {

    private final PureTech instance;

    private final Random random;

    private int messageCount;

    public MessageListener(PureTech instance) {
        this.instance = instance;
        this.messageCount = 0;
        random = new Random();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        messageCount++;

        if (messageCount >= Config.TRIGGER_COUNT) {
            event.getChannel().sendMessage("> " + getRandomMessage()).queue();
            messageCount = 0;
        }
    }

    private String getRandomMessage() {
        List<String> messages = instance.getConfig().getMessages();
        return messages.get(random.nextInt(messages.size()));
    }
}
