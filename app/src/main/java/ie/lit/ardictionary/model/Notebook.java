package ie.lit.ardictionary.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.List;

public class Notebook {
    private String name;
    private String uid;
    private String date;
    private DayOfWeek dayOfWeek;
    private List<Word> words;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notebook(){
        LocalDate localDate = LocalDate.now();
        this.dayOfWeek = localDate.getDayOfWeek();
        this.date = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notebook(String name, String uid, List<Word> words){
        this.name = name;
        this.uid = uid;
        this.words = words;
        LocalDate localDate = LocalDate.now();
        this.dayOfWeek = localDate.getDayOfWeek();
        this.date = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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

    public String getDate() {
        return date;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
