package ie.lit.ardictionary.utils;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import ie.lit.ardictionary.ui.camera.CameraViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextRecognitionHandler {

    public String recognisedWord;

    public TextRecognitionHandler(){}

    public void runTextRecognition(Bitmap screenshot, int x, int y, CameraViewModel cameraViewModel) {
        // TODO: get angle of camera programatically
        InputImage inputImage = InputImage.fromBitmap(screenshot,0);
        TextRecognizer recognizer = TextRecognition.getClient();

                Task<Text> result = recognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        // [START mlkit_process_text_block]
                        String resultText = text.getText();
                        // iterate over response, break down each text block to lines, and each line to individual words
                        for (Text.TextBlock block : text.getTextBlocks()) {
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
                                    // check each word if coordinates of touch event are inside its' bounds
                                    if(elementFrame.contains(x,y)){

                                        cameraViewModel.setWord(elementText);
                                        //Log.w("ELEMENT TEXT", cameraViewModel.getWord().getValue());
                                    }
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    } // END runTextRecognition method

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