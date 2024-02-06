package com.alexandreconrado.easylife.fragments.register.account;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentRegisterDialogAccountBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogBackupLoadFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogNotifyFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.sidemenu.BackupsFragment;
import com.alexandreconrado.easylife.fragments.register.RegisterFragment;
import com.alexandreconrado.easylife.scripts.BackupsUpLoader;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
import com.alexandreconrado.easylife.scripts.mailsending.SendMailTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class RegisterDialogAccountFragment extends Fragment implements
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogBackupLoad_CustomAlertDialog,
        CustomAlertDialogFragment.DismissListenner {

    private FragmentRegisterDialogAccountBinding binding;
    private RegisterFragment parent;
    private List<Timestamp> listBackupsDates = new ArrayList<>();
    private RegisterDialogAccountFragment THIS;
    private String codeEmail, TAG = "EasyLife_Logs_RegisterDialogAccountFrag", finalUserEmail = "", emailInsertedInEditText = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean auxIsEmailRegistered = false, isRegistering = false, isDialogOpen = false;


    private interface FirestoreDBCallback_isRegistered{
        void onFirestoreDBCallback_isRegistered();
    }
    private interface FirestoreDBCallback_getAllBackups{
        void onFirestoreDBCallback_getAllBackups();
    }

    public RegisterDialogAccountFragment() {

    }
    public RegisterDialogAccountFragment(RegisterFragment parent) {
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterDialogAccountBinding.inflate(inflater);

        THIS = this;
        setupTextViewResendEmail();
        setupContinueButton();

        return binding.getRoot();
    }
    private void setupTextViewResendEmail(){
        binding.textViewResendEmailRegisterAccountDialogFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewResendEmailRegisterAccountDialogFrag.setEnabled(false);

                sendEmail(emailInsertedInEditText);

                startDelayTimer();
            }
        });
    }
    private void startDelayTimer() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.textViewResendEmailRegisterAccountDialogFrag.setEnabled(true);
            }
        }, 5 * 60 * 1000);
    }
    private void setupContinueButton(){
        binding.butContinueRegisterAccountDialogFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.butContinueRegisterAccountDialogFrag.setEnabled(false);
                if (binding.editTextEmailCodeRegisterAccountDialogFrag.getVisibility() == View.GONE) {
                    emailInsertedInEditText = binding.editTextEmailRegisterAccountDialogFrag.getText().toString().trim();
                    if (isValidEmail(emailInsertedInEditText)) {
                        startDelayTimer();
                        binding.textViewResendEmailRegisterAccountDialogFrag.setEnabled(false);
                        sendEmail(emailInsertedInEditText);
                    } else {
                        Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(codeEmail.equals(binding.editTextEmailCodeRegisterAccountDialogFrag.getText().toString().trim())){
                        finalUserEmail = binding.editTextEmailRegisterAccountDialogFrag.getText().toString().trim();

                        Map<String, Object> object = new HashMap<>();
                        object.put("UserEmail", finalUserEmail);
                        binding.butContinueRegisterAccountDialogFrag.setEnabled(false);

                        if(isNetworkAvailable()){
                           if(!isDialogOpen){
                               FirestoreDBCallback_isRegistered dbCallbakc = new FirestoreDBCallback_isRegistered() {
                                   @Override
                                   public void onFirestoreDBCallback_isRegistered() {
                                       if(auxIsEmailRegistered){
                                           if(!isDialogOpen){
                                               isDialogOpen = true;
                                               CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                                               customAlertDialogFragment.setDismissListenner(THIS);
                                               customAlertDialogFragment.setConfirmListenner(THIS);
                                               customAlertDialogFragment.setCancelListenner(THIS);
                                               AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.register_dialog_AlertDialog_Question_Title), getString(R.string.register_dialog_AlertDialog_Question_Text), customAlertDialogFragment, customAlertDialogFragment, "4");
                                               customAlertDialogFragment.setCustomFragment(fragment);
                                               customAlertDialogFragment.setTag("FragRegisterDialogAccount_BackupLoad");
                                               customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                                           }
                                       }else{
                                           FirestoreDBCallback_isRegistered callbackIsRegistered = new FirestoreDBCallback_isRegistered() {
                                               @Override
                                               public void onFirestoreDBCallback_isRegistered() {
                                                   parent.changeDialogFragments(finalUserEmail);
                                               }
                                           };
                                           registerAccountFirebase(object, callbackIsRegistered);
                                       }
                                   }
                               };

                               isEmailRegistered(finalUserEmail, dbCallbakc);
                           }
                        }else{
                            Toast.makeText(getContext(), getString(R.string.register_dialog_account_Toast_NotAvaliable_Network), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        binding.editTextEmailCodeRegisterAccountDialogFrag.setError(getString(R.string.register_dialog_account_email_code_wrong));
                    }
                }
                binding.butContinueRegisterAccountDialogFrag.setEnabled(true);
            }
        });
    }
    private boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void sendEmail(String email) {
        String fromEmail = "hmtdiverification@yahoo.com";
        String fromPassword = "pugbvahsdlwyiuxt";
        String toEmail = email;
        String emailSubject = "Easy Life Account Creation";
        int min = 100000; // The minimum value of the range (inclusive)
        int max = 999999; // The maximum value of the range (inclusive)

        Random random = new Random();
        int randomNumber = random.nextInt((max - min) + 1) + min;
        String emailBody = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Email Verification</title><style>@import url('https://fonts.googleapis.com/css2?family=Dosis:wght@400&family=Arapey:wght@400&display=swap');body{font-family:'Arial',sans-serif;margin:0;padding:0;background-color:#f4f4f4;}.container{max-width:600px;margin:20px auto;background-color:#6A6ECF;padding:20px;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);position:relative;color:#ffffff;}.logo{max-width:60px;height:auto;border-radius:50%;margin-right:10px;}.content{text-align:left;}.verification-code{font-size:24px;font-weight:bold;color:#F493FB;text-align:center;}.footer{margin-top:20px;text-align:center;color:#8D93E3;}</style></head><body><div class=\"container\"><div><img class=\"logo\" src=\"https://media.discordapp.net/attachments/1117167988659998791/1191714890432380968/snapLogoWhite.png?ex=65a671fa&is=6593fcfa&hm=29e4cfabf6f0eb62607799a2cacc4c0f98cc98025b4c524a66cd3a8750baeca1&=&format=webp&quality=lossless\" alt=\"Company Logo\"><h2>Email Verification</h2></div><div class=\"content\"><p>Hello!! User,</p><p>Thanks for creating an account!! Please use the following verification code to enter the Application:</p><p class=\"verification-code\">" + randomNumber + "</p></div><div class=\"footer\"><p>If you did not sign up for this service, please ignore this email.</p></div></div></body></html>\n";

        SendMailTask sendMailTask = new SendMailTask(fromEmail, fromPassword, toEmail, emailSubject, emailBody);
        sendMailTask.execute();

        binding.editTextEmailCodeRegisterAccountDialogFrag.setVisibility(View.VISIBLE);
        binding.textViewResendEmailRegisterAccountDialogFrag.setVisibility(View.VISIBLE);
        codeEmail = Integer.toString(randomNumber);
        PopUpNotifyEmailSent();
    }
    private void PopUpNotifyEmailSent(){
        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
        AlertDialogNotifyFragment fragment = new AlertDialogNotifyFragment(getString(R.string.alertDialog_Notify_EmailSend_Title), getString(R.string.alertDialog_Notify_EmailSend_Description), customAlertDialogFragment);
        customAlertDialogFragment.setCustomFragment(fragment);
        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
    }
    private void isEmailRegistered(String email, FirestoreDBCallback_isRegistered callbakc){
        auxIsEmailRegistered = false;
        isRegistering = true;
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String emailFromFirestore = document.getString("UserEmail");
                                if(emailFromFirestore.equals(email)){
                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("firebaseID", document.getId());
                                    editor.apply();
                                    auxIsEmailRegistered = true;
                                    callbakc.onFirestoreDBCallback_isRegistered();
                                    break;
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void registerAccountFirebase(Map<String, Object> object, FirestoreDBCallback_isRegistered callbackIsRegistered){
        db.collection("Users")
                .add(object)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Locale deviceLanguage = Locale.getDefault();
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("firebaseID", documentReference.getId());
                        editor.putString("theme_preference", "System-Sync");
                        editor.putString("user_language", deviceLanguage.getDisplayName().toLowerCase());
                        editor.putString("autoBackupTime", "monthly");
                        editor.apply();

                        callbackIsRegistered.onFirestoreDBCallback_isRegistered();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding documents.");
                    }
                });
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void getAllBackups(String userID, FirestoreDBCallback_getAllBackups callback){
        db.collection("Users")
                .document(userID)
                .collection("Backups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timestamp backupDate = document.getTimestamp("TimeStamp");
                                listBackupsDates.add(backupDate);
                            }
                            callback.onFirestoreDBCallback_getAllBackups();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        switch (Tag){
            case "FragRegisterDialogAccount_BackupLoad":
                FirestoreDBCallback_getAllBackups callback = new FirestoreDBCallback_getAllBackups() {
                    @Override
                    public void onFirestoreDBCallback_getAllBackups() {
                        isRegistering = false;
                        TypedValue typedValue = new TypedValue();
                        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
                        int color = typedValue.data;

                        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                        customAlertDialogFragment.setConfirmButtonClickAlertDialogBackupLoad_CustomAlertDialog(THIS);
                        customAlertDialogFragment.setBackgroundColor(color);
                        AlertDialogBackupLoadFragment fragment = new AlertDialogBackupLoadFragment(listBackupsDates, customAlertDialogFragment, customAlertDialogFragment);
                        customAlertDialogFragment.setCustomFragment(fragment);
                        customAlertDialogFragment.setTag("FragRegisterDialogAccount_BackupLoad");
                        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                    }
                };

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
                String firebaseID = sharedPreferences.getString("firebaseID", "");
                getAllBackups(firebaseID, callback);
                break;
        }
    }
    @Override
    public void onCancelButtonClicked(String Tag) {
        switch (Tag){
            case "FragRegisterDialogAccount_BackupLoad":
                isRegistering = false;
                parent.changeDialogFragments(finalUserEmail);
                break;
        }
    }

    @Override
    public void onDismissListenner() {
        isDialogOpen = false;
        binding.butContinueRegisterAccountDialogFrag.setEnabled(true);
    }

    @Override
    public void onConfirmButtonClickAlertDialogBackupLoad_CustomAlertDialog(Timestamp backup) {
        binding.butContinueRegisterAccountDialogFrag.setEnabled(false);
        processBackup(backup);
    }
    private interface FirestoreDBCallback_getSelectedBackup{
        void onFirestoreDBCallback_getSelectedBackup(String docID);
    }
    private void processBackup(Timestamp backup){
        FirestoreDBCallback_getSelectedBackup callback = new FirestoreDBCallback_getSelectedBackup() {
            @Override
            public void onFirestoreDBCallback_getSelectedBackup(String docID) {
                loadBackup(docID);
            }
        };
        getSelectedBackup(callback, backup);
    }
    private void loadBackup(String docID){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");
        BackupsUpLoader backupsUpLoader = new BackupsUpLoader(THIS.getContext());
        backupsUpLoader.loadBackup(firebaseID, docID);
        binding.butContinueRegisterAccountDialogFrag.setEnabled(true);
        parent.changeDialogFragments(finalUserEmail);
    }
    private void getSelectedBackup(FirestoreDBCallback_getSelectedBackup callback, Timestamp backup){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");
        db.collection("Users")
                .document(firebaseID)
                .collection("Backups")
                .whereEqualTo("TimeStamp", backup)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            callback.onFirestoreDBCallback_getSelectedBackup(document.getId());
                            break;
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
                        }
                    }
                });
    }
}