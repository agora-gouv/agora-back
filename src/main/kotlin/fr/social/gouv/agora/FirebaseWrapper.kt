package fr.social.gouv.agora

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

object FirebaseWrapper {

    fun initFirebase() {
        System.getenv("FIREBASE_CREDENTIALS_JSON")?.let { firebaseCredentialsJson ->
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseCredentialsJson.byteInputStream()))
                .build()

            FirebaseApp.initializeApp(options)
        }
    }

}