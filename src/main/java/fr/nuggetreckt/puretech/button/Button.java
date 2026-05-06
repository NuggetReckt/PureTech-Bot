package fr.nuggetreckt.puretech.button;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public abstract class Button {

    @Getter
    private final String name;

    public Button(String name) {
        this.name = name;
    }

    public abstract void execute(ButtonInteractionEvent event);

}
