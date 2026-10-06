package fr.nuggetreckt.puretech.button.impl;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.button.Button;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.selections.SelectOption;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

class RoleSelect {
    @Getter
    private final String configId;
    @Getter
    @Setter
    private String roleId;
    @Getter
    private final String roleName;
    @Getter
    private final String emoji;
    @Getter
    private final String description;

    public RoleSelect(String configId, String roleName, String emoji) {
        this.configId = configId;
        this.roleName = roleName;
        this.emoji = emoji;
        this.roleId = null;
        this.description = null;
    }

    public RoleSelect(String configId, String roleName, String emoji, String description) {
        this.configId = configId;
        this.roleName = roleName;
        this.emoji = emoji;
        this.roleId = null;
        this.description = description;
    }
}

public class RoleSelectButton extends Button {

    private final PureTech instance;

    private static final Set<RoleSelect> ROLES = new HashSet<>() {{
        add(new RoleSelect("ping_polls", "Ping Sondages", "\uD83D\uDCCA", "Choisi ce rôle si tu souhaites être notifié lors des sondages"));
        add(new RoleSelect("fighter_pilot", "Pilote de chasse", "U+1FA82"));
        add(new RoleSelect("aviation_enthusiast", "Passionné d'aviation", "U+1FA82"));
        add(new RoleSelect("engineer", "Ingénieur", "U+1F6E0"));
        add(new RoleSelect("puretech_owner", "PureTech Owner", "U+1F680"));
    }};

    public RoleSelectButton(PureTech instance) {
        super("role_select");

        this.instance = instance;

        
        // TODO: To execute at onReady after JDA launches 
        for (RoleSelect r : ROLES) {
            r.setRoleId(instance.getConfigHandler().getConfig().getRole(r.getConfigId()).getId());
        }
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
                Role role = event.getGuild().getRoleById(r.getRoleId());
                if (role == null) return;

                boolean hasRole = member.getRoles().contains(role);
                if (r.getDescription() == null) {
                    menu.addOptions(
                        SelectOption.of(r.getRoleName(), r.getConfigId())
                            .withEmoji(Emoji.fromFormatted(r.getEmoji()))
                            .withDefault(hasRole)
                    );
                } else {
                    menu.addOptions(
                        SelectOption.of(r.getRoleName(), r.getConfigId())
                            .withDescription(r.getDescription())
                            .withEmoji(Emoji.fromFormatted(r.getEmoji()))
                            .withDefault(hasRole)
                    );
                }
            });

            event.reply("| Sélectionne un ou plusieurs rôles dans la liste ci-dessous")
                .addComponents(ActionRow.of(menu.build()))
                .setEphemeral(true)
                .queue();
        }
    }
}