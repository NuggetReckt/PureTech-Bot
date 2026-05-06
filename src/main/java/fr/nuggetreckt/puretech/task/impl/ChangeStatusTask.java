package fr.nuggetreckt.puretech.task.impl;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.task.Task;
import net.dv8tion.jda.api.entities.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChangeStatusTask extends Task {

    private final PureTech instance;

    private final List<String> status;
    private final Random random;

    public ChangeStatusTask(PureTech instance) {
        super(0, 60);

        this.instance = instance;
        this.status = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    protected void setup() {
        addStatus("Dev avec ❤ par NuggetReckt");
        addStatus(String.format("%s v%s", instance.getJDA().getSelfUser().getName(), instance.getVersion()));
        addStatus("1.2L PureTech");
    }

    @Override
    public void execute() {
        int a = 0;

        if (status.isEmpty()) return;
        if (status.size() > 1)
            a = random.nextInt(status.size());
        instance.getJDA().getPresence().setActivity(Activity.playing(String.valueOf(status.get(a))));
    }

    private void addStatus(String message) {
        status.add(message);
    }
}