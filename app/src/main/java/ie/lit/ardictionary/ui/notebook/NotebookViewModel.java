package ie.lit.ardictionary.ui.notebook;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ie.lit.ardictionary.model.Notebook;
import ie.lit.ardictionary.repository.NotebookRepository;

public class NotebookViewModel extends AndroidViewModel {
    private NotebookRepository notebookRepository;
    private MutableLiveData<List<Notebook>> notebookListMutableLiveData;

    public NotebookViewModel(@NonNull Application application) {
        super(application);

        notebookRepository = new NotebookRepository(application);
        notebookListMutableLiveData = notebookRepository.getNotebookListMutableLiveData();
    }

    public void getUserNotebooks(){
        notebookRepository.getUserNotebooks();
    }

    public MutableLiveData<List<Notebook>> getNotebookListMutableLiveData() {
        return notebookListMutableLiveData;
    }
}
