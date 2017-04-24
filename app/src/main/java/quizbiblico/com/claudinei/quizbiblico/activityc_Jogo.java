package quizbiblico.com.claudinei.quizbiblico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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

import java.lang.reflect.Array;
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
    private ArrayList<Button> botoes;

    //TextView que exibe o texto da pergunta, seção da bíblia e txtDificuldade da questão
    private TextView txtPergunta;
    private TextView txtDificuldade;
    private TextView txtSecao;

    // Objeto que receberá o usuário
    private Usuario usuario;

    // Número de alternativas que o usuário eliminou. (O limite é 3, pois restaria apenas a alternativa correta)
    private int alternativasEliminadas = 0;

    // Contador de itemTempo
    private int tempoRestante;
    private final int TEMPOTOTAL = 20;

    // Handler que fará a atualização de informações quando estiver trabalhando noutra Thread (exceto a principal)
    //  e for necessário atualizar informações de UI
    private Handler handler;

    //No início dos tempos a thread ainda não é executada
    private boolean executaThread = false;

    //Parametros para a seleção das perguntas
    private ArrayList<String> parametros;

    //Menu que contém algumas opções
    private Menu menu;

    //Item de menu que exibe a pontuação
    private MenuItem itemPontuacao;

    //Item de menu que exibe o itemTempo restante
    private MenuItem itemTempo;

    //Pontuação obtida nesta partida
    private int pontuacaoPartida;

    private long tempoInicial;

    private class Partida{
        public int pontuacao;
        public Array acertos;   //[0] - Difícil | [1] - Média | [2] - Fácil
        public Array erros;     //[0] - Difícil | [1] - Média | [2] - Fácil
        //public long itemTempo;

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_jogo);

        // Recebe os dados da Activity anterior
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            usuario = (Usuario) extra.getSerializable("usuario");
            parametros = extra.getStringArrayList("parametros");
        }

        instanciaElementosInterface();
        setaElementosInterface();

        // Handler responsável por fazer o decremento do itemTempo
        handler = new Handler();

        // Runnable responsável por controlar o itemTempo
        Runnable controlaTempo = new Runnable() {
            @Override
            public void run() {

                while (tempoRestante >= 0) {

                    if (executaThread) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                itemTempo.setTitle(String.valueOf(tempoRestante) + "''");
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

        // Chamada para função que irá preencher os textos na tela, inclusive exibir o itemTempo
        proximaQuestao();

    }

    private void instanciaElementosInterface() {
        // Botões
        botoes = new ArrayList<>();
        botoes.add((Button) findViewById(R.id.btnA));
        botoes.add((Button) findViewById(R.id.btnB));
        botoes.add((Button) findViewById(R.id.btnC));
        botoes.add((Button) findViewById(R.id.btnD));

        //TextViews
        txtPergunta = (TextView) findViewById(R.id.txtPergunta);
        txtDificuldade = (TextView) findViewById(R.id.filtros_txtDificuldade);
        txtSecao = (TextView) findViewById(R.id.txtSecao);

        pontuacaoPartida = 0;
    }

    private void setaElementosInterface() {
        // Instanciando o click dos botões
        botoes.get(0).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(0);}});
        botoes.get(1).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(1);}});
        botoes.get(2).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(2);}});
        botoes.get(3).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {tentativa(3);}});

    }

    private void atualizaMenuBonus(){ //Função responsável por atualizar todos os menus de bonus, inserindo o número de bonus restantes
        for (int i = 0; i < menu.size(); i++) {
            atualizaMenuBonus(menu.getItem(i));
        }
    }

    private void atualizaMenuBonus(MenuItem menuItem){ //Função responsável por atualizar todos um de bonus, inserindo o número de bonus restantes
        switch (menuItem.getItemId()){
            case R.id.menu_bonus_exibetextobiblico:
            {
                menuItem.setTitle(getString(R.string.jogo_bonus_referenciabiblica) + " (" + usuario.getBonusTexto() + ")");
                break;
            }
            case R.id.menu_bonus_eliminaalternativa:
            {
                menuItem.setTitle(getString(R.string.jogo_bonus_eliminaalternativa) + " (" + usuario.getBonusAlternativa() + ")");
                break;
            }
            case R.id.menu_bonus_maistempo:
            {
                menuItem.setTitle(getString(R.string.jogo_bonus_maistempo) + " (" + usuario.getBonusTempo() + ")");
                break;
            }
        }
    }

    private void maisTempo(){ // Função responsável por acrescentar mais itemTempo para responder
        if (usuario.getBonusTempo() > 0) {
            tempoRestante += Parameter.MAIS_TEMPO;

            usuario.setBonusTempo(-1);

            atualizaMenuBonus(menu.findItem(R.id.menu_bonus_maistempo));
        }else
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.bonus_insuficiente), " acrescentar mais itemTempo para responder"), Toast.LENGTH_SHORT).show();
    }

    private void ajuda(){ // Função responsável por eliminar uma resposta incorreta

        if (usuario.getBonusAlternativa() > 0) {
            if (alternativasEliminadas < 3) {

                Random random = new Random();
                int alternativaEliminada = random.nextInt(4);

                while (alternativaEliminada == question.getAnswer() || botoes.get(alternativaEliminada).getVisibility() == View.INVISIBLE) {
                    alternativaEliminada = random.nextInt(4);
                }

                botoes.get(alternativaEliminada).setVisibility(View.INVISIBLE);
                alternativasEliminadas++;

                usuario.setBonusAlternativa(-1);

                atualizaMenuBonus(menu.findItem(R.id.menu_bonus_eliminaalternativa));
            }
        }else
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.bonus_insuficiente), "eliminar uma alternativa incorreta"), Toast.LENGTH_SHORT).show();
    }

    private void exibeReferenciaBiblica(){

        if (usuario.getBonusTexto() > 0){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Referência bíblica");
            builder.setIcon(R.drawable.img_icon);
            builder.setMessage(question.getReferenciaBiblica());

            if(!isFinishing())
                builder.create().show();

            builder = null;

            usuario.setBonusTexto(-1);

            atualizaMenuBonus(menu.findItem(R.id.menu_bonus_exibetextobiblico));

        }else
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.bonus_insuficiente), "exibir a referência bíblica"), Toast.LENGTH_SHORT).show();

    }

    private void tentativa(int alternativaUsuario){ // Função acionada no fim da questão ou quando o usuário seleciona a opção

        // Variável local que exibirá a pontuação obtida com esta questão
        int pontuacaoTentativa = 0;

        // Para com a thread que decrementa o itemTempo
        executaThread = false;

        //Bloqueia os controles do usuário
        bloqueia_desbloqueiaControles(false);

        // Verifica se o usuário acertou
        boolean acertou = alternativaUsuario == question.getAnswer();

        // Cria um AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (acertou){
            usuario.addAnswered(question.getIdQuestion());

            // Reproduz um som de correto
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.resposta_correta);
            mediaPlayer.start();

            // Pinta de verde a alternativa correta
            botoes.get(alternativaUsuario).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quadro_alternativa_correta));

            // Soma pontuação por dificuldade
            pontuacaoTentativa += (   question.getLevelQuestion() == 3 ? Parameter.PONTOS_ACERTO_DIFICIL :
                                        (question.getLevelQuestion() == 2 ? Parameter.PONTOS_ACERTO_MEDIO :
                                                (question.getLevelQuestion() == 1 ? Parameter.PONTOS_ACERTO_FACIL : 0)
                                        )
                                );

            // Soma pontuação por itemTempo
            pontuacaoTentativa += (   tempoRestante >= 15 ? Parameter.PONTOS_TEMPO_19_15 :
                                        (tempoRestante <= 14 && tempoRestante >= 10 ? Parameter.PONTOS_TEMPO_14_10 :
                                                (tempoRestante <= 9 && tempoRestante >= 5 ? Parameter.PONTOS_TEMPO_9_5 :
                                                        (tempoRestante <= 4 ? Parameter.PONTOS_TEMPO_4_0 : 0)
                                                )
                                        )

                                );

            // Seta o título e o ícone do AlertDialog como corretos
            builder.setTitle("Parabéns! Você acertou\n" + "+ " + pontuacaoTentativa + " pontos");
            builder.setIcon(R.drawable.ico_correct);

        }else {

            // Reproduz um som de incorreto
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.resposta_incorreta);
            mediaPlayer.start();

            if (alternativaUsuario != 5) {
                // Pinta de vermelho a alternativa incorreta
                botoes.get(alternativaUsuario).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quadro_alternativa_incorreta));
            }else{
                for (int i = 0; i < botoes.size(); i++){
                    botoes.get(i).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quadro_alternativa_incorreta));
                }
            }

            // Decrementa pontuação por erro
            pontuacaoTentativa += Parameter.PONTOS_ERRO;

            // Decrementa pontuação por dificuldade
            pontuacaoTentativa +=   (   question.getLevelQuestion() == 3 ? Parameter.PONTOS_ERRO_DIFICIL :
                                            (question.getLevelQuestion() == 2 ? Parameter.PONTOS_ERRO_MEDIO :
                                                (question.getLevelQuestion() == 1 ? Parameter.PONTOS_ERRO_FACIL : 0)
                                            )
                                    );

            // Seta o título e o ícone do AlertDialog como incorretos
            builder.setTitle("Que pena! Você errou\n" + "- " + (pontuacaoTentativa * -1) + " pontos");
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
        if(!isFinishing())
            builder.create().show();

        // Destrói as variáveis
        builder = null;

        // Soma a pontuação obtida nesta tentativa à pontuação da partida
        pontuacaoPartida += pontuacaoTentativa;

        // Exibe a pontuação da partida no menu
        itemPontuacao.setTitle("Pontuação: " + String.valueOf(pontuacaoPartida));
    }

    private void proximaQuestao(){

        alternativasEliminadas = 0;
        for (int i = 0; i < botoes.size(); i++){
            botoes.get(i).setVisibility(View.VISIBLE);
            botoes.get(i).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quadro_alternativa));
        }

        tempoRestante = TEMPOTOTAL;

        getQuestion(usuario.getRespondidas());
    }

    public void bloqueia_desbloqueiaControles(boolean bloqueio){
        for (int i = 0; i < botoes.size(); i++){
            botoes.get(i).setClickable(bloqueio);
        }

        for (int i = 0; i < menu.size(); i++){
            menu.getItem(i).setEnabled(bloqueio);
        }

    }

    private void getQuestion(ArrayList<Integer> questoesRespondidasPeloUsuario){
        boolean randomOk = false;

        Random random = new Random();
        int questaoAletoria = 0;

        while (!randomOk){
            questaoAletoria = random.nextInt(Parameter.getNextQuestionNum());

            if ((!questoesRespondidasPeloUsuario.contains(questaoAletoria)) && (questaoAletoria != 0)){
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
                    txtSecao.setText(   "Seção da bíblia:\n" +
                                        question.getSecaoBiblia() +
                                        " (" + question.getTestamento().replace("Antigo", "A.T.").replace("Novo", "N. T.") + ") ".replace(" N. T(", "("));

                    switch (question.getLevelQuestion()){
                        case 1:{
                            txtDificuldade.setText("Dificuldade:\n" + getString(R.string.dificuldades_facil));
                            break;
                        }
                        case 2:{
                            txtDificuldade.setText("Dificuldade:\n" + getString(R.string.dificuldades_medio));
                            break;
                        }
                        case 3:{
                            txtDificuldade.setText("Dificuldade:\n" + getString(R.string.dificuldades_dificil));
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

        FirebaseDB.getQuestionReference().orderByChild("idQuestion").equalTo(questaoAletoria).addListenerForSingleValueEvent(buscaQuestoes);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu){
        super.onCreateOptionsMenu(_menu);

        getMenuInflater().inflate(R.menu.menu_jogo, _menu);

        //MenuItems
        itemPontuacao = _menu.findItem(R.id.menu_pontuacao);
        itemTempo = _menu.findItem(R.id.menu_tempo);
        itemTempo.setTitle(String.valueOf(tempoRestante) + "''");        //Seta pela primeira vez o itemTempo restante

        menu = _menu;

        bloqueia_desbloqueiaControles(false);

        atualizaMenuBonus();

        return(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_bonus_maistempo: {
                maisTempo();
                return true;
            }
            case R.id.menu_bonus_eliminaalternativa:{
                ajuda();
                return true;
            }
            case R.id.menu_bonus_exibetextobiblico:{
                exibeReferenciaBiblica();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();

        usuario.setPontuacao(pontuacaoPartida);
        FirebaseDB.getUsuarioReferencia().child(usuario.getUid()).setValue(usuario);

        finish();
    }

}
