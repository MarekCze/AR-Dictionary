package ie.lit.ardictionary.ui.gallery;

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

import ie.lit.ardictionary.R;
import ie.lit.ardictionary.ui.camera.CameraViewModel;

public class SearchResultFragment extends Fragment {

    private CameraViewModel cameraResultViewModel;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cameraResultViewModel = new ViewModelProvider(getActivity()).get(CameraViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_result, container, false);

        textView = root.findViewById(R.id.text_search_result);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView.setText(cameraResultViewModel.wordStr);
        //Log.w("word text", textView.getText().toString());

        // Create the observer which updates the UI.
        final Observer<String> wordObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView
                Log.w("Changed", "ouajsofdij");
                textView.setText(newName);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        cameraResultViewModel.getWord().observe(getViewLifecycleOwner(), wordObserver);

    }
}