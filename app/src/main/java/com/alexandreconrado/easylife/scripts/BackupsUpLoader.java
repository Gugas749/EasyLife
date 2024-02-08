package com.alexandreconrado.easylife.scripts;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.alexandreconrado.easylife.database.LocalDataBase;
import com.alexandreconrado.easylife.database.daos.DraggableCardViewDao;
import com.alexandreconrado.easylife.database.daos.SpendingsAccountsDao;
import com.alexandreconrado.easylife.database.daos.UserInfosDao;
import com.alexandreconrado.easylife.database.entities.DraggableCardViewEntity;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.database.entities.SubSpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class BackupsUpLoader {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "EasyLife_Logs_BackupsFrag";
    private LocalDataBase localDataBase;
    private DraggableCardViewDao draggableCardViewDao;
    private SpendingsAccountsDao spendingsAccountsDao;
    private UserInfosDao userInfosDao;
    private static final String ENCRYPTION_KEY = "V1BXGibGjiWNrlED2kwbyHgGQnaRdZNl";
    private boolean load1Complete = false, load2Complete = false, load3Complete = false;
    private Context context;
    private List<DraggableCardViewEntity> draggableCardViewEntityList = new ArrayList<>();
    private List<SpendingAccountsEntity> spendingAccountsEntitiesList = new ArrayList<>();
    private List<UserInfosEntity> userInfosEntityList = new ArrayList<>();

    private interface LocalDB_getAll{
        void onLocalDB_getAll();
    }
    private interface LocalDB_clear{
        void onLocalDB_clear();
    }
    private interface LocalDB_loadAll{
        void onLocalDB_loadAll();
    }
    
    public BackupsUpLoader(){
        
    }
    public BackupsUpLoader(Context context){
        this.context = context;
    }
    
    public void uploadBackup(String userIDFirebase, String backupDocID){
        setupLocalDataBase();

        LocalDB_getAll callbabck1 = new LocalDB_getAll() {
            @Override
            public void onLocalDB_getAll() {
                //DraggableCardViews
                for (int i = 0; i < draggableCardViewEntityList.size(); i++) {
                    DraggableCardViewEntity selectedObject = draggableCardViewEntityList.get(i);

                    Map<String, Object> object = new HashMap<>();
                    object.put("parentView", selectedObject.getParentView());
                    object.put("position", selectedObject.getPosition());
                    object.put("accountID", selectedObject.getAccountID());
                    object.put("subAccountID", selectedObject.getSubAccountID());
                    object.put("type", selectedObject.getType());
                    object.put("chartName", selectedObject.getChartName());
                    object.put("style", selectedObject.getStyle());

                    object.put("value1Percentage", selectedObject.getValue1Percentage());
                    object.put("value2Percentage", selectedObject.getValue2Percentage());
                    object.put("value3Percentage", selectedObject.getValue3Percentage());
                    object.put("value4Percentage", selectedObject.getValue4Percentage());
                    object.put("value5Percentage", selectedObject.getValue5Percentage());
                    object.put("value6Percentage", selectedObject.getValue6Percentage());
                    object.put("value7Percentage", selectedObject.getValue7Percentage());
                    object.put("value8Percentage", selectedObject.getValue8Percentage());

                    object.put("value1Text", selectedObject.getValue1Text());
                    object.put("value2Text", selectedObject.getValue2Text());
                    object.put("value3Text", selectedObject.getValue3Text());
                    object.put("value4Text", selectedObject.getValue4Text());
                    object.put("value5Text", selectedObject.getValue5Text());
                    object.put("value6Text", selectedObject.getValue6Text());
                    object.put("value7Text", selectedObject.getValue7Text());
                    object.put("value8Text", selectedObject.getValue8Text());

                    object.put("value1Color", selectedObject.getValue1Color());
                    object.put("value2Color", selectedObject.getValue2Color());
                    object.put("value3Color", selectedObject.getValue3Color());
                    object.put("value4Color", selectedObject.getValue4Color());
                    object.put("value5Color", selectedObject.getValue5Color());
                    object.put("value6Color", selectedObject.getValue6Color());
                    object.put("value7Color", selectedObject.getValue7Color());
                    object.put("value8Color", selectedObject.getValue8Color());

                    db.collection("Users")
                            .document(userIDFirebase)
                            .collection("Backups")
                            .document(backupDocID)
                            .collection("DraggableCardViews")
                            .add(object)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
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

                //SpendingAccounts
                for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                    SpendingAccountsEntity selectedObject = spendingAccountsEntitiesList.get(i);

                    Map<String, Object> object = new HashMap<>();
                    object.put("idUserFirebase", selectedObject.getIdUserFirebase());
                    object.put("emailUser", encrypt(selectedObject.getEmailUser()));
                    object.put("accountTitle", selectedObject.getAccountTitle());
                    List<Map<String, Object>> subAccountsList = new ArrayList<>();
                    if(selectedObject.getSubAccountsList() != null){
                        for (SubSpendingAccountsEntity subAccount : selectedObject.getSubAccountsList()) {
                            subAccountsList.add(convertSubSpendingAccountsEntityToMap(subAccount));
                        }
                    }
                    object.put("subAccountsList", subAccountsList);
                    List<Map<String, Object>> spendsList = new ArrayList<>();
                    if(selectedObject.getSpendsList() != null){
                        for (SpendsEntity spendsEntity : selectedObject.getSpendsList()) {
                            spendsList.add(convertSpendsEntityToMap(spendsEntity));
                        }
                    }
                    object.put("spendsList", spendsList);
                    object.put("percentagesNamesList", selectedObject.getPercentagesNamesList());
                    object.put("percentagesColorList", selectedObject.getPercentagesColorList());


                    db.collection("Users")
                            .document(userIDFirebase)
                            .collection("Backups")
                            .document(backupDocID)
                            .collection("SpendingAccounts")
                            .add(object)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
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

                //UserInfos
                for (int i = 0; i < userInfosEntityList.size(); i++) {
                    UserInfosEntity selectedObject = userInfosEntityList.get(i);

                    Map<String, Object> object = new HashMap<>();
                    object.put("firebaseID", selectedObject.firebaseID);
                    object.put("email", encrypt(selectedObject.email));
                    object.put("Password", encrypt(selectedObject.Password));
                    object.put("Theme", selectedObject.Theme);
                    object.put("Language", selectedObject.Language.toLanguageTag());;


                    db.collection("Users")
                            .document(userIDFirebase)
                            .collection("Backups")
                            .document(backupDocID)
                            .collection("UserInfos")
                            .add(object)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
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
            }
        };

        new LocalDatabaseGetTask(callbabck1).execute();
    }
    public void loadBackup(String userIDFirebase, String backupDocID){
        setupLocalDataBase();

        LocalDB_clear callback1 = new LocalDB_clear() {
            @Override
            public void onLocalDB_clear() {
                LocalDB_clear callback2 = new LocalDB_clear() {
                    @Override
                    public void onLocalDB_clear() {
                        if(load1Complete && load2Complete && load3Complete){
                            new LocalDatabaseInsertTask().execute();
                        }
                    }
                };

                loadDraggableCardViews(userIDFirebase, backupDocID, callback2);
                loadSpendingAccount(userIDFirebase, backupDocID, callback2);
                loadUserInfos(userIDFirebase, backupDocID, callback2);
            }
        };

        new LocalDatabaseClearTask(callback1).execute();
    }
    private void loadDraggableCardViews(String userIDFirebase, String backupDocID, LocalDB_clear callback2){
        db.collection("Users")
                .document(userIDFirebase)
                .collection("Backups")
                .document(backupDocID)
                .collection("DraggableCardViews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Long positionLong = document.getLong("position");
                                int aux = positionLong != null ? positionLong.intValue() : 0;
                                DraggableCardViewEntity newObject = new DraggableCardViewEntity(aux,
                                        document.getString("type"),
                                        document.getString("chartName"),
                                        document.getString("style"));
                                newObject.setInfos(document.getString("accountID"),
                                        document.getString("subAccountID"),
                                        document.getDouble("value1Percentage").floatValue(),
                                        document.getDouble("value2Percentage").floatValue(),
                                        document.getDouble("value3Percentage").floatValue(),
                                        document.getDouble("value4Percentage").floatValue(),
                                        document.getString("value1Text"),
                                        document.getString("value2Text"),
                                        document.getString("value3Text"),
                                        document.getString("value4Text"),
                                        document.getLong("value1Color").intValue(),
                                        document.getLong("value2Color").intValue(),
                                        document.getLong("value3Color").intValue(),
                                        document.getLong("value4Color").intValue());
                                newObject.setInfosType3(document.getDouble("value5Percentage").floatValue(),
                                        document.getDouble("value6Percentage").floatValue(),
                                        document.getDouble("value7Percentage").floatValue(),
                                        document.getDouble("value8Percentage").floatValue(),
                                        document.getString("value5Text"),
                                        document.getString("value6Text"),
                                        document.getString("value7Text"),
                                        document.getString("value8Text"),
                                        document.getLong("value5Color").intValue(),
                                        document.getLong("value6Color").intValue(),
                                        document.getLong("value7Color").intValue(),
                                        document.getLong("value8Color").intValue());

                                draggableCardViewEntityList.add(newObject);
                            }

                            load1Complete = true;
                            callback2.onLocalDB_clear();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void loadSpendingAccount(String userIDFirebase, String backupDocID, LocalDB_clear callback2){
        db.collection("Users")
                .document(userIDFirebase)
                .collection("Backups")
                .document(backupDocID)
                .collection("SpendingAccounts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SpendingAccountsEntity newObject = new SpendingAccountsEntity();

                                List<SubSpendingAccountsEntity> subAccountList = new ArrayList<>();
                                List<SpendsEntity> spendsEntityList = new ArrayList<>();

                                List<Map<String, Object>> subAccountsList = (List<Map<String, Object>>) document.get("subAccountsList");
                                if (subAccountsList != null) {
                                    for (Map<String, Object> subAccountMap : subAccountsList) {
                                        SubSpendingAccountsEntity subAccount = convertMapToSubSpendingAccountsEntity(subAccountMap);
                                        subAccountList.add(subAccount);
                                    }
                                }
                                List<Map<String, Object>> spendsList = (List<Map<String, Object>>) document.get("spendsList");
                                if (spendsList != null) {
                                    for (Map<String, Object> spendsMap : spendsList) {
                                        SpendsEntity spendsEntity = convertMapToSpendsEntity(spendsMap);
                                        spendsEntityList.add(spendsEntity);
                                    }
                                }

                                newObject.setInfos(document.getString("idUserFirebase"),
                                        decrypt(document.getString("emailUser")),
                                        document.getString("accountTitle"),
                                        spendsEntityList,
                                        (List<String>) document.get("percentagesNamesList"),
                                        (List<String>) document.get("percentagesColorList"));
                                newObject.setSubAccountsList(subAccountList);

                                spendingAccountsEntitiesList.add(newObject);
                            }

                            load2Complete = true;
                            callback2.onLocalDB_clear();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void loadUserInfos(String userIDFirebase, String backupDocID, LocalDB_clear callback2){
        db.collection("Users")
                .document(userIDFirebase)
                .collection("Backups")
                .document(backupDocID)
                .collection("UserInfos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String languageTag = document.getString("Language");
                                Locale locale = Locale.forLanguageTag(languageTag);

                                UserInfosEntity newObject = new UserInfosEntity(
                                        decrypt(document.getString("Password")),
                                        document.getString("Theme"),
                                        locale);

                                newObject.setInfos(document.getString("firebaseID"), decrypt(document.getString("email")));

                                userInfosEntityList.add(newObject);
                            }

                            load3Complete = true;
                            callback2.onLocalDB_clear();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(context, LocalDataBase.class, "EasyLifeLocalDB").build();
        draggableCardViewDao = localDataBase.draggableCardViewDao();
        spendingsAccountsDao = localDataBase.spendingsAccountsDao();
        userInfosDao = localDataBase.userInfosDao();
    }

    private class LocalDatabaseGetTask extends AsyncTask<Void, Void, String> {

        private LocalDB_getAll callbabck1;

        public LocalDatabaseGetTask(){

        }
        public LocalDatabaseGetTask(LocalDB_getAll callbabck1){
            this.callbabck1 =  callbabck1;
        }
        @Override
        protected String doInBackground(Void... voids) {
            draggableCardViewEntityList = draggableCardViewDao.getObjects();
            spendingAccountsEntitiesList = spendingsAccountsDao.getSpendingsAccounts();
            userInfosEntityList = userInfosDao.getUserInfos();
            return "";
        }

        @Override
        protected void onPostExecute(String object) {
            callbabck1.onLocalDB_getAll();
        }
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
    private class LocalDatabaseInsertTask extends AsyncTask<Void, Void, String> {

        private LocalDB_clear callbabck1;

        public LocalDatabaseInsertTask(){

        }
        public LocalDatabaseInsertTask(LocalDB_clear callbabck1){
            this.callbabck1 =  callbabck1;
        }
        @Override
        protected String doInBackground(Void... voids) {
            draggableCardViewDao.insertList(draggableCardViewEntityList);
            spendingsAccountsDao.insertList(spendingAccountsEntitiesList);
            userInfosDao.insertList(userInfosEntityList);
            return "";
        }

        @Override
        protected void onPostExecute(String object) {

        }
    }

    private Map<String, Object> convertSpendsEntityToMap(SpendsEntity spendsEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("amount", spendsEntity.getAmount());
        map.put("date", spendsEntity.getDate());
        map.put("mainAccountID", spendsEntity.getMainAccountID());
        map.put("subAccountID", spendsEntity.getSubAccountID());
        map.put("category", spendsEntity.getCategory());
        map.put("isPartOfSubAccount", spendsEntity.isPartOfSubAccount());
        map.put("isPartOf", spendsEntity.getIsPartOf());
        return map;
    }
    private SpendsEntity convertMapToSpendsEntity(Map<String, Object> map) {
        SpendsEntity spendsEntity = new SpendsEntity();
        spendsEntity.setAmount((double) map.get("amount"));
        Timestamp timestamp = (Timestamp) map.get("date");
        Date date = timestamp.toDate();
        spendsEntity.setDate(date);
        spendsEntity.setMainAccountID((String) map.get("mainAccountID"));
        spendsEntity.setSubAccountID((String) map.get("subAccountID"));
        spendsEntity.setCategory((String) map.get("category"));
        spendsEntity.setPartOfSubAccount((boolean) map.get("isPartOfSubAccount"));
        spendsEntity.setIsPartOf((String) map.get("isPartOf"));
        return spendsEntity;
    }
    private Map<String, Object> convertSubSpendingAccountsEntityToMap(SubSpendingAccountsEntity subSpendingAccountsEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("parentID", subSpendingAccountsEntity.getParentID());
        map.put("positionInTheList", subSpendingAccountsEntity.getPositionInTheList());
        map.put("accountTitle", subSpendingAccountsEntity.getAccountTitle());
        map.put("spendsList", subSpendingAccountsEntity.getSpendsList());
        map.put("percentagesNamesList", subSpendingAccountsEntity.getPercentagesNamesList());
        map.put("percentagesColorList", subSpendingAccountsEntity.getPercentagesColorList());
        map.put("colorInParent", subSpendingAccountsEntity.getColorInParent());
        return map;
    }
    private SubSpendingAccountsEntity convertMapToSubSpendingAccountsEntity(Map<String, Object> map) {
        SubSpendingAccountsEntity subSpendingAccountsEntity = new SubSpendingAccountsEntity();
        subSpendingAccountsEntity.setParentID((long) map.get("parentID"));
        subSpendingAccountsEntity.setPositionInTheList((long) map.get("positionInTheList"));
        subSpendingAccountsEntity.setAccountTitle((String) map.get("accountTitle"));
        // Similar conversions for other fields
        return subSpendingAccountsEntity;
    }

    private String encrypt(String value) {
        try {
            Key key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String decrypt(String encryptedValue) {
        try {
            Key key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
