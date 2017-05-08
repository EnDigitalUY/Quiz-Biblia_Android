package quizbiblico.com.claudinei.quizbiblico;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class activityc_Splash extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_splash);

        /*//Disponibilidade da base de dados offline
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "FirebaseDatabase error\n\n" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }*/

        Parameter.getNexQuestionNum_Aux();
        Configuracoes.verificaConexao();

        Handler handler = new Handler();
        handler.postDelayed(this, 3000);
    }

    @Override
    public void run() {

        startActivity(new Intent(this, activityc_Login.class));
        finish();
    }

}