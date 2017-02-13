package quizbiblico.com.claudinei.quizbiblico;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class splashActivity extends AppCompatActivity implements Runnable {


    public boolean debug = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_splash);

        //Disponibilidade da base de dados offline
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "FirebaseDatabase error\n\n" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        Handler handler = new Handler();
        handler.postDelayed(this, 3000);
    }
    @Override
    public void run() {
        //startActivity(new Intent(this, Login.class));
        startActivity(new Intent(this, Jogo.class));
        finish();
    }
}
