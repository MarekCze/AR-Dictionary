package com.mad1.ardictionary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.Rational;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TextRecognitionHelper {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;

    private Executor analysisExecutor = Executors.newSingleThreadExecutor();
    private Context context;
    private int x = 0;
    private int y = 0;

    public TextRecognitionHelper(){}

    public TextRecognitionHelper(Context context, PreviewView previewView){
        this.context = context;
        this.previewView = previewView;
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

    public void runTextRecognition(Bitmap screenshot, int x, int y){
        InputImage inputImage = InputImage.fromBitmap(screenshot,0);
        TextRecognizer recognizer = TextRecognition.getClient();

        recognizer.process(inputImage)
                .addOnSuccessListener(text -> {
                    processTextBlock(text);
                }).addOnFailureListener(e -> {

        });
    } // END runTextRecognition method

    private void processTextBlock(Text result) {
        // [START mlkit_process_text_block]
        String resultText = result.getText();
        //Log.w("BLOCK CORNER POINTS", resultText);

        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            //Log.w("TEXT BLOCK", "FIRST TEXT BLOCK");
            for(Point p : blockCornerPoints){
                //Log.w("BLOCK CORNER POINTS", p.toString());
            }
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                //Log.w("LINE TEXT", lineText);
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for(Point p : lineCornerPoints){
                    //Log.w("LINE CORNER POINTS", p.toString());
                }
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                    for(Point p : elementCornerPoints){
                        //Log.w("ELEMENT TEXT", elementText);
                        //Log.w("ELEMENT CORNER POINTS", p.toString());
                    }
                    //Log.w("ELEMENT TEXT", elementText);
                    if(elementFrame.contains(x,y)){
                        Log.w("ELEMENT TEXT", elementText);
                    }
                }
            }
        }
    } // END processTextBlock method

    public ListenableFuture<ProcessCameraProvider> getCameraProviderFuture() {
        return cameraProviderFuture;
    }

    public void setCameraProviderFuture(ListenableFuture<ProcessCameraProvider> cameraProviderFuture) {
        this.cameraProviderFuture = cameraProviderFuture;
    }

    public PreviewView getPreviewView() {
        return previewView;
    }

    public void setPreviewView(PreviewView previewView) {
        this.previewView = previewView;
    }

    public Executor getAnalysisExecutor() {
        return analysisExecutor;
    }

    public void setAnalysisExecutor(Executor analysisExecutor) {
        this.analysisExecutor = analysisExecutor;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
