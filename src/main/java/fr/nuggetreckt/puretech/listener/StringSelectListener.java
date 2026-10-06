package fr.nuggetreckt.puretech.listener;

import fr.nuggetreckt.puretech.PureTech;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringSelectListener extends ListenerAdapter {

    private final PureTech instance;

    public StringSelectListener(PureTech instance) {
        this.instance = instance;
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("roles")) {
            List<Role> toAdd = new ArrayList<>();
            List<Role> toRemove = new ArrayList<>();
            List<Role> roles = new ArrayList<>();
            Set<String> selected = new HashSet<>(event.getValues());
            Member member = event.getMember();

            for (int i = 0; i < event.getValues().size(); i++) {
                Role role = instance.getConfigHandler().getConfig().getRole(event.getValues().get(i));

                if (role == null) continue;
                roles.add(role);
            }

            for (Role role : roles) {
                boolean wants = selected.contains(role.getId());
                boolean has = member.getRoles().contains(role);

                if (wants && !has) toAdd.add(role);
                else if (!wants && has) toRemove.add(role);
            }

            if (member == null) {
                event.reply("> Une erreur est survenue.").setEphemeral(true).queue();
                return;
            }

            event.deferEdit().queue();
            event.getGuild().modifyMemberRoles(member, toAdd, toRemove).queue(
                success -> event.getHook().editOriginal("> Tes rôles ont été mis à jour.")
                    .setComponents().queue(),
                error -> event.getHook().editOriginal("> Impossible de modifier tes rôles.")
                    .setComponents().queue()
            );
        }
    }
}
