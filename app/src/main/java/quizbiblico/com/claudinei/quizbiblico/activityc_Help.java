package quizbiblico.com.claudinei.quizbiblico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class activityc_Help extends AppCompatActivity {

    //TextView
    private TextView txtPontuacao;
    private TextView txtPontuacao_Help;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_help);

        instanciaElementosInterface();
        setaElementosInterface();

    }

    public void instanciaElementosInterface(){
        txtPontuacao = (TextView) findViewById(R.id.help_pontuacao) ;
        txtPontuacao_Help = (TextView) findViewById(R.id.help_pontuacao_help) ;
    }
    public void setaElementosInterface(){
        txtPontuacao_Help.setText(String.format(getString(R.string.help_como_funciona_pontuacao),
                Parameter.PONTOS_ACERTO_DIFICIL,
                Parameter.PONTOS_ACERTO_MEDIO,
                Parameter.PONTOS_ACERTO_FACIL,
                Parameter.PONTOS_ERRO_DIFICIL,
                Parameter.PONTOS_ERRO_MEDIO,
                Parameter.PONTOS_ERRO_FACIL,
                Parameter.PONTOS_TEMPO_19_15,
                Parameter.PONTOS_TEMPO_14_10,
                Parameter.PONTOS_TEMPO_9_5));
    }

}
