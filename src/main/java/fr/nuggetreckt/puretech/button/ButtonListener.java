package fr.nuggetreckt.puretech.button;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.button.impl.VerifyButton;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ButtonListener extends ListenerAdapter {

    private final HashMap<String, Button> buttons;
    private final PureTech instance;

    public ButtonListener(PureTech instance) {
        this.buttons = new HashMap<>();
        this.instance = instance;

        registerButtons();
    }

    private void registerButtons() {
        registerButton(new VerifyButton(instance));
    }

    private void registerButton(Button button) {
        buttons.put(button.getName(), button);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (String command : buttons.keySet()) {
            if (event.getComponentId().toLowerCase().startsWith(command)) {
                buttons.get(command).execute(event);
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
    }
}
