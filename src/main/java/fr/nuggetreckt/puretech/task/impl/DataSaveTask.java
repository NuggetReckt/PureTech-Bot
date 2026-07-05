package fr.nuggetreckt.puretech.task.impl;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.task.Task;

public class DataSaveTask extends Task {

    private final PureTech instance;

    public DataSaveTask(PureTech instance) {
        super(720 * 60, 720 * 60);

        this.instance = instance;
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void execute() {
        instance.getLogger().info("[AUTOSAVE] Saving data...");
        instance.getDataHandler().save();
        instance.getLogger().info("[AUTOSAVE] Data saved successfully");
    }
}
