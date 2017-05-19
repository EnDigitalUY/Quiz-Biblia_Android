package quizbiblico.com.claudinei.quizbiblico;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;

public class activityc_Login extends AppCompatActivity {

    //Firebase Realtime Database
    private FirebaseDB database;

    //Firebase Authentication
    private FirebaseAuth authentication;
    private FirebaseAuth.AuthStateListener authenticationListener;

    //Botões
    private Button btnLogin;
    private Button btnRegister;
    private LoginButton facebookLoginButton;

    //ImageView
    private ImageView showPassword;

    //EditText
    private EditText email;
    private EditText password;

    //TextView
    private TextView forgetPassword;

    //Usuário
    private Usuario userLogged = null;

    //Variável que identifica se é uma criação ou não de um usuário
    private boolean usuarioCadastrado;

    //Variável que identifica se é um activityl_login pelo Facebook
    private boolean loginFacebook = false;

    //CallbackManager responsável por auxiliar na conexão pelo Facebook
    private CallbackManager callbackManager;

    private CoordinatorLayout layoutPrincipal;
    public static RelativeLayout telaLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instanciando o SDK do Facebook para utilizá-lo
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activityl_login);

        //Criação do callbackManager para realizar o activityl_login pelo Facebook
        callbackManager = CallbackManager.Factory.create();

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

                // Verifica se o usuário vindouro da autenticação é diferente   de nulo, ou seja, está autenticado
                if (user != null){
                    userLogged = new Usuario(user.getEmail(), user.getDisplayName(), user.getUid().toString());

                    // Verifica se é a autenticação é vindoura de um cadastro de usuário
                    if (usuarioCadastrado)
                        FirebaseDB.getUsuarioReferencia().child(userLogged.getUid()).setValue(userLogged);

                    //Indo para a próxima tela
                    Intent intent = new Intent(activityc_Login.this, activityc_MenuPrincipal.class);
                    intent.putExtra("userLogged", userLogged);
                    intent.putExtra("cadastro", usuarioCadastrado);

                    layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));

                    startActivity(intent);

                }else{
                    // User is signed out
                    Log.e("activityc_Login.java", "onAuthStateChanged:signed_out");
                }
            }
        };

        instanciaElementosInterface();
        setaElementosInterface();

        //Instanciando o botão de activityl_login do Facebook
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

        //Verifica se o usuário já estava conectado pelo Facebook, caso já esteja, o reconecta
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logInWithReadPermissions(this, AccessToken.getCurrentAccessToken().getPermissions());
        }

    }

    private void instanciaElementosInterface() {
        //EditText
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtSenha);

        //Buttons
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        //Instanciando o botão de activityl_login do Facebook
        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);

        //ImageView
        showPassword = (ImageView) findViewById(R.id.login_showpassword);

        //TextView
        forgetPassword = (TextView) findViewById(R.id.login_forget_password);

        layoutPrincipal = (CoordinatorLayout) findViewById(R.id.layout_login);
        telaLoading = (RelativeLayout) findViewById(R.id.login_layout_carregando);

    }

    private void setaElementosInterface() {
        //Definindo a ação tomada ao clicar em activityl_login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String returnVerifyFields = verifyFields();

                if (returnVerifyFields.equals("")) {
                    firebaseLogin(email.getText().toString(), password.getText().toString());
                    telaLoading.setVisibility(View.VISIBLE);
                }else{
                        Snackbar.make(findViewById(R.id.activity_main), returnVerifyFields, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //Definindo a ação tomada ao clicar em Cadastro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String returnVerifyFields = verifyFields();

                if (returnVerifyFields.equals("")) {
                    firebaseRegister(email.getText().toString(), password.getText().toString());
                    telaLoading.setVisibility(View.VISIBLE);
                }else{
                    Snackbar.make(findViewById(R.id.activity_main), returnVerifyFields, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //Definindo o botão que ao clicar exibe/esconde a senha
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword.setImageResource(R.drawable.img_locked);
                }else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassword.setImageResource(R.drawable.img_unlocked);
                }
            }
        });

        //Define a ação do botão de esqueci minha senha
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String returnVerifyFields = verifyFields(false);

                if (returnVerifyFields.equals("")) {
                    telaLoading.setVisibility(View.VISIBLE);
                    authentication.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(findViewById(R.id.activity_main), "E-mail de redefinição de senha enviado com sucesso", Snackbar.LENGTH_SHORT).show();
                                        telaLoading.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }else{
                    Snackbar.make(findViewById(R.id.activity_main), returnVerifyFields, Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        Glide.with(this)
                .load(R.drawable.other_loading)
                .asGif()
                .into((ImageView) findViewById(R.id.login_progresso));

    }

    public String verifyFields(){
        return verifyFields(true);
    }

    private String verifyFields(boolean verifyPass) {
        boolean emailFilled = true;
        boolean passwordFilled = true;

        if (email.getText().toString().equals(""))
            emailFilled = false;
        else
            emailFilled = true;

        if (verifyPass) {
            if (password.getText().toString().equals(""))
                passwordFilled = false;
            else
                passwordFilled = true;
        }

        String mensagem = "";
        if (!emailFilled && !passwordFilled)
            mensagem = "Por favor, preencha o e-mail e a senha.";
        else{
            if (!emailFilled)
                mensagem = "Por favor, preencha o e-mail.";
            else{
                if (!passwordFilled)
                    mensagem = "Por favor, preencha a senha.";
            }
        }

        return mensagem;

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        authentication.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.e(getClass().toString(), "Erro ao fazer activityl_login pelo Facebook");
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

    // No início da aplicação, seta o Listener para acompanhar as mudanças na autenticação
    @Override
    public void onStart() {
        super.onStart();
        authentication.addAuthStateListener(authenticationListener);

        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));

    }

    // No término da aplicação, remove o Listener que acompanha as mudanças na autenticação
    @Override
    public void onStop() {
        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));

        super.onStop();

        if (authenticationListener != null) {
            authentication.removeAuthStateListener(authenticationListener);
        }
    }

    // Função responsável por criar o usuário tradicional e após isso, realizar o activityl_login do mesmo
    private void firebaseRegister(String email, String pass){
        usuarioCadastrado = true;

        authentication.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("activityc_Login.java", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Snackbar.make(findViewById(R.id.activity_main), "Não foi possível criar o usuário", Snackbar.LENGTH_SHORT).show();
                            telaLoading.setVisibility(View.INVISIBLE);
                            usuarioCadastrado = false;
                        }
                    }
                });
    }

    // Função responsável por fazer o activityl_login tradicional do usuário
    private void firebaseLogin(String email, String pass){
        authentication.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("activityc_Login.java", "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Snackbar.make(findViewById(R.id.activity_main), "Não foi possível conectar o usuário", Snackbar.LENGTH_SHORT).show();
                            telaLoading.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

}