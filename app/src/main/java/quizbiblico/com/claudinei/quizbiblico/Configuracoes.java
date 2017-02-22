package quizbiblico.com.claudinei.quizbiblico;

import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Claudinei on 14/02/2017.
 */

public final class Configuracoes {

    private static boolean conexao;
    private static boolean bVerificado = false;

    public Configuracoes(){}

    public static void verificaConexao(){
        FirebaseDB.getConexaoReferencia().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conexao = dataSnapshot.getValue(Boolean.class);
                bVerificado = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static boolean getConexao(){
        if (bVerificado) {
            return conexao;
        }
        else{
            verificaConexao();
            return false;
        }
    }


}
