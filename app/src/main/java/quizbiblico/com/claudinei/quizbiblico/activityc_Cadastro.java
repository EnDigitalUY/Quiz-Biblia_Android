package quizbiblico.com.claudinei.quizbiblico;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class activityc_Cadastro extends AppCompatActivity {

    private Button btnResetar;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_cadastro);

        // Recebe os dados da Activity anterior
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            usuario = (Usuario) extra.getSerializable("usuario");
        }

        btnResetar = (Button) findViewById(R.id.btnResetar);
        btnResetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    FirebaseDB.getUsuarioReferencia().child(usuario.getUid()).child("respondidas").setValue(null);
                    Snackbar.make(findViewById(R.id.activity_main), "Usuário resetado com sucesso", Snackbar.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
