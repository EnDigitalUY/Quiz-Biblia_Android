package quizbiblico.com.claudinei.quizbiblico;

import android.media.Image;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

    private Usuario usuario;

    private ImageView btnEditarEmail;
    private ImageView btnEditarSenha;

    private boolean emailAlterado = false;
    private boolean senhaAlterada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_perfil);

        //Verifica se existem dados vindouros da tela anterior
        Bundle extra = getIntent().getExtras();
        if (extra != null) {

            // Recebe o usuário da tela anterior
            usuario = (Usuario) extra.getSerializable("usuario");
        }

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

    }

    private void setaElementosInterface() {

        //EditText
        txtEmail.setText(           usuario.getEmail());
        txtNome.setText(            usuario.getNome());
        //txtSenha.getSenha
        txtPUPTempo.setText(        String.valueOf(usuario.getBonus().getBonusTempo()));
        txtPUPAlternativa.setText(  String.valueOf(usuario.getBonus().getBonusAlternativa()));
        txtPUPReferencia.setText(   String.valueOf(usuario.getBonus().getBonusTexto()));
        txtPontuacao.setText(       String.valueOf(usuario.getPontuacao()));
        txtQuestoes.setText(        String.valueOf(usuario.getRespondidas().size()));

        //Switch
        swSons.setChecked(usuario.getPreferencias().isSons());
        swVibracao.setChecked(usuario.getPreferencias().isVibracao());

        //ImageView
        btnEditarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail.setEnabled(!txtEmail.isEnabled());
                if(txtEmail.isEnabled())
                    txtEmail.requestFocus();
            }
        });


        btnEditarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSenha.setEnabled(!txtSenha.isEnabled());
                if (txtSenha.isEnabled())
                    txtSenha.requestFocus();
            }
        });

    }
}
