package fr.nuggetreckt.puretech.listener;

import fr.nuggetreckt.puretech.button.impl.RoleSelect;
import fr.nuggetreckt.puretech.button.impl.RoleSelectButton;
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

    public StringSelectListener() {
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("roles")) {
            List<Role> toAdd = new ArrayList<>();
            List<Role> toRemove = new ArrayList<>();
            Set<String> selected = new HashSet<>(event.getValues());
            Member member = event.getMember();

            for (RoleSelect r : RoleSelectButton.ROLES) {
                Role role = r.getRole();

                if (role == null) continue;

                boolean wants = selected.contains(r.getConfigId());
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
