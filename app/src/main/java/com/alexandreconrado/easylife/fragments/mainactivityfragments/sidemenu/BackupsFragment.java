package com.alexandreconrado.easylife.fragments.mainactivityfragments.sidemenu;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentBackupsBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogBackupLoadFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.alexandreconrado.easylife.fragments.register.account.RVAdapterBackups;
import com.alexandreconrado.easylife.fragments.register.account.RegisterDialogAccountFragment;
import com.alexandreconrado.easylife.scripts.BackupsUpLoader;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackupsFragment extends Fragment implements
        RVAdapterBackups.BackupsItemClick,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag {

    private FragmentBackupsBinding binding;
    private List<Timestamp> listBackupsDates = new ArrayList<>();
    private Timestamp timestampFromClickedBackup;
    private RVAdapterBackups adapter;
    private BackupsFragment THIS;
    private String TAG = "EasyLife_Logs_BackupsFrag", backupDocID = "";;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private interface FirestoreDBCallback_getAllBackups{
        void onFirestoreDBCallback_getAllBackups();
    }
    private interface FirestoreDBCallback_UploadBackup{
        void onFirestoreDBCallback_UploadBackup();
    }
    private interface FirestoreDBCallback_getSelectedBackup{
        void onFirestoreDBCallback_getSelectedBackup(String docID);
    }

    public BackupsFragment() {
        // Required empty public constructor
    }
    public BackupsFragment(List<Timestamp> listBackupsDates) {
        this.listBackupsDates = listBackupsDates;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBackupsBinding.inflate(inflater);

        init();
        load();
        setupCreateBackupButton();
        setupLoadBackupButton();

        return binding.getRoot();
    }
    private void init(){
        THIS = this;
        adapter = new RVAdapterBackups(getContext(), listBackupsDates);
        adapter.setSpendingsItemListenner(this);
    }
    private void setupCreateBackupButton(){
        binding.imageViewButtonUploadFragBackups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBackup();
            }
        });
    }
    private void setupLoadBackupButton(){
        binding.imageViewButtonLoadFragBackups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setConfirmListenner(THIS);
                customAlertDialogFragment.setCancelListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.general_AlertDialog_Question_ExitWithoutSaving_Title), getString(R.string.general_AlertDialog_Question_ExitWithoutSaving_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragBackups_loadBackup");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void load(){
        FirestoreDBCallback_getAllBackups callback = new FirestoreDBCallback_getAllBackups() {
            @Override
            public void onFirestoreDBCallback_getAllBackups() {
                loadRv();
            }
        };

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");
        getAllBackups(firebaseID, callback);
    }
    private void loadRv(){
        if(listBackupsDates.size() > 0){
            listBackupsDates = sortTimestampsDescending(listBackupsDates);
            adapter.updateData(listBackupsDates);
            binding.rvBackupsFragBackups.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvBackupsFragBackups.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            binding.textViewNoItemsToDisplayFragBackups.setVisibility(View.VISIBLE);
            binding.rvBackupsFragBackups.setVisibility(View.GONE);
        }
    }
    public static List<Timestamp> sortTimestampsDescending(List<Timestamp> timestampList) {
        List<Timestamp> sortedList = new ArrayList<>(timestampList);
        Comparator<Timestamp> timestampComparator = (timestamp1, timestamp2) -> timestamp2.compareTo(timestamp1);
        Collections.sort(sortedList, timestampComparator);
        return sortedList;
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
    private void getSelectedBackup(FirestoreDBCallback_getSelectedBackup callback){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");
        db.collection("Users")
                .document(firebaseID)
                .collection("Backups")
                .whereEqualTo("TimeStamp", timestampFromClickedBackup)
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
    private void uploadBackup(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");

        FirestoreDBCallback_UploadBackup callback = new FirestoreDBCallback_UploadBackup() {
            @Override
            public void onFirestoreDBCallback_UploadBackup() {
                BackupsUpLoader backupsUpLoader = new BackupsUpLoader(THIS.getContext());
                backupsUpLoader.uploadBackup(firebaseID, backupDocID);
            }
        };

        Timestamp currentTime = Timestamp.now();
        Map<String, Object> object = new HashMap<>();
        object.put("TimeStamp", currentTime);

        db.collection("Users")
                .document(firebaseID)
                .collection("Backups")
                .add(object)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        backupDocID = documentReference.getId();
                        callback.onFirestoreDBCallback_UploadBackup();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    private void loadBackup(String docID){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");
        BackupsUpLoader backupsUpLoader = new BackupsUpLoader(THIS.getContext());
        backupsUpLoader.loadBackup(firebaseID, docID);
    }
    @Override
    public void onBackupsItemClick(Timestamp backup) {
        timestampFromClickedBackup = backup;
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        switch (Tag){
            case "FragBackups_loadBackup":
                if(timestampFromClickedBackup != null){
                    FirestoreDBCallback_getSelectedBackup callback = new FirestoreDBCallback_getSelectedBackup() {
                        @Override
                        public void onFirestoreDBCallback_getSelectedBackup(String docID) {
                            loadBackup(docID);
                        }
                    };
                    getSelectedBackup(callback);
                }
                break;
        }
    }

    @Override
    public void onCancelButtonClicked(String Tag) {

    }
}