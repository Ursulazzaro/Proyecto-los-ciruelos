package Grupo11.Seminario.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class FirebaseInitializer {

    @PostConstruct
    public void initialize() {
        try {
            // Leer la ruta del archivo JSON desde la variable de entorno
            String firebaseCredentialsPath = System.getenv("FIREBASE_CREDENTIALS_PATH");
            InputStream serviceAccount = new FileInputStream(firebaseCredentialsPath);

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            // Inicializa FirebaseApp si no ha sido inicializado
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase inicializado correctamente.");
            } else {
                System.out.println("Firebase ya estaba inicializado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
