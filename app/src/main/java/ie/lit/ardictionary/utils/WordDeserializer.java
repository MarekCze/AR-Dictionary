package ie.lit.ardictionary.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ie.lit.ardictionary.model.Word;

public class WordDeserializer implements JsonDeserializer<Word> {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Word deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        Word w = new Word();

        try{
            // get root object
            JsonObject results = jo.get("results").getAsJsonArray().get(0).getAsJsonObject();

            w.setId(results.get("id").getAsString());
            w.setWord(results.get("id").getAsString());

            // get entries object which contains most needed data
            JsonObject entries = results.get("lexicalEntries")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("entries")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject();

            // get pronunciations object
            JsonObject pronunciations = entries.get("pronunciations").getAsJsonArray().get(0).getAsJsonObject();
            w.setPronunciation(pronunciations.get("phoneticSpelling").getAsString());
            w.setAudio(pronunciations.get("audioFile").getAsString());

            // get senses object
            JsonObject senses = entries.get("senses").getAsJsonArray().get(0).getAsJsonObject();

            // loop through definitions array and set definitions
            List<String> definitions = new ArrayList();
            for(JsonElement el : senses.get("definitions").getAsJsonArray()){
                definitions.add(el.getAsString());
            }
            w.setDefinitions(definitions);

            // loop through examples array and set sentences
            if(senses.has("examples")){
                List<String> sentences = new ArrayList();
                for(JsonElement el : senses.get("examples").getAsJsonArray()){

                    sentences.add(el.getAsJsonObject().get("text").getAsString());
                }
                w.setSentences(sentences);
            }

            // loop through synonyms array and set synonyms
            if(senses.has("synonyms")){
                List<String> synonyms = new ArrayList();
                if(senses.get("synonyms").getAsJsonArray().size() > 5){
                    for(int i = 0; i < 5; i++){
                        synonyms.add(senses.get("synonyms").getAsJsonArray().get(i).getAsJsonObject().get("text").getAsString());
                    }
                } else {
                    for(JsonElement el : senses.get("synonyms").getAsJsonArray()){
                        synonyms.add(el.getAsJsonObject().get("text").getAsString());
                    }
                }

                w.setSynonyms(synonyms);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return w;
    }
}
