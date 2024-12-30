package fr.nuggetreckt.puretech;

import fr.nuggetreckt.puretech.listener.MessageListener;
import fr.nuggetreckt.puretech.listener.ReadyListener;
import fr.nuggetreckt.puretech.util.Config;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PureTech {

    private final PureTech instance;

    private final Logger logger;

    private JDA jda;

    private final Config config;

    public PureTech() {
        instance = this;
        logger = LoggerFactory.getLogger(PureTech.class);

        logger.info("Lancement du bot...");

        this.config = new Config();

        buildJDA();
    }

    private void buildJDA() {
        logger.info("Chargement de JDA...");
        jda = JDABuilder.createDefault(getToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        registerEvents();
    }

    private void registerEvents() {
        logger.info("Chargement des events...");

        //Basic events
        jda.addEventListener(new ReadyListener(this));
        jda.addEventListener(new MessageListener(this));

        /*
        //Register commands
        jda.addEventListener(new CommandManager());

        //Commands/Buttons events
        jda.addEventListener(new CommandListener(this));
        jda.addEventListener(new ButtonListener(this));
        */
    }

    private String getToken() {
        String token;
        Dotenv dotenv;

        logger.info("Récupération du token...");
        dotenv = Dotenv.configure()
                .directory("/env/")
                .filename(".env")
                .load();

        try {
            token = dotenv.get("DISCORD_TOKEN");
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
        return token;
    }


    public PureTech getInstance() {
        return instance;
    }

    public JDA getJDA() {
        return jda;
    }

    public Logger getLogger() {
        return logger;
    }

    public Config getConfig() {
        return config;
    }

    public String getVersion() {
        return this.getClass().getPackage().getImplementationVersion();
    }
}
