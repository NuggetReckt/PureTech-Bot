package fr.nuggetreckt.puretech.task.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.task.Task;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URL;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class YoutubePollerTask extends Task {

    private final PureTech instance;

    private String feedUrl;
    private MessageChannel announcementsChannel;
    private final Set<String> postedVideoIds;

    public YoutubePollerTask(PureTech instance) {
        super(5, 240);

        this.instance = instance;
        this.postedVideoIds = new HashSet<>();
    }

    @Override
    protected void setup() {
        feedUrl = "https://www.youtube.com/feeds/videos.xml?channel_id=" + instance.getConfigHandler().getConfig().getYoutubeChannelID();
        announcementsChannel = instance.getConfigHandler().getConfig().getAnnouncementsChannel();
    }

    @Override
    protected void execute() {
        SyndFeed feed;

        try {
            feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));
        } catch (Exception e) {
            instance.getLogger().error("Error while fetching YouTube feed", e);
            return;
        }

        for (SyndEntry entry : feed.getEntries().reversed()) {
            String videoId = entry.getUri();

            if (!postedVideoIds.contains(videoId)) {
                postedVideoIds.add(videoId);
                sendEmbedNotification(entry);
            }
        }
    }

    private void sendEmbedNotification(@NotNull SyndEntry entry) {
        String title = entry.getTitle();
        String url = entry.getLink();
        String videoId = entry.getUri().replace("yt:video:", "");
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg";
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("🎥 ・ Nouvelle vidéo !")
            .setDescription("**[" + title + "](" + url + ")**")
            .setImage(thumbnailUrl)
            .setColor(new Color(255, 0, 0, 1))
            .setFooter("1.2L PureTech - NuggetReckt", "https://cdn.discordapp.com/app-icons/1323351003713634304/ca9d46eba62afa52b42e2b3392c6f531.png?size=256")
            .setTimestamp(new Date().toInstant());

        announcementsChannel.sendMessage("@everyone")
            .setAllowedMentions(EnumSet.of(Message.MentionType.EVERYONE))
            .setEmbeds(builder.build())
            .addComponents(
                ActionRow.of(
                    Button.link(url, "Visionner la vidéo").withEmoji(Emoji.fromFormatted("📺"))
                )
            )
            .queue();
    }
}
