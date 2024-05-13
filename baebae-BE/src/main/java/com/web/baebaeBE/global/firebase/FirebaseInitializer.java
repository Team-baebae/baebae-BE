package com.web.baebaeBE.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;

public class FirebaseInitializer {
    @Value("${fcm.certification}")
    private String certification;

    @Value("${fcm.databaseurl}")
    private String databaseUrl;

    public void initializeFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream(certification);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
