package fr.nuggetreckt.puretech.listener;

import fr.nuggetreckt.puretech.PureTech;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ShutdownListener extends ListenerAdapter {

    private final PureTech instance;

    public ShutdownListener(PureTech instance) {
        this.instance = instance;
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        instance.getLogger().info("Shutting down...");
        instance.getLogger().info("Stopping tasks...");
        instance.getTasksHandler().stopTasks();
        instance.getLogger().info("Tasks stopped.");
        instance.getLogger().info("Goodbye :)");
    }
}