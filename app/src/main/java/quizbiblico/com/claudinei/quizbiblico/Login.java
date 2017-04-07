package quizbiblico.com.claudinei.quizbiblico;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    //Firebase Realtime Database
    private FirebaseDB database;

    //Firebase Authentication
    private FirebaseAuth authentication;
    private FirebaseAuth.AuthStateListener authenticationListener;

    //Botões
    private Button btnLogin;
    private Button btnRegister;
    private LoginButton facebookLoginButton;

    //EditText
    private EditText email;
    private EditText password;

    //Switch
    private Switch swKeepConnected;

    //Usuário
    private Usuario userLogged = null;

    //Variável que identifica se é uma criação ou não de um usuário
    private boolean usuarioCadastrado;

    //Variável que identifica se é um login pelo Facebook
    private boolean loginFacebook = false;

    //Barra de progresso
    public static ProgressBar progressBar;

    //CallbackManager responsável por auxiliar na conexão pelo Facebook
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instanciando o SDK do Facebook para utilizá-lo
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.login);

        //Criação do callbackManager para realizar o login pelo Facebook
        callbackManager = CallbackManager.Factory.create();

        //Instanciando o botão de login do Facebook
        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(getClass().toString(), "Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(getClass().toString(), "Error");
            }
        });

        //Inicia identificando que não é um cadastro
        usuarioCadastrado = false;

        //Instanciando a autenticação
        authentication = FirebaseAuth.getInstance();

        //Adicionando Listener (ouvinte) para a autenticação. Caso haja alguma mudança, o listener notificará
        authenticationListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Usuário vindouro da autenticação
                FirebaseUser user = authentication.getCurrentUser();

                // Verifica se o usuário vindouro da autenticação é diferente de nulo, ou seja, está autenticado
                if (user != null){
                    userLogged = new Usuario(user.getEmail(), user.getDisplayName(), user.getUid().toString(), swKeepConnected.isChecked());

                    //Verifica se o usuário que está fazendo login pelo Facebook já existe
                    if (loginFacebook){
                        Log.e("Loginlék", "É login pelo Face");

                        // Vai buscar no banco de dados se o usuário já existe
                        FirebaseDB.getUsuarioReferencia().orderByKey().equalTo(userLogged.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                /*for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    usuario = data.getValue(Usuario.class);
                                }*/
                                try{
                                    Log.e("Loginlék", dataSnapshot.toString());
                                }catch (Exception e){
                                    Log.e("Loginlék", e.getMessage());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                    // Verifica se é a autenticação é vindoura de um cadastro de usuário
                    if (usuarioCadastrado)
                        FirebaseDB.getUsuarioReferencia().child(userLogged.getUid()).setValue(userLogged);

                    //Indo para a próxima tela
                    Intent intent = new Intent(Login.this, menuPrincipal.class);
                    intent.putExtra("userLogged", userLogged);
                    intent.putExtra("cadastro", usuarioCadastrado);
                    startActivity(intent);

                }else{
                    // User is signed out
                    Log.e("Login.java", "onAuthStateChanged:signed_out");
                }
            }
        };

        // Instanciando os EditTexts
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtSenha);

        //Instanciando o Switch
        swKeepConnected = (Switch) findViewById(R.id.sw_KeepConnected);

        //Instanciando os botões
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        //Instanciando o progressBar
        progressBar = (ProgressBar) findViewById(R.id.progresso);

        //Definindo a ação tomada ao clicar em login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseLogin(email.getText().toString(), password.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        //Definindo a ação tomada ao clicar em Cadastro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseRegister(email.getText().toString(), password.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        authentication.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.e(getClass().toString(), "Erro ao fazer login pelo Facebook");
                }else{
                    loginFacebook = true;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);
        return(true);
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
            if (!userLogged.isManterConectado()) {
                FirebaseAuth.getInstance().signOut();
            }
        }

        if (authenticationListener != null) {
            authentication.removeAuthStateListener(authenticationListener);
        }
    }

    // Função responsável por criar o usuário tradicional e após isso, realizar o login do mesmo
    private void firebaseRegister(String email, String pass){
        usuarioCadastrado = true;

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
                            progressBar.setVisibility(View.INVISIBLE);
                            usuarioCadastrado = false;
                        }
                    }
                });
    }

    // Função responsável por fazer o login tradicional do usuário
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
                            Snackbar.make(findViewById(R.id.activity_main), "Não foi possível conectar o usuário", Snackbar.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

}