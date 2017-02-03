package quizbiblico.com.claudinei.quizbiblico;

import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;

public final class UsuarioDAO {

    private static DatabaseReference usuarioReferencia = FirebaseDB.getFirebaseReference();
    private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

    public void addUsuario(Usuario usuario){
        // TO DO
    }
    public ArrayList<Usuario> getUsuarioDB(){
        return null;
    }




}
