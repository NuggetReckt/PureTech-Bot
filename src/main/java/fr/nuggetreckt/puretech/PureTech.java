package fr.nuggetreckt.puretech;

import fr.nuggetreckt.puretech.button.ButtonListener;
import fr.nuggetreckt.puretech.command.CommandListener;
import fr.nuggetreckt.puretech.command.CommandManager;
import fr.nuggetreckt.puretech.config.ConfigHandler;
import fr.nuggetreckt.puretech.data.DataHandler;
import fr.nuggetreckt.puretech.guild.GuildsStatsHandler;
import fr.nuggetreckt.puretech.listener.*;
import fr.nuggetreckt.puretech.task.TasksHandler;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PureTech {

    @Getter
    private final PureTech instance;

    @Getter
    private final Logger logger;

    private JDA jda;

    @Getter
    private final ConfigHandler configHandler;
    @Getter
    private final DataHandler dataHandler;
    @Getter
    private final TasksHandler tasksHandler;
    @Getter
    private final CommandManager commandManager;
    @Getter
    private final GuildsStatsHandler guildsStatsHandler;

    private boolean isShuttingDown;

    public PureTech() {
        instance = this;
        logger = LoggerFactory.getLogger(PureTech.class);
        isShuttingDown = false;

        logger.info("Setting up configuration file...");

        this.configHandler = new ConfigHandler(this);
        this.dataHandler = new DataHandler(this);
        this.tasksHandler = new TasksHandler(this);
        this.commandManager = new CommandManager(this);
        this.guildsStatsHandler = new GuildsStatsHandler(this);

        logger.info("Config OK. Launching JDA...");

        Signal.handle(new Signal("INT"), signal -> {
            if (jda == null) {
                logger.info("Detected SIGINT before JDA is ready, Forcing shutting down.");
                System.exit(1);
            }
            if (isShuttingDown) {
                logger.info("Detected SIGINT twice, Forcing shutting down.");
                System.exit(1);
            }
            isShuttingDown = true;
            instance.getDataHandler().saveAndExit();
        });

        try {
            buildJDA();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void buildJDA() throws InterruptedException {
        jda = JDABuilder.createDefault(configHandler.getConfig().getToken())
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .enableIntents(GatewayIntent.GUILD_MESSAGES)
            .enableIntents(GatewayIntent.GUILD_PRESENCES)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)

            .addEventListeners(
                //Basic events
                new ReadyListener(this),
                new ShutdownListener(this),
                new MessageListener(this),
                new MemberJoinListener(this),
                new StringSelectListener(this),
                new AntiSpamListener(),

                //Commands/Buttons events
                new CommandListener(this),
                new ButtonListener(this)
            )
            .build();

        jda.awaitReady();
    }

    public Queue<Member> loadMembers() {
        Queue<Member> members = new ConcurrentLinkedDeque<>();

        configHandler.getConfig().getGuild().loadMembers().onSuccess(members::addAll);
        return members;
    }

    public JDA getJDA() {
        return jda;
    }

    public String getVersion() {
        return this.getClass().getPackage().getImplementationVersion();
    }
}
