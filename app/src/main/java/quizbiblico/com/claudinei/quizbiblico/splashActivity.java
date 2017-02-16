package quizbiblico.com.claudinei.quizbiblico;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splashActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Disponibilidade da base de dados offline
        /*try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "FirebaseDatabase error\n\n" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }*/

        Configuracoes.verificaConexao();


        Handler handler = new Handler();
        handler.postDelayed(this, 3000);
    }
    @Override
    public void run() {
        startActivity(new Intent(this, Login.class));
        finish();
    }
}