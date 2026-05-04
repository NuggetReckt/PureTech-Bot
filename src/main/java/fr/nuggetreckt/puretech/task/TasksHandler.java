package fr.nuggetreckt.puretech.task;

import fr.nuggetreckt.puretech.PureTech;
import fr.nuggetreckt.puretech.task.impl.ChangeStatusTask;
import fr.nuggetreckt.puretech.task.impl.SendEmbedsTask;

import java.util.ArrayList;
import java.util.List;

public class TasksHandler {

    private final PureTech instance;

    private final List<Task> tasks;

    public TasksHandler(PureTech instance) {
        this.instance = instance;
        this.tasks = new ArrayList<>();
        setupTasks();
    }

    private void setupTasks() {
        setupTask(new ChangeStatusTask(instance));
        setupTask(new SendEmbedsTask(instance));
    }

    public void runTasks() {
        for (Task task : tasks) {
            task.launch();
        }
    }

    public void stopTasks() {
        int id = 0;

        for (Task task : tasks) {
            if (task.isRunning()) {
                int attempts = 0;

                while (true) {
                    long startTime = System.currentTimeMillis();

                    instance.getLogger().info("Waiting for task #{} ({}) to finish.", id, task.getClass().getName());
                    if (!task.isRunning()) {
                        task.stop();
                        break;
                    } else if (attempts > 10) {
                        instance.getLogger().warn("Task #{} ({}) failed to stop after 10 attempts. Stopping task...", id, task.getClass().getName());
                        task.stop();
                        break;
                    }

                    long elapsed = System.currentTimeMillis() - startTime;
                    long sleepTime = 1000 - elapsed;
                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    attempts++;
                }
                continue;
            }
            task.stop();
            id++;
        }
    }

    private void setupTask(Task task) {
        tasks.add(task);
    }
}
