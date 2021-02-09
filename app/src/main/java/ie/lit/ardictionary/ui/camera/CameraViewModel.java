package ie.lit.ardictionary.ui.camera;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ie.lit.ardictionary.model.Word;

public class CameraViewModel extends ViewModel {

    private MutableLiveData<String> word;
    private MutableLiveData<List<Word>> wordDefinition;
    public String wordStr;
    private int x, y;
    private MutableLiveData<Boolean> isPermissionsGranted;

    public MutableLiveData<Boolean> isPermissionsGranted() {
        return isPermissionsGranted;
    }

    public void setPermissionsGranted(boolean permissionsGranted) {
        isPermissionsGranted.setValue(permissionsGranted);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public CameraViewModel() {
        word = new MutableLiveData<>();
        wordDefinition = new MutableLiveData<>();
        isPermissionsGranted = new MutableLiveData<>();
        isPermissionsGranted.setValue(false);
    }

    public MutableLiveData<String> getWord() {
        return word;
    }

    public MutableLiveData<List<Word>> getWordDefinition() {
        return wordDefinition;
    }

    public void setWord(String word) {
        this.word.postValue(word);
    }

    public void setWordDefinition(List<Word> wordDefinition) {
        this.wordDefinition.postValue(wordDefinition);
    }
}