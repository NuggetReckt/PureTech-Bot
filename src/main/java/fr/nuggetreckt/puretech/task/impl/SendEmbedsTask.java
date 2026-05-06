package fr.nuggetreckt.puretech.task.impl;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.task.Task;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.selections.SelectOption;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.awt.*;
import java.util.Date;

public class SendEmbedsTask extends Task {

    private final PureTech instance;

    public SendEmbedsTask(PureTech instance) {
        super(10);

        this.instance = instance;
    }

    @Override
    protected void setup() {

    }

    @Override
    protected void execute() {
        MessageChannel roleChannel = instance.getConfigHandler().getConfig().getRoleChannel();
        MessageChannel verifyChannel = instance.getConfigHandler().getConfig().getVerifyChannel();

        roleEmbedSender(roleChannel);
        verifyEmbedSender(verifyChannel);
    }

    private void roleEmbedSender(MessageChannel takeRoleChannel) {
        MessageHistory history = MessageHistory.getHistoryFromBeginning(takeRoleChannel).complete();
        int messages = history.getRetrievedHistory().size();

        if (messages >= 1) return;

        EmbedBuilder takeRoleEmbed = new EmbedBuilder();

        takeRoleEmbed.setTitle("\uD83D\uDCCC ・ Rôles")
            .setDescription("Sélectionne les rôles à l'aide du menu déroulant ci-dessous pour avoir des pings personnalisés et des rôles qui te correspondent !")
            .addField("__Mentions__", """
                \uD83D\uDCCA ・ Sondages
                """, true)
            .addField("__Pour toi__", """
                ✈️ ・ Pilote de ligne
                🛩️ ・ Pilote de chasse
                🪂 ・ Passionné d'aviation
                🛠️ ・ Ingénieur
                🚀 ・ PureTech Owner
                """, true)
            .setColor(new Color(255, 255, 255, 1))
            .setFooter("1.2L PureTech - NuggetReckt", "https://cdn.discordapp.com/app-icons/1323351003713634304/ca9d46eba62afa52b42e2b3392c6f531.png?size=256")
            .setTimestamp(new Date().toInstant());

        takeRoleChannel.sendMessageEmbeds(takeRoleEmbed.build())
            .addComponents(
                ActionRow.of(
                    StringSelectMenu.create("roles")
                        .setPlaceholder("Sélectionne un rôle dans la liste")
                        .setMaxValues(25)
                        .addOptions(
                            SelectOption.of("Ping Sondages", "ping_polls").withDescription("Choisi ce rôle si tu souhaites être notifié lors des sondages").withEmoji(Emoji.fromFormatted("\uD83D\uDCCA")),
                            SelectOption.of("Pilote de ligne", "airline_pilot").withEmoji(Emoji.fromFormatted("U+2708")),
                            SelectOption.of("Pilote de chasse", "fighter_pilot").withEmoji(Emoji.fromFormatted("U+1F6E9")),
                            SelectOption.of("Passionné d'aviation", "aviation_enthusiast").withEmoji(Emoji.fromFormatted("U+1FA82")),
                            SelectOption.of("Ingénieur", "engineer").withEmoji(Emoji.fromFormatted("U+1F6E0")),
                            SelectOption.of("PureTech Owner", "puretech_owner").withEmoji(Emoji.fromFormatted("U+1F680"))
                        )
                        .build()
                )
            )
            .queue();
    }

    private void verifyEmbedSender(MessageChannel verifyChannel) {
        MessageHistory history = MessageHistory.getHistoryFromBeginning(verifyChannel).complete();
        int messages = history.getRetrievedHistory().size();

        if (messages >= 1) return;

        EmbedBuilder verifyEmbed = new EmbedBuilder();

        verifyEmbed.setTitle("\uD83D\uDEE1 ・ Vérification")
            .setDescription("Bienvenue sur le discord de la communauté de **Craftime** !\nVérifie-toi en cliquant sur le bouton ci-dessous.")
            .setImage("https://media.discordapp.net/attachments/1099493674582286336/1501279284608897024/image.jpg?ex=69fb7ec9&is=69fa2d49&hm=cfbe1318aec6acbe35ae2bf69810693f6a03f9b51e049760318fbd96d4fc40d0&=&format=webp&width=812&height=930")
            .setColor(new Color(255, 255, 255, 1))
            .setFooter("1.2L PureTech - NuggetReckt", "https://cdn.discordapp.com/app-icons/1323351003713634304/ca9d46eba62afa52b42e2b3392c6f531.png?size=256")
            .setTimestamp(new Date().toInstant());

        verifyChannel.sendMessageEmbeds(verifyEmbed.build())
            .addComponents(
                ActionRow.of(
                    Button.primary("VERIFY", "Acceder au Discord").withEmoji(Emoji.fromFormatted("\uD83D\uDEE1"))
                )
            )
            .queue();
    }
}
