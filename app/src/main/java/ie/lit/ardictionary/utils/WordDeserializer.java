package ie.lit.ardictionary.utils;

import android.util.Log;

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

public class WordDeserializer implements JsonDeserializer<Word> {

    @Override
    public Word deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray ja = json.getAsJsonArray();
        Word w = new Word();

        try{
            JsonObject jo = ja.get(0).getAsJsonObject();

            w.setId(jo.get("meta").getAsJsonObject().get("id").getAsString());
            w.setOffensive(jo.get("meta").getAsJsonObject().get("offensive").getAsBoolean());
            w.setWord(jo.get("meta").getAsJsonObject().get("stems").getAsJsonArray().get(0).getAsString());

            JsonObject hwiObject = jo.get("hwi").getAsJsonObject();
            if(hwiObject.has("prs")){
                w.setPronunciation(hwiObject.get("prs").getAsJsonArray().get(0).getAsJsonObject().get("mw").getAsString());
                w.setAudio(hwiObject.get("prs").getAsJsonArray().get(0).getAsJsonObject().get("sound").getAsJsonObject().get("audio").getAsString());
            }

            // set shortDefs array
            JsonArray shortDefs = jo.get("shortdef").getAsJsonArray();
            List<String> shortDefList = new ArrayList();
            for(JsonElement el : shortDefs){
                shortDefList.add(el.getAsString());
            }
            w.setShortDefs(shortDefList);

            // set definitions array
            //JsonArray definitions = jo.get("def").getAsJsonArray().get(0).getAsJsonObject().get("sseq").getAsJsonArray();
            //List<String> defs = new ArrayList();
            //for(JsonElement element : definitions){
            //    defs.add(element.getAsJsonArray().get(0).getAsJsonArray().get(1).getAsJsonObject().get("dt").getAsJsonArray().get(0).getAsJsonArray().get(1).getAsString());
            //}
            //w.setDefinitions(defs);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return w;
    }
}
