package quizbiblico.com.claudinei.quizbiblico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;


public class activityc_Perfil extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtNome;
    private EditText txtSenha;
    private EditText txtPUPTempo;
    private EditText txtPUPAlternativa;
    private EditText txtPUPReferencia;
    private EditText txtPontuacao;
    private EditText txtQuestoes;

    private Switch swSons;
    private Switch swVibracao;

    private ImageView btnEditarEmail;
    private ImageView btnEditarSenha;

    private ImageView btnCancelar;
    private ImageView btnSalvar;

    private LinearLayout layoutPrincipal;

    private boolean emailAlterado = false;
    private boolean senhaAlterada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_perfil);

        instanciaElementosInterface();
        setaElementosInterface();

    }

    private void instanciaElementosInterface() {
        //EditText
        txtEmail            = (EditText) findViewById(R.id.perfil_txtEmail);
        txtNome             = (EditText) findViewById(R.id.perfil_txtNome);
        txtSenha            = (EditText) findViewById(R.id.perfil_txtSenha);
        txtPUPTempo         = (EditText) findViewById(R.id.perfil_powerUP_tempo);
        txtPUPAlternativa   = (EditText) findViewById(R.id.perfil_powerUP_alternativa);
        txtPUPReferencia    = (EditText) findViewById(R.id.perfil_powerUP_referencia);
        txtPontuacao        = (EditText) findViewById(R.id.perfil_pontuacao);
        txtQuestoes         = (EditText) findViewById(R.id.perfil_pontuacao_acertos);

        //Switch
        swSons      =   (Switch) findViewById(R.id.perfil_swSons);
        swVibracao  =   (Switch) findViewById(R.id.perfil_swVibracao);

        //ImageView
        btnEditarEmail =    (ImageView) findViewById(R.id.perfil_btnEditEmail);
        btnEditarSenha =    (ImageView) findViewById(R.id.perfil_btnEditSenha);
        btnCancelar =       (ImageView) findViewById(R.id.perfil_btnCancelar);
        btnSalvar =         (ImageView) findViewById(R.id.perfil_btnSalvar);

        layoutPrincipal = (LinearLayout) findViewById(R.id.layout_perfil);


    }

    private void setaElementosInterface() {

        //EditText
        txtEmail.setText(           activityc_MenuPrincipal.usuario.getEmail());
        txtNome.setText(            activityc_MenuPrincipal.usuario.getNome());
        txtPUPTempo.setText(        String.valueOf(activityc_MenuPrincipal.usuario.getBonus().getBonusTempo()));
        txtPUPAlternativa.setText(  String.valueOf(activityc_MenuPrincipal.usuario.getBonus().getBonusAlternativa()));
        txtPUPReferencia.setText(   String.valueOf(activityc_MenuPrincipal.usuario.getBonus().getBonusReferenciaBiblica()));
        txtPontuacao.setText(       String.valueOf(activityc_MenuPrincipal.usuario.getPontuacao()));
        txtQuestoes.setText(        String.valueOf(activityc_MenuPrincipal.usuario.getRespondidas().size()));

        //Switch
        swSons.setChecked(activityc_MenuPrincipal.usuario.getPreferencias().isSons());
        swVibracao.setChecked(activityc_MenuPrincipal.usuario.getPreferencias().isVibracao());

        //ImageView
        btnEditarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail.setEnabled(!txtEmail.isEnabled());
                if(txtEmail.isEnabled())
                    txtEmail.requestFocus();
                else
                    emailAlterado = (! txtEmail.getText().toString().equals(activityc_MenuPrincipal.usuario.getEmail()));
            }
        });

        btnEditarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSenha.setEnabled(!txtSenha.isEnabled());
                if (txtSenha.isEnabled())
                    txtSenha.requestFocus();
                else
                    senhaAlterada = (! txtSenha.getText().toString().isEmpty());
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria um AlertDialog que confirmará se o usuário realmente quer sair
                AlertDialog.Builder builder = new AlertDialog.Builder(activityc_Perfil.this);
                builder.setTitle("Você deseja sair?");
                builder.setMessage("Ao sair, todas as alterações serão desfeitas. Você deseja sair?");
                builder.setCancelable(false);
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(findViewById(R.id.activity_perfil), "Alterações descartadas", Snackbar.LENGTH_LONG).show();
                        finish();
                    }
                });

                if(!isFinishing())
                    builder.create().show();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria um AlertDialog que confirmará se o usuário realmente quer salvar
                AlertDialog.Builder builder = new AlertDialog.Builder(activityc_Perfil.this);
                builder.setTitle("Você deseja gravar?");
                builder.setMessage("Ao confirmar, todas as alterações não poderão mais ser desfeitas. Você deseja gravar as alterações?");
                builder.setCancelable(false);
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try{

                            activityc_MenuPrincipal.usuario.setNome(txtNome.getText().toString());
                            activityc_MenuPrincipal.usuario.getPreferencias().setSons(swSons.isChecked());
                            activityc_MenuPrincipal.usuario.getPreferencias().setVibracao(swVibracao.isChecked());
                            FirebaseDB.getUsuarioReferencia().child(activityc_MenuPrincipal.usuario.getUid()).setValue(activityc_MenuPrincipal.usuario);

                            Snackbar.make(findViewById(R.id.activity_perfil), "Alterações gravadas", Snackbar.LENGTH_SHORT).show();
                            finish();
                        } catch (Exception e){
                            Snackbar.make(findViewById(R.id.activity_perfil), "Houve uma falha! Tente novamente mais tarde", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

                if(!isFinishing())
                    builder.create().show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
    }

    @Override
    protected void onStop() {

        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));

        super.onStop();
    }
}
