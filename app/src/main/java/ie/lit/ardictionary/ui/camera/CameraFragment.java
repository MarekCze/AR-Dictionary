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

import ie.lit.ardictionary.R;
import ie.lit.ardictionary.api.DictionaryApi;
import ie.lit.ardictionary.api.DictionaryApiInterface;
import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.ui.gallery.SearchResultFragment;
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

    private CameraViewModel cameraViewModel;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Camera camera;
    private PreviewView previewView;
    private Context context;
    private Executor cameraExecutor = Executors.newSingleThreadExecutor();
    DictionaryApiInterface dictionaryApiInterface;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_camera, container, false);
        cameraViewModel = new ViewModelProvider(getActivity()).get(CameraViewModel.class);
        dictionaryApiInterface = DictionaryApi.getClient().create(DictionaryApiInterface.class);
        context = getActivity();

        cameraViewModel.isPermissionsGranted().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                    startCamera();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previewView = root.findViewById(R.id.previewView);
        previewView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Bitmap screenshot = previewView.getBitmap();
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    TextRecognitionHandler recognizer = new TextRecognitionHandler();
                    recognizer.runTextRecognition(screenshot, x, y, cameraViewModel);

                    Log.w("Touch Event", x + "," + y);
                }

                return true;
            }
        });

        if(cameraViewModel.isPermissionsGranted().getValue()){
            startCamera();
        }

        cameraViewModel.getWord().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String text) {
                Log.w("word changed", text);
                Call<List<Word>> words = dictionaryApiInterface.getWord(text, "0fb33c6f-c632-467c-8f72-1be2b685075b");

                words.enqueue(new Callback<List<Word>>() {
                    @Override
                    public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                        if(response.isSuccessful()){
                            List<Word> wordList = response.body();
                            cameraViewModel.setWordDefinition(wordList);
                            Fragment newFragment = new SearchResultFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frameLayout, newFragment, "findThisFragment")
                                    .addToBackStack(null)
                                    .commit();
                            for(Word w : wordList){
                                Log.w("TAGG", response.body().get(0).getId());
                            }
                        }

                        //Log.w("WORD TAG", wordList.get(0).getId());

                    }

                    @Override
                    public void onFailure(Call<List<Word>> call, Throwable t) {
                        Log.w("TAG", "Inside OnFailure");
                        call.cancel();
                    }
                });
            }
        });
    }

    public void startCamera(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();
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

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        /*ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(previewView.getWidth(), previewView.getHeight()))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();*/

        /*imageAnalysis.setAnalyzer(analysisExecutor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                runTextRecognition(image);
            } // END analyze
        }); // END setAnalyzer*/

        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, preview);
    } // END bindPreview method



}