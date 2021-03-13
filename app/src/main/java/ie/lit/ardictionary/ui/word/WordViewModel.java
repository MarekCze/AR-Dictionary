package ie.lit.ardictionary.ui.word;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.repository.WordRepository;

public class WordViewModel extends AndroidViewModel {
    private WordRepository wordRepository;
    private MutableLiveData<Word> wordMutableLiveData;
    private MutableLiveData<List<Word>> wordListMutableLiveData;

    public WordViewModel(@NonNull Application application) {
        super(application);

        wordRepository = new WordRepository(application);
        wordMutableLiveData = wordRepository.getWordMutableLiveData();
        wordListMutableLiveData = wordRepository.getWordListMutableLiveData();
    }

    public MutableLiveData<Word> getWordMutableLiveData() {
        return wordMutableLiveData;
    }

    public MutableLiveData<List<Word>> getWordListMutableLiveData() {
        return wordListMutableLiveData;
    }

    public void getWordDefinition(Bitmap screenshot, int x, int y){
        wordRepository.getWordDefinition(screenshot, x, y);
    }

    public void getNotebookWords(String notebookId){
        wordRepository.getNotebookWords(notebookId);
    }
}
