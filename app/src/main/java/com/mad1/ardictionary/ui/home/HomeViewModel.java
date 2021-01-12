package com.mad1.ardictionary.ui.home;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> word;
    private int x, y;
    private MutableLiveData<Boolean> isPermissionsGranted;

    public MutableLiveData<Boolean> isPermissionsGranted() {
        return isPermissionsGranted;
    }

    public void setPermissionsGranted(boolean permissionsGranted) {
        isPermissionsGranted.setValue(permissionsGranted);
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

    public HomeViewModel() {
        word = new MutableLiveData<>();
        isPermissionsGranted = new MutableLiveData<>();
        word.setValue("");
        isPermissionsGranted.setValue(false);
    }

    public LiveData<String> getWord() {
        return word;
    }

    public void runTextRecognition(Bitmap screenshot){
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
            Log.w("TEXT BLOCK", "FIRST TEXT BLOCK");
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

                        word.setValue(elementText);
                        Log.w("ELEMENT TEXT", word.getValue());
                    }
                }
            }
        }
    } // END processTextBlock method
}