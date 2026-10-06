package fr.nuggetreckt.puretech.button.impl;

import fr.nuggetreckt.puretech.button.Button;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.selections.SelectOption;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class RoleSelectButton extends Button {
    public static final Set<RoleSelect> ROLES = new HashSet<>() {{
        add(new RoleSelect("ping_polls", "Ping Sondages", "\uD83D\uDCCA", "Choisi ce rôle si tu souhaites être notifié lors des sondages"));
        add(new RoleSelect("airline_pilot", "Pilote de ligne", "✈️"));
        add(new RoleSelect("fighter_pilot", "Pilote de chasse", "\uD83D\uDEE9️"));
        add(new RoleSelect("aviation_enthusiast", "Passionné d'aviation", "U+1FA82"));
        add(new RoleSelect("engineer", "Ingénieur", "U+1F6E0"));
        add(new RoleSelect("puretech_owner", "PureTech Owner", "U+1F680"));
    }};

    public RoleSelectButton() {
        super("role_select");
    }

    @Override
    public void execute(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("ROLE_SELECT")) {
            Member member = event.getMember();

            if (member == null) return;

            StringSelectMenu.Builder menu = StringSelectMenu.create("roles")
                .setPlaceholder("Choisis tes rôles")
                .setMinValues(0)
                .setMaxValues(ROLES.size());

            ROLES.forEach((r) -> {
                Role role = r.getRole();
                if (role == null) return;

                boolean hasRole = member.getRoles().contains(role);
                SelectOption option = SelectOption.of(r.getRoleName(), r.getConfigId())
                    .withEmoji(Emoji.fromFormatted(r.getEmoji()))
                    .withDefault(hasRole);

                if (r.getDescription() != null)
                    option = option.withDescription(r.getDescription());

                menu.addOptions(option);
            });

            event.reply("> Sélectionne un ou plusieurs rôles dans la liste ci-dessous")
                .addComponents(ActionRow.of(menu.build()))
                .setEphemeral(true)
                .queue();
        }
    }
}