package quizbiblico.com.claudinei.quizbiblico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class activityc_Help extends AppCompatActivity {

    //TextView
    private TextView txtPontuacao;
    private TextView txtPontuacao_Help;
    private TextView txtPowerUPs;
    private TextView txtPowerUPs_Help;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_help);

        instanciaElementosInterface();
        setaElementosInterface();

    }

    public void instanciaElementosInterface(){
        txtPontuacao = (TextView) findViewById(R.id.help_pontuacao);
        txtPontuacao_Help = (TextView) findViewById(R.id.help_pontuacao_help);
        txtPowerUPs = (TextView) findViewById(R.id.help_powerups);
        txtPowerUPs_Help = (TextView) findViewById(R.id.help_powerups_help);
    }
    public void setaElementosInterface(){
        txtPontuacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtPontuacao_Help.getVisibility() == View.VISIBLE) {
                    txtPontuacao_Help.setVisibility(View.INVISIBLE);

                    txtPontuacao_Help.setText("");
                }else{
                    txtPontuacao_Help.setVisibility(View.VISIBLE);

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
        });

        txtPowerUPs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtPowerUPs_Help.getVisibility() == View.VISIBLE) {
                    txtPowerUPs_Help.setVisibility(View.INVISIBLE);

                    txtPowerUPs_Help.setText("");

                }else{
                    txtPowerUPs_Help.setVisibility(View.VISIBLE);

                    txtPowerUPs_Help.setText(String.format(getString(R.string.help_como_funcionam_os_powerups),
                            Parameter.MAIS_TEMPO));
                }


            }
        });

    }

}
