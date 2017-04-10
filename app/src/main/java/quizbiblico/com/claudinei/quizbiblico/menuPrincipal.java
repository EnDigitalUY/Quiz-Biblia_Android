package quizbiblico.com.claudinei.quizbiblico;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class menuPrincipal extends AppCompatActivity {

    //Botões
    private Button btnJogar;

    // Usuário logado, quando entrar recebe os dados vindouros da tela de login, após logar recebe os dados da base de dados
    private Usuario usuario;

    //ImageView que contem a imagem do usuário
    //private ImageView imgUsuario;

    // TextView que conterá o nome do usuário
    private TextView txtNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Coloca como invisível a barra de progresso da tela de login
        Login.progressBar.setVisibility(View.INVISIBLE);

        //Verifica se existem dados vindouros da tela anterior
        Bundle extra = getIntent().getExtras();
        if (extra != null){

            // Recebe o usuário da tela anterior
            usuario = (Usuario) extra.getSerializable("userLogged");

            Log.e(getClass().toString(), "Cadastro: " + String.valueOf(extra.getBoolean("cadastro")) );

            // Caso seja um cadastro, apenas exibe
            if (extra.getBoolean("cadastro")) {
                Toast.makeText(getApplicationContext(), "Usuário sendo cadastrado", Toast.LENGTH_SHORT).show();
            }else {

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

        // Instanciando os objetos visuais
        btnJogar = (Button) findViewById(R.id.btnJogar);
        txtNome = (TextView) findViewById(R.id.txtNome);
        //imgUsuario = (ImageView) findViewById(R.id.imgUsuario);

        // Vai para a tela do jogo
        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Indo para a próxima tela
                Intent intent = new Intent(menuPrincipal.this, Jogo.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);
        return(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_desconectar: {
                // Desconecta o usuário
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            }
            case R.id.menu_perfil:{
                // Atualiza o cadastro do usuário
                Intent intent = new Intent(menuPrincipal.this, cadastroActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
