package quizbiblico.com.claudinei.quizbiblico;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseDB {

    private static DatabaseReference databaseReference;

    public static DatabaseReference getDatabaseReference(){

        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        return databaseReference;
    }

}