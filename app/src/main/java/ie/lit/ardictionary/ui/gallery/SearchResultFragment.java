package ie.lit.ardictionary.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ie.lit.ardictionary.R;
import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.ui.camera.CameraViewModel;

public class SearchResultFragment extends Fragment {

    private CameraViewModel cameraResultViewModel;
    private TextView word, definition, pronunciation;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cameraResultViewModel = new ViewModelProvider(getActivity()).get(CameraViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_result, container, false);

        word = root.findViewById(R.id.word);
        definition = root.findViewById(R.id.definition);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //textView.setText(cameraResultViewModel.wordStr);
        //Log.w("word text", textView.getText().toString());

        // Create the observer which updates the UI.
        final Observer<List<Word>> wordDefinitionObserver = new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                word.setText(words.get(0).getWord());
                definition.setText(words.get(0).getShortDefs().get(0));
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        cameraResultViewModel.getWordDefinition().observe(getViewLifecycleOwner(), wordDefinitionObserver);

    }
}