package ie.lit.ardictionary.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
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

public class SearchResultFragment extends Fragment {

    private CameraViewModel cameraResultViewModel;
    private TextView wordTextView, definitionTextView, pronunciationTextView;
    private RecyclerView wordRecyclerView;
    private WordAdapter wordAdapter;
    private List<Word> words;
    private View root;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cameraResultViewModel = new ViewModelProvider(getActivity()).get(CameraViewModel.class);
        root = inflater.inflate(R.layout.fragment_search_result, container, false);
        words = new ArrayList();
        context = getActivity();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the observer which updates the UI.
        final Observer<Word> wordDefinitionObserver = new Observer<Word>() {
            @Override
            public void onChanged(Word word) {
                Word w = new Word();
                w.setWord("asdafs");
                w.setPronunciation("dfhh");
                Log.w("TAG","pronunciation: " + word.getPronunciation());

                words.add(word);

                wordRecyclerView = (RecyclerView) root.findViewById(R.id.word_list_recycler_view);
                wordAdapter = new WordAdapter(context, words);

                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
                wordRecyclerView.setLayoutManager(mLayoutManager);
                wordRecyclerView.setItemAnimator(new DefaultItemAnimator());
                wordRecyclerView.setAdapter(wordAdapter);

                //words.add(word);
                //wordAdapter.notifyDataSetChanged();
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        cameraResultViewModel.getWordDefinition().observe(getViewLifecycleOwner(), wordDefinitionObserver);

    }
}