package ie.lit.ardictionary.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchResultViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SearchResultViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the searc result fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}