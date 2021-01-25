package ie.lit.ardictionary.ui.camera;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraViewModel extends ViewModel {

    private MutableLiveData<String> word;
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
        isPermissionsGranted = new MutableLiveData<>();
        isPermissionsGranted.setValue(false);
    }

    public MutableLiveData<String> getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word.postValue(word);
    }
}