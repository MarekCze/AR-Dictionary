package ie.lit.ardictionary.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import ie.lit.ardictionary.model.Notebook;
import ie.lit.ardictionary.model.Word;

public class NotebookRepository {
    private Application application;
    private FirebaseFirestore db;
    private CollectionReference notebookCollectionRef;
    private FirebaseUser user;
    private MutableLiveData<List<Notebook>> notebookListMutableLiveData;

    public NotebookRepository(Application application){
        this.application = application;

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        notebookCollectionRef = (CollectionReference) db.collection("Users/" + user.getUid() + "/Notebooks");
        notebookListMutableLiveData = new MutableLiveData<>();
    }

    public void getUserNotebooks(){
        Query query = notebookCollectionRef.orderBy("dateModified");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Notebook> notebooks = task.getResult().toObjects(Notebook.class);
                    notebookListMutableLiveData.postValue(notebooks);
                }
            }
        });
    }

    public MutableLiveData<List<Notebook>> getNotebookListMutableLiveData() {
        return notebookListMutableLiveData;
    }
}
