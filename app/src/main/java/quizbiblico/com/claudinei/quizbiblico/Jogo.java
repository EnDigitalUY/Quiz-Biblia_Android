package quizbiblico.com.claudinei.quizbiblico;

import android.app.AlertDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private Button btnMaisTempo;

    // TextView do nome da pergunta
    private TextView txtPergunta;

    // Objeto que receberá o usuário
    private Usuario usuario;

    // Número de alternativas que o usuário eliminou. (O limite é 3, pois restaria apenas a alternativa correta)
    private int alternativasEliminadas = 0;

    // Contador decremental de tempo
    private int tempoRestante;

    // Runnable responsável por controlar o tempo
    private Runnable controlaTempo;

    // Handler que fará a atualização de informações quando estiver trabalhando noutra Thread (exceto a principal)
    //  e for necessário atualizar informações de UI
    private Handler handler;

    private Thread thread;

    private TextView tempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        // Recebe os dados da Activity anterior
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            usuario = (Usuario) extra.getSerializable("usuario");
        }

        // Responsável por fazer o decremento do tempo
        handler = new Handler();

        // Instanciando os botões
        botoes.add((Button) findViewById(R.id.btnA));
        botoes.add((Button) findViewById(R.id.btnB));
        botoes.add((Button) findViewById(R.id.btnC));
        botoes.add((Button) findViewById(R.id.btnD));
        btnAjuda = (Button) findViewById(R.id.btnAjuda);
        btnMaisTempo = (Button) findViewById(R.id.btnTempo);

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
        btnMaisTempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempoRestante += 5;
            }
        });

        // Instanciando o TextView da questão
        txtPergunta = (TextView) findViewById(R.id.txtPergunta);

        // Instanciando o TextView do tempo
        tempo = (TextView) findViewById(R.id.txtTempo);
        tempo.setText(String.valueOf(tempoRestante));

        // Chamada para função que irá preencher os textos na tela, inclusive exibir o tempo
        proximaQuestao();

        controlaTempo = new Runnable() {
            @Override
            public void run() {
                while (tempoRestante > 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tempo.setText(String.valueOf(tempoRestante));
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    tempoRestante--;

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tentativa(5);
                    }
                });
            }
        };

        thread = new Thread(controlaTempo);

        thread.start();

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (acertou){
            if (Configuracoes.getConexao()){
                usuario.addAnswered(question.getIdQuestion());
                FirebaseDB.getUsuarioReferencia().child(usuario.getUid()).setValue(usuario);
            }
            builder.setTitle("Parabéns! Você acertou");
        }else {
            builder.setTitle("Que pena! Você errou");
        }
        builder.setMessage(question.getTextBiblical());
        AlertDialog dialog = builder.create();

        dialog.show();

        proximaQuestao();

    }

    private void proximaQuestao(){

        alternativasEliminadas = 0;
        for (int i = 0; i < botoes.size(); i++){
            botoes.get(i).setVisibility(View.VISIBLE);
        }

        tempoRestante = 20;

        getQuestion((Configuracoes.getConexao() ? usuario.getRespondidas() : null));
    }

    private void getQuestion(ArrayList<Integer> excludedQuestions){

        if (!Configuracoes.getConexao()) {

            question = new Question("Quem foi Jesus", 3, "Um profeta", "Um juiz", "Um usado", "O Messias", "ele foi nosso Messias", 1);
            botoes.get(0).setText(question.getAlternative_A());
            botoes.get(1).setText(question.getAlternative_B());
            botoes.get(2).setText(question.getAlternative_C());
            botoes.get(3).setText(question.getAlternative_D());
        } else {
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

}
