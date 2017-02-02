package quizbiblico.com.claudinei.quizbiblico;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseDB {

    private static DatabaseReference firebaseReferencia;
    private DatabaseReference perguntaReferencia;
    private DatabaseReference respostaReferencia;
    private DatabaseReference usuarioReferencia;

    private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

    public FirebaseDB(){
        firebaseReferencia = FirebaseDatabase.getInstance().getReference();
        perguntaReferencia = firebaseReferencia.child("pergunta");
        respostaReferencia = firebaseReferencia.child("resposta");
        usuarioReferencia = firebaseReferencia.child("usuario");
    }

    public void addUsuario(Usuario usuario){
        usuarioReferencia.child(usuario.getEmail()).setValue(usuario);
    }

    public ArrayList<Usuario> getUsuarioDB(){

        usuarioReferencia.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    usuarios.add(dataSnapshot.getValue(Usuario.class));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return usuarios;

    }

    public DatabaseReference getFirebaseReferencia() {
        return firebaseReferencia;
    }

    public DatabaseReference getPerguntaReferencia() {
        return perguntaReferencia;
    }

    public DatabaseReference getRespostaReferencia() {
        return respostaReferencia;
    }

    public DatabaseReference getUsuarioReferencia() {
        return usuarioReferencia;
    }
}