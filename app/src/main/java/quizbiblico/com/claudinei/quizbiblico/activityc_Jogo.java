package quizbiblico.com.claudinei.quizbiblico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Random;

public class activityc_Jogo extends AppCompatActivity {

    // Objeto que receberá a questão aleatória
    private Question question;

    /*  Array de Botões
            0 - Alternativa A
            1 - Alternativa B
            2 - Alternativa C
            3 - Alternativa D   */
    private ArrayList<Button> botoes = new ArrayList<>();

    // TextView do nome da pergunta
    private TextView txtPergunta;

    //TextVie que exibe o tempo restante, seção da bíblia e txtDificuldade da questão
    private TextView tempo;
    private TextView txtDificuldade;
    private TextView txtSecao;

    // Objeto que receberá o usuário
    private Usuario usuario;

    // Número de alternativas que o usuário eliminou. (O limite é 3, pois restaria apenas a alternativa correta)
    private int alternativasEliminadas = 0;

    // Contador decremental de tempo
    private int tempoRestante;

    // Handler que fará a atualização de informações quando estiver trabalhando noutra Thread (exceto a principal)
    //  e for necessário atualizar informações de UI
    private Handler handler;

    private boolean executaThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_jogo);

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

        // Instanciando o click dos botões
        botoes.get(0).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(0);}});
        botoes.get(1).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(1);}});
        botoes.get(2).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(2);}});
        botoes.get(3).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(3);}});

        // Instanciando o TextView da questão
        txtPergunta = (TextView) findViewById(R.id.txtPergunta);

        // Instanciando o TextView do tempo
        tempo = (TextView) findViewById(R.id.txtTempo);
        tempo.setText(String.valueOf(tempoRestante));

        // Instanciando os TextViews da Dificuldade e da seção da bíblia
        txtDificuldade = (TextView) findViewById(R.id.filtros_txtDificuldade);
        txtSecao = (TextView) findViewById(R.id.txtSecao);

        // Chamada para função que irá preencher os textos na tela, inclusive exibir o tempo
        proximaQuestao();

        // Runnable responsável por controlar o tempo
        Runnable controlaTempo = new Runnable() {
            @Override
            public void run() {
                while (tempoRestante >= 0) {
                    if (tempoRestante == 0)
                        bloqueia_desbloqueiaControles(false);

                    if (executaThread) {
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
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tentativa(5);
                    }
                });

            }
        };

        new Thread(controlaTempo).start();

    }

    private void maisTempo(){
        tempoRestante += 5;
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

        // Pára com a thread que decrementa o tempo
        executaThread = false;

        //Bloqueia os controles do usuário
        bloqueia_desbloqueiaControles(false);

        // Verifica se o usuário acertou
        boolean acertou = alternativaUsuario == question.getAnswer();

        // Cria um AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (acertou){
            usuario.addAnswered(question.getIdQuestion());
            FirebaseDB.getUsuarioReferencia().child(usuario.getUid()).setValue(usuario);

            // Seta o título e o ícone do AlertDialog como corretos
            builder.setTitle("Parabéns! Você acertou");
            builder.setIcon(R.drawable.ico_correct);
        }else {
            // Seta o título e o ícone do AlertDialog como incorretos
            builder.setTitle("Que pena! Você errou");
            builder.setIcon(R.drawable.ico_wrong);
        }

        // Seta a mensagem do AlertDialog que é o texto bíblico
        builder.setMessage(question.getTextBiblical());

        //Seta a ação do usuário a clicar no botão de Próxima Questão
        builder.setNeutralButton("Próxima Questão", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Pausa a execução da thread
                executaThread = false;

                //Chamada à próxima questão
                proximaQuestao();

            }
        });

        // Não permite que o usuário "cancele" a mensagem de alerta tocando em outro lugar da tela
        builder.setCancelable(false);

        // Instancia o AlertDialog e o exibe
        builder.create().show();

        // Destrói as variáveis
        builder = null;
    }

    private void proximaQuestao(){

        alternativasEliminadas = 0;
        for (int i = 0; i < botoes.size(); i++){
            botoes.get(i).setVisibility(View.VISIBLE);
        }

        tempoRestante = 20;

        getQuestion(usuario.getRespondidas());
    }

    public void bloqueia_desbloqueiaControles(boolean bloqueio){
        for (int i = 0; i < botoes.size(); i++){
            botoes.get(i).setClickable(bloqueio);
        }
    }

    private void getQuestion(ArrayList<Integer> excludedQuestions){
        boolean randomOk = false;

        Random random = new Random();
        int randomizedQuestion = 0;

        while (!randomOk){
            randomizedQuestion = random.nextInt(Parameter.getNextQuestionNum());

            if ((!excludedQuestions.contains(randomizedQuestion)) && (randomizedQuestion != 0)){
                randomOk = true;
            }
        }

        ValueEventListener buscaQuestoes = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren() ){
                    question = data.getValue(Question.class);

                    // Preechimento das informações da questão
                    txtPergunta.setText(question.getQuestion());
                    txtSecao.setText(   question.getSecaoBiblia().replace(" 2", "").replace(" 1", "") +
                                        " (" + question.getTestamento().replace("Antigo", "A.T.").replace("Novo", "N. T.") + ") ");
                    switch (question.getLevelQuestion()){
                        case 1:{
                            txtDificuldade.setText("Fácil");
                            break;
                        }
                        case 2:{
                            txtDificuldade.setText("Médio");
                            break;
                        }
                        case 3:{
                            txtDificuldade.setText("Difícil");
                            break;
                        }
                    }

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
                        /*alternativaAleatoria = random.nextInt(alternativas.size());

                        if (alternativaAleatoria == question.getAnswer() && trocou == false){
                            trocou = true;
                            question.setAnswer(i);
                        }*/

                        //botoes.get(i).setText(alternativas.get(alternativaAleatoria));
                        botoes.get(i).setText(alternativas.get(i));
                        //alternativas.remove(alternativaAleatoria);
                    }

                    bloqueia_desbloqueiaControles(true);
                    executaThread = true;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        FirebaseDB.getQuestionReference().orderByChild("idQuestion").equalTo(randomizedQuestion).addListenerForSingleValueEvent(buscaQuestoes);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_jogo, menu);
        return(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_maistempo: {
                maisTempo();
                return true;
            }
            case R.id.menu_eliminaalternativa:{
                ajuda();
                return true;
            }
            case R.id.menu_exibetextobiblico:{
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();

        Toast.makeText(getApplicationContext(), "Saí do jogo", Toast.LENGTH_LONG).show();
        finish();
    }

}
