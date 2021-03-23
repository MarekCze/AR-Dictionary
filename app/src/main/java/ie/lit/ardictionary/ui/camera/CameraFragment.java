package ie.lit.ardictionary.ui.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import ie.lit.ardictionary.MainActivity;
import ie.lit.ardictionary.R;
import ie.lit.ardictionary.api.DictionaryApi;
import ie.lit.ardictionary.api.DictionaryApiInterface;
import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.ui.gallery.SearchResultFragment;
import ie.lit.ardictionary.ui.word.WordFragment;
import ie.lit.ardictionary.ui.word.WordViewModel;
import ie.lit.ardictionary.utils.TextRecognitionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment {

    private WordViewModel wordViewModel;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private Context context;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_camera, container, false);
        // init WordViewModel
        wordViewModel = new ViewModelProvider(getActivity()).get(WordViewModel.class);
        // init dictionary API
        context = getActivity();

        return root;
    }
    // init previewView and set listeners once view is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previewView = root.findViewById(R.id.previewView);
        startCamera();

        previewView.setOnTouchListener(new View.OnTouchListener() {
            // create bitmap and send to text recognizer on touch
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Bitmap screenshot = previewView.getBitmap();
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    wordViewModel.getWordDefinition(screenshot, x, y);

                    Log.w("Touch Event", x + "," + y);
                }
                return true;
            }
        });

        // observe if a new word is set in ViewModel
        wordViewModel.getWordMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Word>() {
            @Override
            public void onChanged(Word word) {
                Fragment newFragment = new WordFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, newFragment, "Search Result Fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void startCamera(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                // unbind all views from cameraProvider (prevents black screen when switching back to camera fragment)
                cameraProvider.unbindAll();
                // bind again after everything has been unbound
                bindPreview(cameraProvider);
            } catch (InterruptedException | ExecutionException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(context));
    } // END startCamera method

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Rational aspectRatio = new Rational(previewView.getWidth(), previewView.getHeight());

        Log.d("Rational Value", aspectRatio.toString());
        // create preview, select camera and set previewView as the surface for this preview
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, preview);
    } // END bindPreview method
}