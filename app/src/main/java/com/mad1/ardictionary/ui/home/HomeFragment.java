package com.mad1.ardictionary.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.mad1.ardictionary.R;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    public static final String[] REQUIRED_PERMISSIONS = new String[] {Manifest.permission.CAMERA};
    public static final int REQUEST_CODE_PERMISSIONS = 1001;
    private HomeViewModel homeViewModel;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private Context context;
    private Executor analysisExecutor = Executors.newSingleThreadExecutor();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        context = getActivity();

        previewView = root.findViewById(R.id.previewView);
        previewView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bitmap screenshot = previewView.getBitmap();
                homeViewModel.setX((int)event.getX());
                homeViewModel.setY((int)event.getY());
                homeViewModel.runTextRecognition(screenshot);

                Log.w("TOUCHED", (int)event.getX() + "," + (int)event.getY());
                return true;
            }
        });

        if(allPermissionsGranted()){
            startCamera();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        return root;
    }

    public void startCamera(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
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

    public boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    } // END allPermissionsGranted method

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera();
            } else {
                Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    } // END onRequestPermissionResult method

}