package ie.lit.ardictionary.model;

import java.util.GregorianCalendar;
import java.util.List;

public class Word {
    private String id;
    private String word;
    private List<String> definitions;
    private String pronunciation;
    private String audio;
    private List<String> sentences;
    private List<String> synonyms;
    private GregorianCalendar date;

    public Word(){
        this.date = new GregorianCalendar();
    }

    public Word(String id, String word, List<String> definitions){
        this.id = id;
        this.word = word;
        this.definitions = definitions;
        this.date = new GregorianCalendar();
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
