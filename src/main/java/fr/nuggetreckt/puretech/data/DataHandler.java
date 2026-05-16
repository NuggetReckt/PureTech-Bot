package fr.nuggetreckt.puretech.data;

import fr.nuggetreckt.puretech.PureTech;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataHandler {

    private final PureTech instance;

    private File dataFile;

    @Getter
    private final JSONCache jsonCache;

    public DataHandler(PureTech instance) {
        this.instance = instance;
        this.jsonCache = new JSONCache();
    }

    public void init() {
        dataFile = new File("data.json");
        open();
    }

    public void open() {
        if (!dataFile.exists()) {
            instance.getLogger().info("No data file found, creating one...");
            save();
            instance.getLogger().info("Data file created.");
        }
        instance.getLogger().info("Data file found, loading it...");

        JSONObject obj = null;

        try {
            obj = (JSONObject) new JSONParser().parse(new FileReader(dataFile));
        } catch (IOException e) {
            instance.getLogger().error("Failed to load file: {}", dataFile.getAbsolutePath());
        } catch (ParseException e) {
            instance.getLogger().error("Failed to parse file: {}", dataFile.getAbsolutePath());
        }
        if (obj == null) return;

        jsonCache.fromJSON(obj);
    }

    public void saveAndExit() {
        Thread thread = new Thread(() -> {
            instance.getLogger().info("Saving stats data...");
//            Queue<Member> members = instance.loadMembers();

            save();
            instance.getLogger().info("Stats saved.");
            instance.getJDA().shutdown();
            Thread.currentThread().interrupt();
        });
        thread.start();
    }

    public void save() {
        if (dataFile.exists()) {
            if (!dataFile.delete())
                instance.getLogger().warn("Failed to delete existing stats file");
        }
        try {
            if (!dataFile.createNewFile())
                throw new IOException("Failed to create new file");
            FileWriter fw = new FileWriter(dataFile.getName());

            fw.write(jsonCache.toJSON().toJSONString());
            fw.flush();
            fw.close();
        } catch (RuntimeException e) {
            instance.getLogger().error("Failed to write JSON object", e);
        } catch (IOException e) {
            instance.getLogger().error("Failed to create JSON file", e);
        }
    }
}
