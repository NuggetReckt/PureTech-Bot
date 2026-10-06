package fr.nuggetreckt.puretech.button.impl;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Role;

public class RoleSelect {
    @Getter
    private final String configId;
    @Getter
    @Setter
    private Role role;
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
        this.role = null;
        this.description = null;
    }

    public RoleSelect(String configId, String roleName, String emoji, String description) {
        this.configId = configId;
        this.roleName = roleName;
        this.emoji = emoji;
        this.role = null;
        this.description = description;
    }
}
