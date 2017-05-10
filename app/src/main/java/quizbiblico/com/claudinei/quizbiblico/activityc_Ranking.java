package quizbiblico.com.claudinei.quizbiblico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.ViewAnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activityc_Ranking extends AppCompatActivity {

    //RelativeLayout
    private ScrollView layoutPrincipal;

    private ListView listaUsuarios;

    //Tela de loading
    private RelativeLayout telaLoading;

    private ArrayList<Usuario> usuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_ranking);

        instanciaElementosInterface();
        setaElementosInterface();

        FirebaseDB.getUsuarioReferencia().orderByChild("pontuacao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Usuario> usuarios_aux = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.d(getClass().toString(), data.toString());
                    usuarios_aux.add(data.getValue(Usuario.class));
                }

                for (int i = usuarios_aux.size() - 1; i >= 0; i--){
                    usuarios.add(usuarios_aux.get(i));
                }

                Log.d(getClass().toString(), usuarios.toString());

                AdapterUsuariosPersonalizados usuariosArrayAdapter = new AdapterUsuariosPersonalizados(usuarios, activityc_Ranking.this);
                //ArrayAdapter<Usuario> usuarioArrayAdapter = new ArrayAdapter<Usuario>(getApplicationContext(), android.R.layout.simple_list_item_1, usuarios);

                listaUsuarios.setAdapter(usuariosArrayAdapter);

                Toast.makeText(getApplicationContext(), "Terminou", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void instanciaElementosInterface() {
        listaUsuarios = (ListView) findViewById(R.id.ranking_lista);
    }

    private void setaElementosInterface() {

    }
}
