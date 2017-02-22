package quizbiblico.com.claudinei.quizbiblico;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseDB {

    private static DatabaseReference databaseReference;
    private static DatabaseReference usuarioReferencia = getDatabaseReference().child("usuario");
    private static DatabaseReference questionReference = getDatabaseReference().child("question");
    private static DatabaseReference parametrosReferencia = getDatabaseReference().child("parameters");
    private static DatabaseReference conexaoReferencia = FirebaseDatabase.getInstance().getReference(".info/connected");

    public static DatabaseReference getDatabaseReference(){

        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        return databaseReference;
    }

    public static DatabaseReference getParametrosReferencia() {
        return parametrosReferencia;
    }

    public static DatabaseReference getUsuarioReferencia() {
        return usuarioReferencia;
    }

    public static DatabaseReference getQuestionReference() {return questionReference;}

    public static DatabaseReference getConexaoReferencia() {
        return conexaoReferencia;
    }
}