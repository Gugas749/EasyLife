package com.alexandreconrado.easylife.scripts.mailsending;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.activitys.MainActivity;
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;
import com.alexandreconrado.easylife.scripts.UniqueRandomStringGenerator;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class SendMonthlyResumeEmail {
    private UserInfosEntity userInfosEntity;
    private String TAG = "SendMonthlyResumeEmail_Task";
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private Context context;
    private int colorSecondary = 0, textAppearanceBodyMedium = 0;
    private String colorSecondaryHex = "", textAppearanceBodyMediumHex = "";

    public SendMonthlyResumeEmail(){

    }
    public SendMonthlyResumeEmail(UserInfosEntity userInfosEntity, Context context){
        this.userInfosEntity = userInfosEntity;
        this.context = context;
    }

    private interface FirebaseStorageCallback_uploadImage{
        void onFirebaseStorageCallback_uploadImage();
    }
    private interface FirebaseStorageCallback_getUploadedImageUrl{
        void onFirebaseStorageCallback_getUploadedImageUrl(String url);
    }

    private String convertColorToHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    private void getColors(){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
        colorSecondary = context.getResources().getColor(R.color.secondaryLight);

        textAppearanceBodyMedium = Color.WHITE;

        colorSecondaryHex = convertColorToHex(colorSecondary);
        textAppearanceBodyMediumHex = convertColorToHex(textAppearanceBodyMedium);
    }
    public void prepareMonthlyResumeEmailInfo(Bitmap chartBitmap, String title, String textValue1, String mostSpendText, String lessSpendText, String totalAmount, String mostSpendAccountName, String lessSpendAccountName, List<String> percentagesList, List<String> accountsNames, List<String> colorsHexList){
        getColors();

        int colorToReplace = Color.WHITE;
        for (int x = 0; x < chartBitmap.getWidth(); x++) {
            for (int y = 0; y < chartBitmap.getHeight(); y++) {
                int pixelColor = chartBitmap.getPixel(x, y);
                if (pixelColor == colorToReplace) {
                    chartBitmap.setPixel(x, y, Color.TRANSPARENT);
                }
            }
        }
        for (int x = 0; x < chartBitmap.getWidth(); x++) {
            for (int y = 0; y < chartBitmap.getHeight(); y++) {
                int pixelColor = chartBitmap.getPixel(x, y);
                if (pixelColor == colorToReplace) {
                    chartBitmap.setPixel(x, y, Color.TRANSPARENT);
                }
            }
        }
        for (int x = 0; x < chartBitmap.getWidth(); x++) {
            for (int y = 0; y < chartBitmap.getHeight(); y++) {
                int pixelColor = chartBitmap.getPixel(x, y);
                if (pixelColor == colorToReplace) {
                    chartBitmap.setPixel(x, y, Color.TRANSPARENT);
                }
            }
        }

        UniqueRandomStringGenerator generator = new UniqueRandomStringGenerator();
        String imageID = generator.generateUniqueString();
        FirebaseStorageCallback_uploadImage callbackUploadImage = new FirebaseStorageCallback_uploadImage() {
            @Override
            public void onFirebaseStorageCallback_uploadImage() {
                FirebaseStorageCallback_getUploadedImageUrl callbackGetUploadedImageUrl = new FirebaseStorageCallback_getUploadedImageUrl() {
                    @Override
                    public void onFirebaseStorageCallback_getUploadedImageUrl(String url) {
                        sendMonthlyResumeEmail(title, url, textValue1, mostSpendText, lessSpendText, totalAmount, mostSpendAccountName, lessSpendAccountName, accountsNames, colorsHexList, percentagesList);
                    }
                };

                getTheUploadedPieChartImageUrl(imageID, callbackGetUploadedImageUrl);
            }
        };

        uploadPieChatAuxImageToStorage(chartBitmap, imageID, callbackUploadImage);
    }
    private void uploadPieChatAuxImageToStorage(Bitmap bitmap, String imageID, FirebaseStorageCallback_uploadImage callback){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference imagesRef = storageRef.child("images/" + imageID + ".png");

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callback.onFirebaseStorageCallback_uploadImage();
                Log.d(TAG, "Image uploaded successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
                Log.e(TAG, "Failed to upload image: " + e.getMessage());
            }
        });
    }
    private void getTheUploadedPieChartImageUrl(String imageID, FirebaseStorageCallback_getUploadedImageUrl callback){
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference imagesRef = storageRef.child("images/"+imageID+".png");
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadUrl = uri.toString();
                callback.onFirebaseStorageCallback_getUploadedImageUrl(downloadUrl);
                Log.d("TAG", "Download URL: " + downloadUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Failed to get download URL: " + e.getMessage());
            }
        });
    }
    private void sendMonthlyResumeEmail(String title, String imageUrl, String textValue1, String textValue2, String textValue3, String amountSpend, String mostSpendAccount, String lessSpendAccount, List<String> accountsNames, List<String> colorsHex, List<String> percentagesList){
        String toEmail = userInfosEntity.email;
        String fromEmail = "hmtdiverification@yahoo.com";
        String fromPassword = "pugbvahsdlwyiuxt";
        String emailSubject = "Easy Life"+title;
        String emailBody = "";
        switch (percentagesList.size()){
            case 1:
                emailBody = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>"+title+"</title><style>@import url('https://fonts.googleapis.com/css2?family=Dosis:wght@400&family=Arapey:wght@400&display=swap');body{font-family:'Arial',sans-serif;margin:0;padding:0;background-color:"+textAppearanceBodyMediumHex+";}.container{max-width:600px;margin:20px auto;background-color:"+colorSecondaryHex+";padding:20px;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);position:relative;color:#ffffff;text-align:center;}.logo{text-align:right;max-width:60px;height:auto;border-radius:50%;margin-right:10px;}.pie-chart-container{display:flex;flex-direction:column;justify-content:center;margin-bottom:20px;}.pie-chart-image{margin:auto;}.legend{display:flex;flex-direction:column;justify-content:center;margin-top:20px;margin-left:15px;text-align:center;}.legend-item{display:flex;align-items:center;margin-right:20px;}.legend-item span{margin-left:8px;}.footer{margin-top:20px;text-align:center;color:"+textAppearanceBodyMediumHex+";}</style></head><body><div class=\"container\"><img class=\"logo\" src=\"https://media.discordapp.net/attachments/1117167988659998791/1191714890432380968/snapLogoWhite.png?ex=65a671fa&is=6593fcfa&hm=29e4cfabf6f0eb62607799a2cacc4c0f98cc98025b4c524a66cd3a8750baeca1&=&format=webp&quality=lossless\" alt=\"Company Logo\"><h2>"+title+"</h2><div class=\"additional-labels\"><p>"+textValue1+": <strong>"+amountSpend+"</strong></p><p>"+textValue2+": <strong>"+mostSpendAccount+"</strong></p><p>"+textValue3+": <strong>"+lessSpendAccount+"</strong></p></div><div class=\"pie-chart-container\"><img id=\"spendsSummaryImage\" class=\"pie-chart-image\" src=\""+imageUrl+"\" alt=\"Spends Summary Image\"></div><div class=\"legend\" style=\"display:flex;flex-direction:row;justify-content:center;margin-top:20px;\"><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(0)+";border-radius:50%;\"></div><span>"+percentagesList.get(0)+"  -  "+accountsNames.get(0)+"</span></div></div><div class=\"footer\"><p>If you have any questions, please contact our support team.</p><p>If you did not sign up for this service, please ignore this email.</p></div></div><script>document.addEventListener(\"DOMContentLoaded\", function() {document.getElementById('spendsSummaryImage').src = \""+imageUrl+"\";});</script></body></html>\n";
                break;
            case 2:
                emailBody = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>"+title+"</title><style>@import url('https://fonts.googleapis.com/css2?family=Dosis:wght@400&family=Arapey:wght@400&display=swap');body{font-family:'Arial',sans-serif;margin:0;padding:0;background-color:"+textAppearanceBodyMediumHex+";}.container{max-width:600px;margin:20px auto;background-color:"+colorSecondaryHex+";padding:20px;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);position:relative;color:#ffffff;text-align:center;}.logo{text-align:right;max-width:60px;height:auto;border-radius:50%;margin-right:10px;}.pie-chart-container{display:flex;flex-direction:column;justify-content:center;margin-bottom:20px;}.pie-chart-image{margin:auto;}.legend{display:flex;flex-direction:column;justify-content:center;margin-top:20px;margin-left:15px;text-align:center;}.legend-item{display:flex;align-items:center;margin-right:20px;}.legend-item span{margin-left:8px;}.footer{margin-top:20px;text-align:center;color:"+textAppearanceBodyMediumHex+";}</style></head><body><div class=\"container\"><img class=\"logo\" src=\"https://media.discordapp.net/attachments/1117167988659998791/1191714890432380968/snapLogoWhite.png?ex=65a671fa&is=6593fcfa&hm=29e4cfabf6f0eb62607799a2cacc4c0f98cc98025b4c524a66cd3a8750baeca1&=&format=webp&quality=lossless\" alt=\"Company Logo\"><h2>"+title+"</h2><div class=\"additional-labels\"><p>"+textValue1+": <strong>"+amountSpend+"</strong></p><p>"+textValue2+": <strong>"+mostSpendAccount+"</strong></p><p>"+textValue3+": <strong>"+lessSpendAccount+"</strong></p></div><div class=\"pie-chart-container\"><img id=\"spendsSummaryImage\" class=\"pie-chart-image\" src=\""+imageUrl+"\" alt=\"Spends Summary Image\"></div><div class=\"legend\" style=\"display:flex;flex-direction:row;justify-content:center;margin-top:20px;\"><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(0)+";border-radius:50%;\"></div><span>"+percentagesList.get(0)+"  -  "+accountsNames.get(0)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(1)+";border-radius:50%;\"></div><span>"+percentagesList.get(1)+"  -  "+accountsNames.get(1)+"</span></div></div><div class=\"footer\"><p>If you have any questions, please contact our support team.</p><p>If you did not sign up for this service, please ignore this email.</p></div></div><script>document.addEventListener(\"DOMContentLoaded\", function() {document.getElementById('spendsSummaryImage').src = \""+imageUrl+"\";});</script></body></html>\n";
                break;
            case 3:
                emailBody = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>"+title+"</title><style>@import url('https://fonts.googleapis.com/css2?family=Dosis:wght@400&family=Arapey:wght@400&display=swap');body{font-family:'Arial',sans-serif;margin:0;padding:0;background-color:"+textAppearanceBodyMediumHex+";}.container{max-width:600px;margin:20px auto;background-color:"+colorSecondaryHex+";padding:20px;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);position:relative;color:#ffffff;text-align:center;}.logo{text-align:right;max-width:60px;height:auto;border-radius:50%;margin-right:10px;}.pie-chart-container{display:flex;flex-direction:column;justify-content:center;margin-bottom:20px;}.pie-chart-image{margin:auto;}.legend{display:flex;flex-direction:column;justify-content:center;margin-top:20px;margin-left:15px;text-align:center;}.legend-item{display:flex;align-items:center;margin-right:20px;}.legend-item span{margin-left:8px;}.footer{margin-top:20px;text-align:center;color:"+textAppearanceBodyMediumHex+";}</style></head><body><div class=\"container\"><img class=\"logo\" src=\"https://media.discordapp.net/attachments/1117167988659998791/1191714890432380968/snapLogoWhite.png?ex=65a671fa&is=6593fcfa&hm=29e4cfabf6f0eb62607799a2cacc4c0f98cc98025b4c524a66cd3a8750baeca1&=&format=webp&quality=lossless\" alt=\"Company Logo\"><h2>"+title+"</h2><div class=\"additional-labels\"><p>"+textValue1+": <strong>"+amountSpend+"</strong></p><p>"+textValue2+": <strong>"+mostSpendAccount+"</strong></p><p>"+textValue3+": <strong>"+lessSpendAccount+"</strong></p></div><div class=\"pie-chart-container\"><img id=\"spendsSummaryImage\" class=\"pie-chart-image\" src=\""+imageUrl+"\" alt=\"Spends Summary Image\"></div><div class=\"legend\" style=\"display:flex;flex-direction:row;justify-content:center;margin-top:20px;\"><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(0)+";border-radius:50%;\"></div><span>"+percentagesList.get(0)+"  -  "+accountsNames.get(0)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(1)+";border-radius:50%;\"></div><span>"+percentagesList.get(1)+"  -  "+accountsNames.get(1)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(2)+";border-radius:50%;\"></div><span>"+percentagesList.get(2)+"  -  "+accountsNames.get(2)+"</span></div></div><div class=\"footer\"><p>If you have any questions, please contact our support team.</p><p>If you did not sign up for this service, please ignore this email.</p></div></div><script>document.addEventListener(\"DOMContentLoaded\", function() {document.getElementById('spendsSummaryImage').src = \""+imageUrl+"\";});</script></body></html>\n";
                break;
            case 4:
                emailBody = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>"+title+"</title><style>@import url('https://fonts.googleapis.com/css2?family=Dosis:wght@400&family=Arapey:wght@400&display=swap');body{font-family:'Arial',sans-serif;margin:0;padding:0;background-color:"+textAppearanceBodyMediumHex+";}.container{max-width:600px;margin:20px auto;background-color:"+colorSecondaryHex+";padding:20px;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);position:relative;color:#ffffff;text-align:center;}.logo{text-align:right;max-width:60px;height:auto;border-radius:50%;margin-right:10px;}.pie-chart-container{display:flex;flex-direction:column;justify-content:center;margin-bottom:20px;}.pie-chart-image{margin:auto;}.legend{display:flex;flex-direction:column;justify-content:center;margin-top:20px;margin-left:15px;text-align:center;}.legend-item{display:flex;align-items:center;margin-right:20px;}.legend-item span{margin-left:8px;}.footer{margin-top:20px;text-align:center;color:"+textAppearanceBodyMediumHex+";}</style></head><body><div class=\"container\"><img class=\"logo\" src=\"https://media.discordapp.net/attachments/1117167988659998791/1191714890432380968/snapLogoWhite.png?ex=65a671fa&is=6593fcfa&hm=29e4cfabf6f0eb62607799a2cacc4c0f98cc98025b4c524a66cd3a8750baeca1&=&format=webp&quality=lossless\" alt=\"Company Logo\"><h2>"+title+"</h2><div class=\"additional-labels\"><p>"+textValue1+": <strong>"+amountSpend+"</strong></p><p>"+textValue2+": <strong>"+mostSpendAccount+"</strong></p><p>"+textValue3+": <strong>"+lessSpendAccount+"</strong></p></div><div class=\"pie-chart-container\"><img id=\"spendsSummaryImage\" class=\"pie-chart-image\" src=\""+imageUrl+"\" alt=\"Spends Summary Image\"></div><div class=\"legend\" style=\"display:flex;flex-direction:row;justify-content:center;margin-top:20px;\"><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(0)+";border-radius:50%;\"></div><span>"+percentagesList.get(0)+"  -  "+accountsNames.get(0)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(1)+";border-radius:50%;\"></div><span>"+percentagesList.get(1)+"  -  "+accountsNames.get(1)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(2)+";border-radius:50%;\"></div><span>"+percentagesList.get(2)+"  -  "+accountsNames.get(2)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(3)+";border-radius:50%;\"></div><span>"+percentagesList.get(3)+"  -  "+accountsNames.get(3)+"</span></div></div><div class=\"footer\"><p>If you have any questions, please contact our support team.</p><p>If you did not sign up for this service, please ignore this email.</p></div></div><script>document.addEventListener(\"DOMContentLoaded\", function() {document.getElementById('spendsSummaryImage').src = \""+imageUrl+"\";});</script></body></html>\n";
                break;
            case 5:
                emailBody = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>"+title+"</title><style>@import url('https://fonts.googleapis.com/css2?family=Dosis:wght@400&family=Arapey:wght@400&display=swap');body{font-family:'Arial',sans-serif;margin:0;padding:0;background-color:"+textAppearanceBodyMediumHex+";}.container{max-width:600px;margin:20px auto;background-color:"+colorSecondaryHex+";padding:20px;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);position:relative;color:#ffffff;text-align:center;}.logo{text-align:right;max-width:60px;height:auto;border-radius:50%;margin-right:10px;}.pie-chart-container{display:flex;flex-direction:column;justify-content:center;margin-bottom:20px;}.pie-chart-image{margin:auto;}.legend{display:flex;flex-direction:column;justify-content:center;margin-top:20px;margin-left:15px;text-align:center;}.legend-item{display:flex;align-items:center;margin-right:20px;}.legend-item span{margin-left:8px;}.footer{margin-top:20px;text-align:center;color:"+textAppearanceBodyMediumHex+";}</style></head><body><div class=\"container\"><img class=\"logo\" src=\"https://media.discordapp.net/attachments/1117167988659998791/1191714890432380968/snapLogoWhite.png?ex=65a671fa&is=6593fcfa&hm=29e4cfabf6f0eb62607799a2cacc4c0f98cc98025b4c524a66cd3a8750baeca1&=&format=webp&quality=lossless\" alt=\"Company Logo\"><h2>"+title+"</h2><div class=\"additional-labels\"><p>"+textValue1+": <strong>"+amountSpend+"</strong></p><p>"+textValue2+": <strong>"+mostSpendAccount+"</strong></p><p>"+textValue3+": <strong>"+lessSpendAccount+"</strong></p></div><div class=\"pie-chart-container\"><img id=\"spendsSummaryImage\" class=\"pie-chart-image\" src=\""+imageUrl+"\" alt=\"Spends Summary Image\"></div><div class=\"legend\" style=\"display:flex;flex-direction:row;justify-content:center;margin-top:20px;\"><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(0)+";border-radius:50%;\"></div><span>"+percentagesList.get(0)+"  -  "+accountsNames.get(0)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(1)+";border-radius:50%;\"></div><span>"+percentagesList.get(1)+"  -  "+accountsNames.get(1)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(2)+";border-radius:50%;\"></div><span>"+percentagesList.get(2)+"  -  "+accountsNames.get(2)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(3)+";border-radius:50%;\"></div><span>"+percentagesList.get(3)+"  -  "+accountsNames.get(3)+"</span></div><div class=\"legend-item\"><div style=\"width:16px;height:16px;background-color:"+colorsHex.get(4)+";border-radius:50%;\"></div><span>"+percentagesList.get(4)+"  -  "+accountsNames.get(4)+"</span></div></div><div class=\"footer\"><p>If you have any questions, please contact our support team.</p><p>If you did not sign up for this service, please ignore this email.</p></div></div><script>document.addEventListener(\"DOMContentLoaded\", function() {document.getElementById('spendsSummaryImage').src = \""+imageUrl+"\";});</script></body></html>\n";
                break;
        }

        SendMailTask sendMailTask = new SendMailTask(fromEmail, fromPassword, toEmail, emailSubject, emailBody);
        sendMailTask.execute();
    }
}
