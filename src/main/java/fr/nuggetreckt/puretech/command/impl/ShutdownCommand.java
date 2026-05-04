package fr.nuggetreckt.puretech.command.impl;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class ShutdownCommand extends Command {

    private final PureTech instance;

    public ShutdownCommand(PureTech instance) {
        super("shutdown", "Shutdown le bot.");
        this.instance = instance;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        assert member != null;
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("> Vous n'avez pas la permission !").setEphemeral(true)
                    .queue();
            return;
        }
        event.reply("> Extinction du bot.").setEphemeral(true)
                .queue();

        instance.getJDA().shutdown();
    }
}
