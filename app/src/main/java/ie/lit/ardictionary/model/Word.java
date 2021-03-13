package ie.lit.ardictionary.model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Word {
    private String id;
    private String word;
    private List<String> definitions;
    private String pronunciation;
    private String audio;
    private List<String> sentences;
    private List<String> synonyms;
    private String date;
    private List<Word> words;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Word(){
        LocalDate localDate = LocalDate.now();
        this.date = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Word(String id, String word, List<String> definitions){
        this.id = id;
        this.word = word;
        this.definitions = definitions;
        LocalDate localDate = LocalDate.now();
        this.date = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void setSentences(List<String> sentences) {
        this.sentences = sentences;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }
}
