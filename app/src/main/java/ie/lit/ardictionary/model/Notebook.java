package ie.lit.ardictionary.model;

import java.util.GregorianCalendar;
import java.util.List;

public class Notebook {
    private String name;
    private String uid;
    private GregorianCalendar date;
    private List<Word> words;

    public Notebook(){
        this.date = new GregorianCalendar();
    }

    public Notebook(String name, String uid, List<Word> words){
        this.name = name;
        this.uid = uid;
        this.words = words;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}
