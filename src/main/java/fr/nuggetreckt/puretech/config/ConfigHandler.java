package fr.nuggetreckt.puretech.config;

import fr.nuggetreckt.puretech.PureTech;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class ConfigHandler {

    private final PureTech instance;
    private final File configFile;

    @Getter
    private Config config;

    public ConfigHandler(PureTech instance) {
        this.instance = instance;
        this.configFile = new File("config.json");
        init();
    }

    private void init() {
        if (!configFile.exists()) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.json");

            assert inputStream != null;
            try {
                if (!configFile.createNewFile()) {
                    throw new IOException("Failed to create new file");
                }
                FileOutputStream outputStream = new FileOutputStream(configFile);
                outputStream.write(inputStream.readAllBytes());
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                instance.getLogger().error("Failed to create config file: ", e);
            }
            instance.getLogger().info("Config file created. Please fill it and restart the bot.");
            System.exit(0);
        }
        loadFile();
    }

    public void reload() {
        instance.getLogger().info("Reloading config file...");
        config = null;
        loadFile();
        instance.getLogger().info("Config file reloaded.");
    }

    private void loadFile() {
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(configFile));
            config = new Config(instance, obj);

            if (!config.isValid()) {
                instance.getLogger().error("Config file is invalid: Maybe you forgot required fields? Please check the config file and restart the bot.");
                System.exit(1);
            }
        } catch (IOException | ParseException e) {
            instance.getLogger().error("Failed to read config file: ", e);
        }
    }
}
