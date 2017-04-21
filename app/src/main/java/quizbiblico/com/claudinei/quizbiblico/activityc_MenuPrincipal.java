package quizbiblico.com.claudinei.quizbiblico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activityc_MenuPrincipal extends AppCompatActivity {

    //Botões
    private Button btnJogar;

    // Usuário logado, quando entrar recebe os dados vindouros da tela de activityl_login, após logar recebe os dados da base de dados
    private Usuario usuario;

    //ImageView que contem a imagem do usuário
    //private ImageView imgUsuario;

    // TextView que conterá o nome do usuário
    private TextView txtNome;

    // TextViews dos filtros
    private TextView txtDificuldade;
    private TextView txtTestamento;
    private TextView txtSecao;

    // ScrollViews dos filtros
    private ScrollView scrollDificuldade;
    private ScrollView scrollTestamento;
    private ScrollView scrollSecao;

    // RadioButton dos filtros
    private ArrayList<RadioButton> radioButtonsDificuldade;
    private ArrayList<RadioButton> radioButtonsTestamento;
    private ArrayList<RadioButton> radioButtonsSecao;

    // Strings que contém os valores dos RadioButtons selecionados
    private ArrayList<String> parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_menu_principal);

        // Coloca como invisível a barra de progresso da tela de activityl_login
        activityc_Login.progressBar.setVisibility(View.INVISIBLE);

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
                            txtNome.setText(usuario.getNome());

                            // Se o usuário ter imagem, a exibe
                            /*if (usuario.getLinkImagemUsuario() != null) {
                                if (!(usuario.getLinkImagemUsuario().equals(""))) {
                                    //imgUsuario.setImageURI(Uri.parse(usuario.getLinkImagemUsuario()));
                                }
                            }*/

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

    private String getOpcaoSelecionada(ArrayList<RadioButton> radioButtons ){

        for (int i = 0; i < radioButtons.size(); i++){
            if (radioButtons.get(i).isChecked())
                return radioButtons.get(i).getText().toString();
        }

        return "";

    }

    private void instanciaElementosInterface() {
        //Botões
        btnJogar = (Button) findViewById(R.id.btnJogar);

        //ImageViews
        //imgUsuario = (ImageView) findViewById(R.id.imgUsuario);

        //TextViews
        txtNome = (TextView) findViewById(R.id.txtNome);
        txtDificuldade = (TextView) findViewById(R.id.filtros_txtDificuldade);
        txtTestamento = (TextView) findViewById(R.id.filtros_txtTestamentos);
        txtSecao = (TextView) findViewById(R.id.filtros_txtSecoes);

        //ScrollViews
        scrollDificuldade = (ScrollView) findViewById(R.id.scrollDificuldade);
        scrollTestamento = (ScrollView) findViewById(R.id.scrollTestamentos);
        scrollSecao = (ScrollView) findViewById(R.id.scrollSecoes);

        //RadioButtons
        radioButtonsDificuldade = new ArrayList<>();
        radioButtonsDificuldade.add((RadioButton) findViewById(R.id.radioDificuldade_todas));
        radioButtonsDificuldade.add((RadioButton) findViewById(R.id.radioDificuldade_facil));
        radioButtonsDificuldade.add((RadioButton) findViewById(R.id.radioDificuldade_media));
        radioButtonsDificuldade.add((RadioButton) findViewById(R.id.radioDificuldade_dificil));

        radioButtonsTestamento = new ArrayList<>();
        radioButtonsTestamento.add( (RadioButton) findViewById(R.id.radioTestamentos_todos) );
        radioButtonsTestamento.add( (RadioButton) findViewById(R.id.radioTestamentos_antigo) );
        radioButtonsTestamento.add( (RadioButton) findViewById(R.id.radioTestamentos_novo) );

        radioButtonsSecao = new ArrayList<>();
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_todos) );
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_pentateuco) );
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_historia) );
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_poesia) );
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_profetas1) );
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_profetas2) );
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_evangelhos) );
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_historia2) );
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_cartas) );
        radioButtonsSecao.add((RadioButton) findViewById(R.id.radioSecoes_profecia) );
    }

    private void setaElementosInterface() {
        // Ao clicar jogar em vai para a tela do jogo
        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Adiciona ao array de parâmetros as opções selecionadas
                parametros = new ArrayList<>();
                parametros.add(getOpcaoSelecionada(radioButtonsDificuldade));
                parametros.add(getOpcaoSelecionada(radioButtonsTestamento));
                parametros.add(getOpcaoSelecionada(radioButtonsSecao));

                //Indo para a próxima tela
                Intent intent = new Intent(activityc_MenuPrincipal.this, activityc_Jogo.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("parametros", parametros);
                startActivity(intent);
            }
        });

        // Instanciando e definindo a ação de clique no texto de dificuldade
        txtDificuldade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Caso a lista esteja fechada, a abre, caso esteja aberta, a fecha
                if (scrollDificuldade.getVisibility() == View.VISIBLE) {
                    scrollDificuldade.setVisibility(View.INVISIBLE);

                    // Recolhe o texto abaixo
                    RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    parametros.addRule(RelativeLayout.BELOW, R.id.filtros_txtDificuldade);
                    txtTestamento.setLayoutParams(parametros);
                } else {
                    scrollDificuldade.setVisibility(View.VISIBLE);

                    // Expande o texto abaixo
                    RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    parametros.addRule(RelativeLayout.BELOW, R.id.scrollDificuldade);
                    txtTestamento.setLayoutParams(parametros);
                }

            }
        });

        // Instanciando e definindo a ação de clique no texto de testamento
        txtTestamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Caso a lista esteja fechada, a abre, caso esteja aberta, a fecha
                if (scrollTestamento.getVisibility() == View.VISIBLE) {
                    scrollTestamento.setVisibility(View.INVISIBLE);

                    // Recolhe o texto abaixo
                    RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    parametros.addRule(RelativeLayout.BELOW, R.id.filtros_txtTestamentos);
                    txtSecao.setLayoutParams(parametros);
                } else {
                    scrollTestamento.setVisibility(View.VISIBLE);

                    // Expande o texto abaixo
                    RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    parametros.addRule(RelativeLayout.BELOW, R.id.scrollTestamentos);
                    txtSecao.setLayoutParams(parametros);

                }
            }
        });

        // Instanciando e definindo a ação de clique no texto de seções
        txtSecao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Caso a lista esteja fechada, a abre, caso esteja aberta, a fecha
                if (scrollSecao.getVisibility() == View.VISIBLE)
                    scrollSecao.setVisibility(View.INVISIBLE);
                else
                    scrollSecao.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_menuprincipal, menu);
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
            case R.id.menu_perfil: {
                // Atualiza o cadastro do usuário
                Intent intent = new Intent(activityc_MenuPrincipal.this, activityc_Cadastro.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
