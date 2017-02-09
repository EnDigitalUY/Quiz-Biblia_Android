package quizbiblico.com.claudinei.quizbiblico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import quizbiblico.com.claudinei.quizbiblico.R;
import quizbiblico.com.claudinei.quizbiblico.Usuario;

public class menuPrincipal extends AppCompatActivity {

    //Botões
    private Button btnDisconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Bundle extra = getIntent().getExtras();

        if (extra != null){
            Usuario userLogged = (Usuario) extra.getSerializable("userLogged");

            Toast.makeText(getApplicationContext(), "Usuário: " + userLogged.getEmail(), Toast.LENGTH_LONG).show();

        }

        btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });


    }
}
