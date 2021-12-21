package com.zenith.firebase;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Service
public class FirebaseInitialization {

	private static String SERVICEACCOUNTKEYFILE = "serviceAccountKey.json";

	@PostConstruct
	public void initialization() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("./" + SERVICEACCOUNTKEYFILE);

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

		FirebaseApp.initializeApp(options);
	}
}
