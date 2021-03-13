package ie.lit.ardictionary.repository;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.lang.reflect.Type;

import ie.lit.ardictionary.api.DictionaryApi;
import ie.lit.ardictionary.api.DictionaryApiInterface;
import ie.lit.ardictionary.model.Notebook;
import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.utils.RootWordDeserializer;
import ie.lit.ardictionary.utils.WordDeserializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordRepository {
    private Application application;
    private FirebaseFirestore db;
    private CollectionReference notebookCollectionRef;
    private FirebaseUser user;
    private DictionaryApiInterface dictionaryApiInterface;
    private final static String TAG = "Word Repository";

    private MutableLiveData<Word> wordMutableLiveData;

    public WordRepository(Application application){
        this.application = application;

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        wordMutableLiveData = new MutableLiveData();
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public CollectionReference getNotebookCollectionRef() {
        return notebookCollectionRef;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public MutableLiveData<Word> getWordMutableLiveData() {
        return wordMutableLiveData;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveWord(Word word){
        String notebookName = word.getDate();
        Notebook notebook = new Notebook();
        notebook.setName(notebook.getDayOfWeek().toString());
        notebook.setUid(notebook.getDate());

        notebookCollectionRef = db.collection("Users/" + user.getUid() + "/Notebooks");


        notebookCollectionRef.document(notebook.getUid()).set(notebook).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                CollectionReference wordRef = notebookCollectionRef.document(notebook.getUid()).collection("Words");
                wordRef.add(word);
            }
        });
    }

    public void getWordDefinition(Bitmap screenshot, int x, int y){
        InputImage inputImage = InputImage.fromBitmap(screenshot,0);
        TextRecognizer recognizer = TextRecognition.getClient();

        Task<Text> result = recognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                String recognizedWord = processTextBlock(text, x, y);

                // GSON
                Type type = new TypeToken<String>(){}.getType();
                GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(type, new RootWordDeserializer());
                Gson gson = gsonBuilder.create();

                dictionaryApiInterface = DictionaryApi.getClient(gson).create(DictionaryApiInterface.class);
                Call<String> wordDefinition = dictionaryApiInterface.getRootWord(recognizedWord);

                wordDefinition.enqueue(getRootWordCallback());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w(TAG, e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private Callback<String> getRootWordCallback(){
        return new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String rootWord = response.body();
                    Log.w(TAG, rootWord);

                    // GSON
                    Type type = new TypeToken<Word>(){}.getType();
                    GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(type, new WordDeserializer());
                    Gson gson = gsonBuilder.create();

                    dictionaryApiInterface = DictionaryApi.getClient(gson).create(DictionaryApiInterface.class);

                    Call<Word> wordDefinition = dictionaryApiInterface.getWordDefinition(rootWord);
                    wordDefinition.enqueue(getWordDefinitionCallback());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.w(TAG, "Root word callback failed");
                Log.w(TAG, t.getMessage());
                t.printStackTrace();
                call.cancel();
            }
        };
    }

    private Callback<Word> getWordDefinitionCallback(){
        return new Callback<Word>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                if(response.isSuccessful()){
                    Word word = response.body();
                    Log.w(TAG, word.getDate());
                    wordMutableLiveData.postValue(word);
                    saveWord(word);
                }
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
                Log.w(TAG, "Word definition callback failed");
                Log.w(TAG, t.getMessage());
                t.printStackTrace();
                call.cancel();
            }
        };
    }

    private static String processTextBlock(Text result, int x, int y) {
        // [START mlkit_process_text_block]
        String resultText = result.getText();

        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                    if(elementFrame.contains(x,y)){
                        Log.w("ELEMENT TEXT", elementText);
                        return elementText;
                    }
                }
            }
        }
        return null;
    } // END processTextBlock method

}
