package quizbiblico.com.claudinei.quizbiblico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
    private Button btnAjuda;

    // TextView do nome da pergunta
    private TextView txtPergunta;

    // Objeto que receberá o usuário
    private Usuario usuario;

    // Número de alternativas que o usuário eliminou. (O limite é 3, pois restaria apenas a alternativa correta)
    private int alternativasEliminadas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        // Recebe os dados da Activity anterior
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            usuario = (Usuario) extra.getSerializable("usuario");
            Toast.makeText(getApplicationContext(), "Aqui\n\n" + usuario.toString(), Toast.LENGTH_SHORT).show();
        }

        // Instanciando os botões
        botoes.add((Button) findViewById(R.id.btnA));
        botoes.add((Button) findViewById(R.id.btnB));
        botoes.add((Button) findViewById(R.id.btnC));
        botoes.add((Button) findViewById(R.id.btnD));
        btnAjuda = (Button) findViewById(R.id.btnAjuda);

        // Instanciando o click dos botões
        botoes.get(0).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(0);}});
        botoes.get(1).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(1);}});
        botoes.get(2).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(2);}});
        botoes.get(3).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(3);}});
        btnAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajuda();
            }
        });

        // Instanciando o TextView da questão
        txtPergunta = (TextView) findViewById(R.id.txtPergunta);


        // Chamada para função que irá preencher os textos na tela
        getQuestion(usuario.getRespondidas());

    }

    private void ajuda(){ // Função responsável por eliminar uma resposta incorreta

        if (alternativasEliminadas < 3) {

            Random random = new Random();
            int alternativaEliminada = random.nextInt(4);

            while (alternativaEliminada == question.getAnswer() || botoes.get(alternativaEliminada).getVisibility() == View.INVISIBLE) {
                alternativaEliminada = random.nextInt(4);
            }

            botoes.get(alternativaEliminada).setVisibility(View.INVISIBLE);
            alternativasEliminadas++;

        }
    }

    private void tentativa(int alternativaUsuario){ // Função acionada no fim da questão ou quando o usuário seleciona a opção
        boolean acertou = alternativaUsuario == question.getAnswer();
        Toast.makeText(getApplicationContext(), String.valueOf(acertou), Toast.LENGTH_SHORT).show();
    }

    private void getQuestion(ArrayList<Integer> excludedQuestions){
        boolean randomOk = false;

        Random random = new Random();
        int randomizedQuestion = 0;

        while (randomOk == false){
            randomizedQuestion = random.nextInt(Parameter.getNextQuestionNum());

            if ((!excludedQuestions.contains(randomizedQuestion)) && (randomizedQuestion != 0)){
                randomOk = true;
            }
        }

        FirebaseDB.getQuestionReference().orderByChild("idQuestion").equalTo(randomizedQuestion).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren() ){
                    question = data.getValue(Question.class);

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
                    for (int i = 0; i <= 3; i++){
                        alternativaAleatoria = random.nextInt(alternativas.size());

                        if (alternativaAleatoria == question.getAnswer() && trocou == false){
                            trocou = true;
                            question.setAnswer(i);
                        }

                        botoes.get(i).setText(alternativas.get(alternativaAleatoria));
                        alternativas.remove(alternativaAleatoria);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
