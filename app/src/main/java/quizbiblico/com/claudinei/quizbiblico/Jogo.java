package quizbiblico.com.claudinei.quizbiblico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Jogo extends AppCompatActivity {

    // Objeto que receberá a questão aleatória
    private Question question;

    /*  Array de Botões
            0 - Alternativa A
            1 - Alternativa B
            2 - Alternativa C
            3 - Alternativa D   */
    private ArrayList<Button> botoes = new ArrayList<Button>();

    // TextView do nome da pergunta
    private TextView txtPergunta;

    // Objeto que receberá o usuário
    private Usuario usuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        // Recebe os dados da Activity anterior
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            usuario = (Usuario) extra.getSerializable("usuario");
        }

        // Instanciando os botões das Alternativas
        botoes.add((Button) findViewById(R.id.btnA));
        botoes.add((Button) findViewById(R.id.btnB));
        botoes.add((Button) findViewById(R.id.btnC));
        botoes.add((Button) findViewById(R.id.btnD));

        // Instanciando o click dos botões
        botoes.get(0).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(0);}});
        botoes.get(1).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(1);}});
        botoes.get(2).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(2);}});
        botoes.get(3).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(3);}});

        // Instanciando o TextView da questão
        txtPergunta = (TextView) findViewById(R.id.txtPergunta);

        // Chamada para função que irá preencher os textos na tela
        preencheTela();
    }

    private void preencheTela(){

        // Recebendo a questão aleatória
        //question = QuestionDAO.getAleatoryQuestion(usuario.getAnswered());
        question = new Question("Jesus me ama?", 0, "Sim", "Não", "Jamais", "Porquê?", "João 3:16", 1);

        // Preechimento do nome da questão
        txtPergunta.setText(question.getQuestion());

        // Coloca o texto nos botões de maneira aleatória, ou seja, a cada vez que
        //  chegar na questão, as alternativas serão apresentadas de forma diferente
        ArrayList<String> alternativas = new ArrayList<>();
        alternativas.add(question.getAlternative_A());
        alternativas.add(question.getAlternative_B());
        alternativas.add(question.getAlternative_C());
        alternativas.add(question.getAlternative_D());

        //Variável booleana que identifica a comutação da resposta correta
        boolean trocou = false;

        // Colocando os textos nos botões
        Random random = new Random();
        int alternativaAleatoria;
        for (int i = 0; i < 4; i++){
            alternativaAleatoria = random.nextInt(alternativas.size());

            if (i == question.getAnswer() && trocou == false){
                trocou = true;
                question.setAnswer(alternativaAleatoria);
                Log.d("Jogo", "Chegou aqui sim.\n\n" + String.valueOf(i) + "\n" + String.valueOf(alternativaAleatoria) + "\n" + String.valueOf(question.getAnswer()));
            }

            botoes.get(i).setText(alternativas.get(alternativaAleatoria));
            alternativas.remove(alternativaAleatoria);
        }
    }

    private void tentativa(int alternativaUsuario){
        Toast.makeText(getApplicationContext(), "Clicado: " + String.valueOf(alternativaUsuario) + "\nCorreta: " + String.valueOf(question.getAnswer()), Toast.LENGTH_SHORT).show();

    }

}
