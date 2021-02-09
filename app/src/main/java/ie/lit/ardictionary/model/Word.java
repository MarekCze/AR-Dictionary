package ie.lit.ardictionary.model;

import java.util.GregorianCalendar;
import java.util.List;

public class Word {
    private String id;
    private String word;
    private List<String> definitions;
    private List<String> shortDefs;
    private String pronunciation;
    private String audio;
    private boolean isOffensive;
    private GregorianCalendar date;
    private List<Word> homographs;

    public Word(){
        this.date = new GregorianCalendar();
    }

    public Word(String id, String word, List<String> definitions, boolean isOffensive){
        this.id = id;
        this.word = word;
        this.definitions = definitions;
        this.isOffensive = isOffensive;
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

    public List<String> getShortDefs() {
        return shortDefs;
    }

    public void setShortDefs(List<String> shortDefs) {
        this.shortDefs = shortDefs;
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

    public boolean isOffensive() {
        return isOffensive;
    }

    public void setOffensive(boolean offensive) {
        isOffensive = offensive;
    }

    public List<Word> getHomographs() {
        return homographs;
    }

    public void setHomographs(List<Word> homographs) {
        this.homographs = homographs;
    }
}
