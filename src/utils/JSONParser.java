package utils;

import namedEntities.NamedEntity;
import namedEntities.PersonEntity;
import namedEntities.LocationEntity;
import namedEntities.OrganizationEntity;
import namedEntities.OtherEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONParser {

    static public List<FeedsData> parseJsonFeedsData(String jsonFilePath) throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        List<FeedsData> feedsList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String label = jsonObject.getString("label");
            String url = jsonObject.getString("url");
            String type = jsonObject.getString("type");
            feedsList.add(new FeedsData(label, url, type));
        }
        return feedsList;
    }

    static public HashMap<String, NamedEntity> parseJsonDict(String jsonFilePath) throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONArray jsonArray = new JSONArray(jsonData);

        List<NamedEntity> namedEntities = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            NamedEntity namedEntity = parseJsonNamedEntity(jsonObject);

            if (namedEntity != null) {
                namedEntities.add(namedEntity);
            }
        }

        var map = new HashMap<String, NamedEntity>();

        for (NamedEntity namedEntity : namedEntities) {
            for (String keyword : namedEntity.getKeywords()) {
                map.put(keyword, namedEntity);
            }
        }

        return map;
    }

    static private NamedEntity parseJsonNamedEntity(JSONObject jsonObject) throws  IOException {
        String label = jsonObject.getString("label");
        String category = jsonObject.getString("Category");
        JSONArray topicsJson = jsonObject.getJSONArray("Topics");
        JSONArray keywordsJson = jsonObject.getJSONArray("Keywords");

        var topics = new ArrayList<String>();
        for (var topic : topicsJson) {
            if (topic instanceof String) {
                topics.add((String)topic);
            }
        }

        var keywords = new ArrayList<String>();
        for (var keyword : keywordsJson) {
            if (keyword instanceof String) {
                keywords.add((String) keyword);
            }
        }

        return switch (category) {
            case "PERSON" -> new PersonEntity(label, topics, keywords, "");
            case "LOCATION" -> new LocationEntity(label, topics, keywords, "", 0, 0);
            case "ORGANIZATION" -> new OrganizationEntity(label, topics, keywords, "", "");
            case "OTHER" -> new OtherEntity(label, topics, keywords);
            default -> null;
        };
    }
}
