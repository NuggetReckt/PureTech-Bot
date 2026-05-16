package fr.nuggetreckt.puretech.data;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class JSONCache {

    @Getter
    private final Set<String> notifiedVideosIds;

    public JSONCache() {
        this.notifiedVideosIds = new HashSet<>();
    }

    public void fromJSON(@NotNull JSONObject json) {
        notifiedVideosIds.clear();

        JSONArray notifiedVideosIdsJSON = (JSONArray) json.get("notifiedVideosIds");
        notifiedVideosIdsJSON.forEach(id -> notifiedVideosIds.add((String) id));
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        JSONArray notifiedVideosIdsJSON = new JSONArray();

        notifiedVideosIdsJSON.addAll(notifiedVideosIds);

        json.put("lastUpdated", LocalTime.now().toString());
        json.put("notifiedVideosIds", notifiedVideosIdsJSON);
        return json;
    }
}
