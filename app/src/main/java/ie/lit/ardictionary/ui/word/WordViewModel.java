package ie.lit.ardictionary.ui.word;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.repository.WordRepository;

public class WordViewModel extends AndroidViewModel {
    private WordRepository wordRepository;
    private MutableLiveData<Word> wordMutableLiveData;

    public WordViewModel(@NonNull Application application) {
        super(application);

        wordRepository = new WordRepository(application);
        wordMutableLiveData = wordRepository.getWordMutableLiveData();
    }

    public MutableLiveData<Word> getWordMutableLiveData() {
        return wordMutableLiveData;
    }

    public void getWordDefinition(Bitmap screenshot, int x, int y){
        wordRepository.getWordDefinition(screenshot, x, y);
    }

}
