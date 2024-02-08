package com.alexandreconrado.easylife.fragments.mainactivityfragments.sidemenu;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.LocalDataBase;
import com.alexandreconrado.easylife.database.daos.DraggableCardViewDao;
import com.alexandreconrado.easylife.database.daos.SpendingsAccountsDao;
import com.alexandreconrado.easylife.database.daos.UserInfosDao;
import com.alexandreconrado.easylife.databinding.FragmentBackupsBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogBackupLoadFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogNotifyFragment;
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
    private boolean changed = false;
    private LocalDataBase localDataBase;
    private DraggableCardViewDao draggableCardViewDao;
    private SpendingsAccountsDao spendingsAccountsDao;
    private UserInfosDao userInfosDao;

    private ExitBackupsFrag exitListenner;
    public interface ExitBackupsFrag{
        void onExitBackupsFrag(boolean changed);
    }

    private interface FirestoreDBCallback_getAllBackups{
        void onFirestoreDBCallback_getAllBackups();
    }
    private interface FirestoreDBCallback_UploadBackup{
        void onFirestoreDBCallback_UploadBackup();
    }
    private interface FirestoreDBCallback_getSelectedBackup{
        void onFirestoreDBCallback_getSelectedBackup(String docID);
    }
    private interface FirestoreDBCallback_deleteOldestBackup{
        void onFirestoreDBCallback_deleteOldestBackup();
    }
    private interface LocalDB_clear{
        void onLocalDB_clear();
    }

    public BackupsFragment() {
        // Required empty public constructor
    }
    public BackupsFragment(ExitBackupsFrag exitListenner) {
        this.exitListenner = exitListenner;
    }
    public BackupsFragment(List<Timestamp> listBackupsDates, ExitBackupsFrag exitListenner) {
        this.listBackupsDates = listBackupsDates;
        this.exitListenner = exitListenner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBackupsBinding.inflate(inflater);

        init();
        setupLocalDataBase();
        disableBackPressed();
        load();
        setupExitButton();
        setupCreateBackupButton();
        setupLoadBackupButton();

        return binding.getRoot();
    }
    private void disableBackPressed(){
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });
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
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
                long timestampToCompare = sharedPreferences.getLong("lastBackupUpload", 0);
                boolean passedFiveMinutes = isFiveMinutesPassed(timestampToCompare);
                if (passedFiveMinutes) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("lastBackupUpload", System.currentTimeMillis());
                    editor.apply();

                    CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                    customAlertDialogFragment.setConfirmListenner(THIS);
                    customAlertDialogFragment.setCancelListenner(THIS);
                    AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.backupsFrag_AlertDialog_Question_UploadBackup_Title), getString(R.string.backupsFrag_AlertDialog_Question_UploadBackup_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                    customAlertDialogFragment.setCustomFragment(fragment);
                    customAlertDialogFragment.setTag("FragBackups_uploadBackup");
                    customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                } else {
                    CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                    AlertDialogNotifyFragment fragment = new AlertDialogNotifyFragment(getString(R.string.backupsFrag_AlertDialog_Notify_AntiBackupSpam_Title), getString(R.string.backupsFrag_AlertDialog_Notify_AntiBackupSpam_Text), customAlertDialogFragment);
                    customAlertDialogFragment.setCustomFragment(fragment);
                    customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                }
            }
        });
    }
    public boolean isFiveMinutesPassed(long timestamp) {
        long currentTimeMillis = System.currentTimeMillis();
        long differenceMillis = currentTimeMillis - timestamp;
        long differenceMinutes = differenceMillis / (60 * 1000); // Convert milliseconds to minutes

        return differenceMinutes >= 5;
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        draggableCardViewDao = localDataBase.draggableCardViewDao();
        spendingsAccountsDao = localDataBase.spendingsAccountsDao();
        userInfosDao = localDataBase.userInfosDao();
    }
    private void setupLoadBackupButton(){
        binding.imageViewButtonLoadFragBackups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setConfirmListenner(THIS);
                customAlertDialogFragment.setCancelListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.backupsFrag_AlertDialog_Question_LoadBackup_Title), getString(R.string.backupsFrag_AlertDialog_Question_LoadBackup_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragBackups_loadBackup");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void setupExitButton(){
        binding.imageViewButtonExitFragBackups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitListenner.onExitBackupsFrag(changed);
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

                loadRv();
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                AlertDialogNotifyFragment fragment = new AlertDialogNotifyFragment(getString(R.string.backupsFrag_AlertDialog_Notify_Backup_Upload_Title), getString(R.string.backupsFrag_AlertDialog_Notify_Backup_Upload_Text), customAlertDialogFragment);
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
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
                        listBackupsDates.add(currentTime);
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

        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
        AlertDialogNotifyFragment fragment = new AlertDialogNotifyFragment(getString(R.string.backupsFrag_AlertDialog_Notify_Backup_Load_Title), getString(R.string.backupsFrag_AlertDialog_Notify_Backup_Load_Text), customAlertDialogFragment);
        customAlertDialogFragment.setCustomFragment(fragment);
        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
    }
    private void deleteOldestBackup(FirestoreDBCallback_deleteOldestBackup callback){
        Timestamp oldestTimestamp = listBackupsDates.get(0);
        for (Timestamp timestamp : listBackupsDates) {
            if (timestamp.compareTo(oldestTimestamp) < 0) {
                oldestTimestamp = timestamp;
            }
        }

        timestampFromClickedBackup = oldestTimestamp;

        FirestoreDBCallback_getSelectedBackup callback2 = new FirestoreDBCallback_getSelectedBackup() {
            @Override
            public void onFirestoreDBCallback_getSelectedBackup(String docID) {
                deleteBackup(docID, callback);
            }
        };
        getSelectedBackup(callback2);
    }
    private void deleteBackup(String docID, FirestoreDBCallback_deleteOldestBackup callback){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");
        db.collection("Users")
                .document(firebaseID)
                .collection("Backups")
                .document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listBackupsDates.remove(timestampFromClickedBackup);
                        callback.onFirestoreDBCallback_deleteOldestBackup();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failure delete backup firestore operation: "+e);
                    }
                });
    }
    @Override
    public void onBackupsItemClick(Timestamp backup) {
        timestampFromClickedBackup = backup;
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        switch (Tag){
            case "FragBackups_loadBackup":
                changed = true;
                if(timestampFromClickedBackup != null){
                    FirestoreDBCallback_getSelectedBackup callback = new FirestoreDBCallback_getSelectedBackup() {
                        @Override
                        public void onFirestoreDBCallback_getSelectedBackup(String docID) {
                            LocalDB_clear callback1 = new LocalDB_clear() {
                                @Override
                                public void onLocalDB_clear() {
                                    loadBackup(docID);
                                }
                            };

                            new LocalDatabaseClearTask(callback1).execute();
                        }
                    };
                    getSelectedBackup(callback);
                }
                break;
            case "FragBackups_uploadBackup":
                if(listBackupsDates.size() < 8){
                    uploadBackup();
                }else{
                    CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                    customAlertDialogFragment.setConfirmListenner(THIS);
                    customAlertDialogFragment.setCancelListenner(THIS);
                    AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.backupsFrag_AlertDialog_Question_DeleteAndUploadBackup_Title), getString(R.string.backupsFrag_AlertDialog_Question_DeleteAndUploadBackup_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                    customAlertDialogFragment.setCustomFragment(fragment);
                    customAlertDialogFragment.setTag("FragBackups_deleteAndUploadBackup");
                    customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                }
                break;
            case "FragBackups_deleteAndUploadBackup":
                FirestoreDBCallback_deleteOldestBackup callback = new FirestoreDBCallback_deleteOldestBackup() {
                    @Override
                    public void onFirestoreDBCallback_deleteOldestBackup() {
                        uploadBackup();
                    }
                };

                deleteOldestBackup(callback);
                break;
        }
    }
    @Override
    public void onCancelButtonClicked(String Tag) {

    }

    private class LocalDatabaseClearTask extends AsyncTask<Void, Void, String> {

        private LocalDB_clear callbabck1;

        public LocalDatabaseClearTask(){

        }
        public LocalDatabaseClearTask(LocalDB_clear callbabck1){
            this.callbabck1 =  callbabck1;
        }
        @Override
        protected String doInBackground(Void... voids) {
            draggableCardViewDao.clearAllEntries();
            spendingsAccountsDao.clearAllEntries();
            userInfosDao.clearAllEntries();
            return "";
        }

        @Override
        protected void onPostExecute(String object) {
            callbabck1.onLocalDB_clear();
        }
    }
}