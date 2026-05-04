package fr.nuggetreckt.puretech.task;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a task to be executed repetitively or once
 */
public abstract class Task {

    /**
     * Delay in milliseconds before task is to be executed
     */
    private final long delay;
    /**
     * Time in milliseconds between successive task executions.
     */
    private final long executeInterval;

    /**
     * Define if the task is cancelled or not
     */
    private boolean isCancelled;

    /**
     * Define if the task is running or not
     */
    private boolean isRunning;

    /**
     * The type of the task
     */
    private final TaskType taskType;

    /**
     * The timer that launches the task
     */
    private final Timer timer;

    /**
     * Task abstract default constructor
     *
     * @param delay    Delay in seconds before task is to be executed
     * @param interval Time in seconds between successive task executions.
     * @param taskType Type of the task to be created
     */
    public Task(long delay, long interval, TaskType taskType) {
        this.timer = new Timer();

        this.delay = delay * 1000;
        this.executeInterval = interval * 1000;
        this.taskType = taskType;
        this.isCancelled = false;
        this.isRunning = false;
    }

    /**
     * Task abstract empty constructor
     */
    public Task() {
        this(0, 0, TaskType.EXECUTE_ONCE);
    }

    /**
     * Task abstract class constructor for repetitive tasks
     *
     * @param delay    Delay in seconds before task is to be executed
     * @param interval Time in seconds between successive task executions.
     */
    public Task(long delay, long interval) {
        this(delay, interval, TaskType.EXECUTE_REPEAT);
    }

    /**
     * Task abstract class constructor for simple tasks
     *
     * @param delay Delay in seconds before task is to be executed
     */
    public Task(long delay) {
        this(delay, 0, TaskType.EXECUTE_ONCE);
    }

    /**
     * Task additional setup before start
     */
    protected abstract void setup();

    /**
     * Task to execute
     */
    protected abstract void execute();

    /**
     * Launches task
     */
    public void launch() {
        setup();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isCancelled) return;

                isRunning = true;
                try {
                    execute();
                } catch (Throwable throwable) {
                    onExecutionError(throwable);
                } finally {
                    isRunning = false;
                }
            }
        };

        switch (taskType) {
            case EXECUTE_ONCE:
                timer.schedule(timerTask, delay);
                break;
            case EXECUTE_REPEAT:
                timer.scheduleAtFixedRate(timerTask, delay, executeInterval);
                break;
            default:
                break;
        }
    }

    /**
     * Stops task
     */
    public void stop() {
        timer.cancel();
    }

    /**
     * Checks if the task is cancelled. If the task is cancelled, it will not be executed.
     *
     * @return true if task is cancelled, false otherwise
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Enables or disables the task
     *
     * @param cancelled boolean value to set
     */
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    /**
     * Checks if the task is running.
     *
     * @return true if the task is running, false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Called when execute() throws.
     * Default behavior prints the stack trace and keeps the timer alive.
     *
     * @param throwable thrown error
     */
    protected void onExecutionError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
