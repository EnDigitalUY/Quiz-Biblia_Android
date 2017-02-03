package quizbiblico.com.claudinei.quizbiblico;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Random;

public final class FirebaseDB {

    private static DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();



    public static DatabaseReference getFirebaseReference() {
        return firebaseReference;
    }
}