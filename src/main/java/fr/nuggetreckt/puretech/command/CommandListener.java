package fr.nuggetreckt.puretech.command;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.command.impl.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandListener extends ListenerAdapter {

    private final PureTech instance;

    private boolean hasLoaded;

    public CommandListener(@NotNull PureTech instance) {
        this.instance = instance;
        this.hasLoaded = false;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        MessageChannel botChannel = instance.getConfigHandler().getConfig().getBotChannel();
        HashMap<String, Command> commands = instance.getCommandManager().getCommands();

        if (!event.getChannel().equals(botChannel)) {
            event.reply("> Vous n'êtes pas dans le salon " + botChannel.getAsMention() + "!")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        for (String command : commands.keySet()) {
            if (event.getName().equalsIgnoreCase(command)) {
                commands.get(command).execute(event);
            }
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        if (hasLoaded) return;

        instance.getLogger().info("Registering commands...");
        instance.getCommandManager().registerCommands(event);
        hasLoaded = true;
    }
}
