package quizbiblico.com.claudinei.quizbiblico;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class activityc_Ranking extends AppCompatActivity {

    public class UsuarioAdapter extends BaseAdapter {

        private final ArrayList<Usuario> usuarios;
        private final Activity activity;
        private final Usuario usuarioAtual;

        public UsuarioAdapter(ArrayList<Usuario> usuarios, Activity activity, Usuario usuarioAtual) {
            this.usuarios = usuarios;
            this.activity = activity;
            this.usuarioAtual = usuarioAtual;
        }

        @Override
        public int getCount() {
            return usuarios.size();
        }

        @Override
        public Object getItem(int position) {
            return usuarios.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = activity.getLayoutInflater().inflate(R.layout.lista_usuarios_personalizada, parent, false);

            Usuario usuario = usuarios.get(position);

            //Instanciando as Views
            TextView nome = (TextView) view.findViewById(R.id.lista_usuarios_personalizada_nome);
            TextView pontuacao = (TextView) view.findViewById(R.id.lista_usuarios_personalizada_pontuacao);
            TextView posicao = (TextView) view.findViewById(R.id.lista_usuarios_personalizada_posicao);
            TextView dataCadastro = (TextView) view.findViewById(R.id.lista_usuarios_personalizada_datacadastro);

            if(usuario.getUid().equals(usuarioAtual.getUid())){
                nome.setTypeface(null, Typeface.BOLD);
                pontuacao.setTypeface(null, Typeface.BOLD);
                posicao.setTypeface(null, Typeface.BOLD);
                dataCadastro.setTypeface(null, Typeface.BOLD);
            }

            //Definindo os valores para as Views
            nome.setText( usuario.getNome() == null ? usuario.getEmail() : usuario.getNome());
            pontuacao.setText(String.valueOf(usuario.getPontuacao()));
            posicao.setText(String.valueOf(position + 1) + "º");
            dataCadastro.setText("Jogador desde " + new SimpleDateFormat("dd/MM/yyyy").format(usuario.getPrimeiroAcesso()));

            return view;
        }
    }

    //RelativeLayout
    private RelativeLayout layoutPrincipal;

    private ListView listaUsuarios;

    //Tela de loading
    private RelativeLayout telaLoading;

    private ArrayList<Usuario> usuarios = new ArrayList<>();

    private boolean rankingCarregado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_ranking);

        instanciaElementosInterface();
        setaElementosInterface();
        congelaTela(true);

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

                UsuarioAdapter usuariosArrayAdapter = new UsuarioAdapter(
                        usuarios, activityc_Ranking.this, activityc_MenuPrincipal.usuario);

                listaUsuarios.setAdapter(usuariosArrayAdapter);

                congelaTela(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void instanciaElementosInterface() {
        listaUsuarios = (ListView) findViewById(R.id.ranking_lista);
        telaLoading = (RelativeLayout) findViewById(R.id.ranking_layout_carregando);
        layoutPrincipal = (RelativeLayout) findViewById(R.id.activity_ranking);
    }

    private void setaElementosInterface() {

        Glide.with(this)
                .load(R.drawable.other_loading)
                .asGif()
                .into((ImageView) findViewById(R.id.ranking_progresso));

    }

    private void congelaTela(boolean bloqueio) {

        if (bloqueio){
            telaLoading.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
            telaLoading.setVisibility(View.VISIBLE);

        }else{
            if (telaLoading.getVisibility() == View.VISIBLE) {
                telaLoading.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                telaLoading.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));

    }

    @Override
    protected void onStop() {

        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));

        super.onStop();
    }
}
