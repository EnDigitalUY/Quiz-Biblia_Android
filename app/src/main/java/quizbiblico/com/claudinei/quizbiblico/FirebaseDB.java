package quizbiblico.com.claudinei.quizbiblico;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseDB {

    private static DatabaseReference databaseReference;
    private static DatabaseReference usuarioReferencia = getDatabaseReference().child("usuario");

    public static DatabaseReference getDatabaseReference(){

        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        return databaseReference;
    }

    public static DatabaseReference getUsuarioReferencia() {
        return usuarioReferencia;
    }
}