package com.alexandreconrado.easylife.scripts.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class TextRecognitionUtil {

    public static String extractTextFromBitmap(Bitmap imageBitmap, Context context) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

        if (!textRecognizer.isOperational()) {
            return "Erro no reconhecimento de texto.";
        } else {
            Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
            SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);

            StringBuilder detectedText = new StringBuilder();
            for (int index = 0; index < textBlocks.size(); index++) {
                TextBlock textBlock = textBlocks.valueAt(index);
                detectedText.append(textBlock.getValue()).append("\n");
            }

            return detectedText.toString();
        }
    }
}

