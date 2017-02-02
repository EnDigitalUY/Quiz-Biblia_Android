package quizbiblico.com.claudinei.quizbiblico;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    //Firebase Realtime Database
    private FirebaseDB database;

    //Firebase Authentication
    private FirebaseAuth authentication;
    private FirebaseAuth.AuthStateListener authenticationListener;

    //Botões
    private Button btnLogin;
    private Button btnRegister;

    //EditText
    private EditText email;
    private EditText password;

    //Switch
    private Switch swKeepConnected;

    //User
    private Usuario userLogged = null;

    //View
    private View myView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Disponibilidade da base de dados offline
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.activity_main), "FirebaseDatabase error\n\n" + e.getMessage().toString(), Snackbar.LENGTH_SHORT).show();
        }

        myView = findViewById(R.id.activity_main);

        //Instanciando a autenticação
        authentication = FirebaseAuth.getInstance();

        //Adicionando Listener (ouvinte) para a autenticação. Caso haja alguma mudança, o listener notificará
        authenticationListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = authentication.getCurrentUser();

                    if (user != null){
                        // User is signed in
                        Log.d("Login.java", "onAuthStateChanged:signed_in:" + user.getUid());

                        userLogged = new Usuario(user.getEmail(), user.getDisplayName(), password.getText().toString(), user.getUid().toString(), false);

                        //Indo para a próxima tela
                        Intent intent = new Intent(Login.this, menuPrincipal.class);
                        intent.putExtra("userLogged", userLogged);
                        startActivity(intent);

                    }else{
                        // User is signed out
                        Log.d("Login.java", "onAuthStateChanged:signed_out");
                    }
            }
        };

        // Instanciando os EditTexts
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtSenha);

        //Instanciando o Switch
        swKeepConnected = (Switch) findViewById(R.id.sw_KeepConnected);

        ArrayList<String> user_pass = getUserPreference();
        if (user_pass != null){
            email.setText(user_pass.get(0));
            password.setText(user_pass.get(1));
        }

        //Instanciando os botões
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        //Definindo a ação tomada ao clicar em login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseLogin(email.getText().toString(), password.getText().toString());

                if (swKeepConnected.isChecked()) {
                    //Grava o email e a senha do usuário para automaticamente fazer o login futuramente
                    setUserPreferences(email.getText().toString(), password.getText().toString(), true);
                }else {
                    setUserPreferences("", "", false);
                }

            }
        });

        //Definindo a ação tomada ao clicar em Cadastro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseRegister(email.getText().toString(), password.getText().toString());
            }
        });
    }

    // Função responsável por retornar as preferências de usuário (email, senha e manterconectado) num arquivo
    //  user_pass[0] = email
    //  user_pass[1] = senha
    //      * Caso o keepInfo esteja como falso, os campos de email e senha estarão em branco
    private ArrayList<String> getUserPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.package_name), 0);
        if (sharedPreferences.contains("keepInfo")){
            if (sharedPreferences.getBoolean("keepInfo", false)){
                ArrayList<String> user_pass = new ArrayList<String>();
                user_pass.add(sharedPreferences.getString("email", ""));
                user_pass.add(sharedPreferences.getString("password", ""));
                return user_pass;
            }
        }
        return null;
    }

    // Função responsável por salvar as preferências de usuário (email, senha e manterconectado) num arquivo
    //  user_pass[0] = email
    //  user_pass[1] = senha
    //      * Caso o keepInfo esteja como falso, os campos de email e senha estarão em branco
    private void setUserPreferences(String email, String password, boolean keepInfo){
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.package_name), 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("keepInfo", keepInfo);
        editor.commit();
    }

    // No início da aplicação, seta o Listener para acompanhar as mudanças na autenticação
    @Override
    public void onStart() {
        super.onStart();
        authentication.addAuthStateListener(authenticationListener);
    }

    // No término da aplicação, remove o Listener que acompanha as mudanças na autenticação
    @Override
    public void onStop() {
        super.onStop();

        if (userLogged != null){
            if (!userLogged.getKeepConnected())
                FirebaseAuth.getInstance().signOut();
        }

        if (authenticationListener != null) {
            authentication.removeAuthStateListener(authenticationListener);
        }
    }

    // Função responsável por criar o usuário e após isso, realizar o login do mesmo
    private void firebaseRegister(String email, String pass){
        authentication.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Login.java", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Snackbar.make(findViewById(R.id.activity_main), "Não foi possível criar o usuário", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Função responsável por fazer o login do usuário
    private void firebaseLogin(String email, String pass){
        authentication.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Login.java", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Login.java", "signInWithEmail:failed", task.getException());
                            Snackbar.make(findViewById(R.id.activity_main), "Não foi possível conectar o usuário", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}