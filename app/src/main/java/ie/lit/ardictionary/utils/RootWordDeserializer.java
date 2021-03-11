package ie.lit.ardictionary.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ie.lit.ardictionary.model.Word;

public class RootWordDeserializer  implements JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        String rootWord = "";

        try{
            JsonArray ja = (JsonArray) jo.get("results");
            rootWord = ja.get(0)
                    .getAsJsonObject()
                    .get("lexicalEntries")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("inflectionOf")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("text")
                    .getAsString();

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return rootWord;
    }
}
