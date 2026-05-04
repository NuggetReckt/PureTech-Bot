package fr.nuggetreckt.puretech.command;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class Command {

    @Getter
    private final String name;
    @Getter
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void execute(SlashCommandInteractionEvent event);
}