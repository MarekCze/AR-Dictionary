package ie.lit.ardictionary.ui.word;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ie.lit.ardictionary.R;
import ie.lit.ardictionary.adapter.WordAdapter;
import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.ui.camera.CameraViewModel;

public class WordFragment extends Fragment {
    private WordViewModel wordViewModel;
    private RecyclerView wordRecyclerView;
    private WordAdapter wordAdapter;
    private List<Word> words;
    private String notebookId;
    private View root;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wordViewModel = new ViewModelProvider(getActivity()).get(WordViewModel.class);
        root = inflater.inflate(R.layout.fragment_word, container, false);
        words = new ArrayList();
        context = getActivity();

        if(getArguments() != null){
            Log.w("Word Fragment", "Inside if statement onCreateView");
            notebookId = getArguments().getString("notebookId");
            wordViewModel.getNotebookWords(notebookId);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the observer which updates the UI.
        final Observer<Word> wordDefinitionObserver = new Observer<Word>() {
            @Override
            public void onChanged(Word word) {
                Log.w("TAG","audio: " + word.getAudio());

                words.add(word);
                buildRecyclerView(words);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        wordViewModel.getWordMutableLiveData().observe(getViewLifecycleOwner(), wordDefinitionObserver);

        final Observer<List<Word>> wordListObserver = new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                Log.w("Word Fragment", "Inside onChanged");
                buildRecyclerView(words);
            }
        };

        wordViewModel.getWordListMutableLiveData().observe(getViewLifecycleOwner(), wordListObserver);
    }

    private void buildRecyclerView(List<Word> wordList){
        wordRecyclerView = (RecyclerView) root.findViewById(R.id.word_list_recycler_view);
        wordAdapter = new WordAdapter(context, wordList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        wordRecyclerView.setLayoutManager(mLayoutManager);
        wordRecyclerView.setItemAnimator(new DefaultItemAnimator());
        wordRecyclerView.setAdapter(wordAdapter);
    }
}
