package fr.nuggetreckt.puretech.command;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.command.impl.ReloadCommand;
import fr.nuggetreckt.puretech.command.impl.ShutdownCommand;
import lombok.Getter;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    @Getter
    private final HashMap<String, Command> commands;
    private final List<CommandData> commandData;

    private final PureTech instance;

    public CommandManager(@NotNull PureTech instance) {
        this.instance = instance;
        this.commands = new HashMap<>();
        this.commandData = new ArrayList<>();
    }

    public void registerCommands(@NotNull GuildReadyEvent event) {
        registerCommand(new ShutdownCommand(instance));
        registerCommand(new ReloadCommand(instance));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
        commandData.add(Commands.slash(command.getName(), command.getDescription()));
    }
}
