package quizbiblico.com.claudinei.quizbiblico;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public final class UsuarioDAO {

    // Referência para o banco de dados Firebase apontando para o nó de usuário
    private static DatabaseReference usuarioReferencia = FirebaseDB.getDatabaseReference().child("usuario");

    //Listener que irá realizar a captura dos usuários
    private static ValueEventListener usuarioListener;

    private static Usuario usuario;

    // Método para adicionar usuários a base de dados
    public static void addUsuario (Usuario usuario){
        usuarioReferencia.child(usuario.getUid()).setValue(usuario);
    }

    public static Usuario getUsuarioEspecifico(String uid){

        usuarioListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);

                usuarioReferencia.removeEventListener(usuarioListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        usuarioReferencia.orderByChild("uid").equalTo(uid).addValueEventListener(usuarioListener);

        return usuario;

    }
}
