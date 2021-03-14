package ie.lit.ardictionary.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class Notebook {
    private String name;
    private String uid;
    private String date;
    private LocalDate dateModified;
    private DayOfWeek dayOfWeek;
    private String style;

    private List<Word> words;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notebook(){
        LocalDate localDate = LocalDate.now();
        this.dayOfWeek = localDate.getDayOfWeek();
        this.date = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.style = setNotebookStyle();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notebook(String name, String uid, List<Word> words){
        this.name = name;
        this.uid = uid;
        this.words = words;
        LocalDate localDate = LocalDate.now();
        this.dayOfWeek = localDate.getDayOfWeek();
        this.date = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.style = setNotebookStyle();
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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

    private String setNotebookStyle(){
        String[] styles = {"green", "blue", "red", "purple", "yellow"};

        int num = new Random().nextInt((4 - 0) + 1) + 0;

        return styles[num];
    }
}
