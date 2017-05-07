package quizbiblico.com.claudinei.quizbiblico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class activityc_MenuPrincipal extends AppCompatActivity {

    //Botões
    private ImageView btnJogar;
    private ImageView btnPerfil;
    private ImageView btnHelp;

    // Usuário logado, quando entrar recebe os dados vindouros da tela de activityl_login, após logar recebe os dados da base de dados
    public static Usuario usuario;

    //Menu que contém algumas opções
    private Menu menu;

    //RelativeLayout
    private RelativeLayout layoutPrincipal;

    //Tela de loading
    private RelativeLayout telaLoading;

    private boolean elementosInstanciados = false;
    private boolean elementosSetados = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_menu_principal);

        // Coloca como invisível a barra de progresso da tela de activityl_login
        activityc_Login.telaLoading.setVisibility(View.INVISIBLE);

        //Verifica se existem dados vindouros da tela anterior
        Bundle extra = getIntent().getExtras();
        if (extra != null) {

            // Recebe o usuário da tela anterior
            usuario = (Usuario) extra.getSerializable("userLogged");

            Log.e(getClass().toString(), "Cadastro: " + String.valueOf(extra.getBoolean("cadastro")));

            // Caso seja um cadastro, apenas exibe
            if (extra.getBoolean("cadastro")) {
                Toast.makeText(getApplicationContext(), "Usuário sendo cadastrado", Toast.LENGTH_SHORT).show();
            } else {

                // Vai buscar no banco de dados as informações do usuário logado e atualiza o objeto
                FirebaseDB.getUsuarioReferencia().orderByKey().equalTo(usuario.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            usuario = data.getValue(Usuario.class);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

        }

        instanciaElementosInterface();
        setaElementosInterface();
    }

    private void congelaTela(boolean bloqueio) {
        for (int i = 0; i < menu.size(); i++){
            menu.getItem(i).setEnabled(! bloqueio);
        }

        if (bloqueio){
            if (layoutPrincipal.getVisibility() == View.VISIBLE) {
                layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                layoutPrincipal.setVisibility(View.INVISIBLE);
            }

            telaLoading.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
            telaLoading.setVisibility(View.VISIBLE);

        }else{
            if (telaLoading.getVisibility() == View.VISIBLE) {
                telaLoading.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                telaLoading.setVisibility(View.INVISIBLE);
            }

            layoutPrincipal.setVisibility(View.VISIBLE);

        }

    }

    private void atualizaTela(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Parameter.isBuscaConcluida()){
                    try{
                        Thread.sleep(100);
                    } catch (Exception e){
                        Log.d(getClass().toString(), e.getMessage().toString());
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        congelaTela(false);
                    }
                });

                /*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        congelaTela(false);
                    }
                });*/


            }
        }).start();

    }

    private void instanciaElementosInterface() {

        if (!elementosInstanciados) {

            //ImageViews
            btnJogar = (ImageView) findViewById(R.id.btnJogar);
            btnPerfil = (ImageView) findViewById(R.id.btnPerfil);
            btnHelp = (ImageView) findViewById(R.id.btnHelp);

            telaLoading = (RelativeLayout) findViewById(R.id.menuprincipal_layout_carregando);

            //LinearLayout
            layoutPrincipal = (RelativeLayout) findViewById(R.id.layout_menuprincipal);

            elementosInstanciados = true;

        }
    }

    private void setaElementosInterface() {

        if (! elementosSetados) {

            // Ao clicar jogar em vai para a tela do jogo
            btnJogar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));
                    Intent intent = new Intent(activityc_MenuPrincipal.this, activityc_Jogo.class);
                    startActivity(intent);
                }
            });

            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));
                    Intent intent = new Intent(activityc_MenuPrincipal.this, activityc_Perfil.class);
                    startActivity(intent);
                }
            });

            // Ao clicar em help abre a tela de ajuda do game
            btnHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));
                    Intent intent = new Intent(activityc_MenuPrincipal.this, activityc_Help.class);
                    startActivity(intent);
                }
            });

            Glide.with(this)
                    .load(R.drawable.other_loading)
                    .asGif()
                    .into((ImageView) findViewById(R.id.menuprincipal_progresso));

            elementosSetados = true;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        super.onCreateOptionsMenu(_menu);

        menu = _menu;

        instanciaElementosInterface();
        setaElementosInterface();
        congelaTela(true);
        atualizaTela();

        getMenuInflater().inflate(R.menu.menu_menuprincipal, _menu);
        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_desconectar: {
                // Desconecta o usuário
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }

                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
    }

    @Override
    protected void onStop() {

        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));

        super.onStop();
    }
}
