package fr.nuggetreckt.puretech.listener;

import fr.nuggetreckt.puretech.PureTech;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StringSelectListener extends ListenerAdapter {

    private final PureTech instance;

    public StringSelectListener(PureTech instance) {
        this.instance = instance;
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("roles")) {
            List<Role> roles = new ArrayList<>();
            Member member = event.getMember();

            for (int i = 0; i < event.getValues().size(); i++) {
                Role role = instance.getConfigHandler().getConfig().getRole(event.getValues().get(i));

                if (role == null) continue;
                roles.add(role);
            }

            if (member == null) {
                event.reply("> Une erreur est survenue.").setEphemeral(true).queue();
                return;
            }

            for (Role role : roles) {
                if (!member.getRoles().contains(role)) {
                    addRoleToMember(member, role);
                } else {
                    removeRoleFromMember(member, role);
                }
            }
            event.reply("> Tes rôles ont été mis à jour").setEphemeral(true)
                .queue();
        }
    }

    private void addRoleToMember(@NotNull Member member, Role role) {
        member.getGuild().addRoleToMember(member, role)
            .queue();
    }

    private void removeRoleFromMember(@NotNull Member member, Role role) {
        member.getGuild().removeRoleFromMember(member, role)
            .queue();
    }
}
