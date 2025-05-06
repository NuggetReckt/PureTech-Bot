package fr.nuggetreckt.puretech.guild;

import fr.nuggetreckt.puretech.PureTech;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class GuildsStatsHandler {

    private final PureTech instance;

    private final HashMap<Long, GuildStats> guildsStats;

    public GuildsStatsHandler(PureTech instance) {
        this.instance = instance;
        this.guildsStats = new HashMap<>();
    }

    public void setupGuildsStats() {
        guildsStats.clear();
        for (Guild guild : instance.getJDA().getGuilds()) {
            guildsStats.put(guild.getIdLong(), new GuildStats());
        }
    }

    public HashMap<Long, GuildStats> getGuildsStats() {
        return guildsStats;
    }

    public GuildStats getGuildStatsById(long id) {
        return guildsStats.get(id);
    }
}
