package fr.social.gouv.agora

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

object FirebaseWrapper {

    fun initFirebase() {
        val credentials = GoogleCredentials.fromStream(System.getenv("FIREBASE_CREDENTIALS_JSON").byteInputStream())
        val options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build()

        FirebaseApp.initializeApp(options)
    }

}