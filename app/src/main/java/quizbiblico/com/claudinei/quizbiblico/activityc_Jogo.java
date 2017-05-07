package quizbiblico.com.claudinei.quizbiblico;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
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

    // Número de alternativas que o usuário eliminou. (O limite é 3, pois restaria apenas a alternativa correta)
    private int alternativasEliminadas = 0;

    // Contador de itemTempo
    private final int TEMPOTOTAL = 20;
    private int tempoRestante = TEMPOTOTAL;

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

    private RelativeLayout layoutPrincipal;
    private RelativeLayout telaLoading;

    private boolean elementosInstanciados = false;
    private boolean elementosSetados = false;
    private boolean podeExibirNovaQuestao = true;

    private class Partida{
        public int pontuacao;
        public Array acertos;   //[0] - Difícil | [1] - Média | [2] - Fácil
        public Array erros;     //[0] - Difícil | [1] - Média | [2] - Fácil
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityl_jogo);

        instanciaElementosInterface();
        setaElementosInterface();

        // Handler responsável por fazer o decremento do itemTempo
        handler = new Handler();

        // Runnable responsável por controlar o itemTempo
        Runnable controlaTempo = new Runnable() {
            @Override
            public void run() {

                while(true) {
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

                    if (tempoRestante == 0)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tentativa(5);
                            }
                        });
                }

            }
        };

        new Thread(controlaTempo).start();

        // Chamada para função que irá preencher os textos na tela, inclusive exibir o itemTempo
        //proximaQuestao();
        getQuestion(activityc_MenuPrincipal.usuario.getRespondidas());

    }

    private void instanciaElementosInterface() {

        if (! elementosInstanciados) {

            // Botões
            botoes = new ArrayList<>();
            botoes.add((Button) findViewById(R.id.btnA));
            botoes.add((Button) findViewById(R.id.btnB));
            botoes.add((Button) findViewById(R.id.btnC));
            botoes.add((Button) findViewById(R.id.btnD));

            //TextViews
            txtPergunta = (TextView) findViewById(R.id.txtPergunta);
            txtDificuldade = (TextView) findViewById(R.id.txtDificuldade);
            txtSecao = (TextView) findViewById(R.id.txtSecao);

            layoutPrincipal = (RelativeLayout) findViewById(R.id.layout_jogo);
            telaLoading = (RelativeLayout) findViewById(R.id.jogo_layout_carregando);

            pontuacaoPartida = 0;

            elementosInstanciados = true;

        }
    }

    private void setaElementosInterface() {

        if (! elementosSetados) {

            // Instanciando o click dos botões
            botoes.get(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tentativa(0);
                }
            });
            botoes.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tentativa(1);
                }
            });
            botoes.get(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tentativa(2);
                }
            });
            botoes.get(3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tentativa(3);
                }
            });

            Glide.with(this)
                    .load(R.drawable.other_loading)
                    .asGif()
                    .into((ImageView) findViewById(R.id.jogo_progresso));

            elementosSetados = true;

        }

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
                menuItem.setTitle(getString(R.string.jogo_bonus_referenciabiblica) + " (" + activityc_MenuPrincipal.usuario.getBonus().getBonusTexto() + ")");
                break;
            }
            case R.id.menu_bonus_eliminaalternativa:
            {
                menuItem.setTitle(getString(R.string.jogo_bonus_eliminaalternativa) + " (" + activityc_MenuPrincipal.usuario.getBonus().getBonusAlternativa() + ")");
                break;
            }
            case R.id.menu_bonus_maistempo:
            {
                menuItem.setTitle(getString(R.string.jogo_bonus_maistempo) + " (" + activityc_MenuPrincipal.usuario.getBonus().getBonusTempo() + ")");
                break;
            }
        }
    }

    private void maisTempo(){ // Função responsável por acrescentar mais itemTempo para responder
        if (activityc_MenuPrincipal.usuario.getBonus().getBonusTempo() > 0) {
            tempoRestante += Parameter.MAIS_TEMPO;

            activityc_MenuPrincipal.usuario.getBonus().setBonusTempo(-1);

            atualizaMenuBonus(menu.findItem(R.id.menu_bonus_maistempo));
        }else
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.bonus_insuficiente), " acrescentar mais itemTempo para responder"), Toast.LENGTH_SHORT).show();
    }

    private void ajuda(){ // Função responsável por eliminar uma resposta incorreta

        if (activityc_MenuPrincipal.usuario.getBonus().getBonusAlternativa() > 0) {
            if (alternativasEliminadas < 3) {

                Random random = new Random();
                int alternativaEliminada = random.nextInt(4);

                while (alternativaEliminada == question.getAnswer() || botoes.get(alternativaEliminada).getVisibility() == View.INVISIBLE) {
                    alternativaEliminada = random.nextInt(4);
                }

                botoes.get(alternativaEliminada).setVisibility(View.INVISIBLE);
                alternativasEliminadas++;

                activityc_MenuPrincipal.usuario.getBonus().setBonusAlternativa(-1);

                atualizaMenuBonus(menu.findItem(R.id.menu_bonus_eliminaalternativa));
            }
        }else
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.bonus_insuficiente), "eliminar uma alternativa incorreta"), Toast.LENGTH_SHORT).show();
    }

    private void exibeReferenciaBiblica(){

        if (activityc_MenuPrincipal.usuario.getBonus().getBonusTexto() > 0){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Referência bíblica");
            builder.setIcon(R.drawable.img_icon);
            builder.setMessage(question.getReferenciaBiblica());

            if(!isFinishing())
                builder.create().show();

            activityc_MenuPrincipal.usuario.getBonus().setBonusTexto(-1);

            atualizaMenuBonus(menu.findItem(R.id.menu_bonus_exibetextobiblico));

        }else
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.bonus_insuficiente), "exibir a referência bíblica"), Toast.LENGTH_SHORT).show();

    }

    private void tentativa(int alternativaUsuario){ // Função acionada no fim da questão ou quando o usuário seleciona a opção

        podeExibirNovaQuestao = false;
        getQuestion(activityc_MenuPrincipal.usuario.getRespondidas());

        // Variável local que exibirá a pontuação obtida com esta questão
        int pontuacaoTentativa = 0;

        // Para com a thread que decrementa o itemTempo
        executaThread = false;

        // Verifica se o usuário acertou
        boolean acertou = alternativaUsuario == question.getAnswer();

        // Cria um AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (acertou){
            activityc_MenuPrincipal.usuario.addAnswered(question.getIdQuestion());

            if (activityc_MenuPrincipal.usuario.getPreferencias().isSons()) {
                // Reproduz um som de correto
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.resposta_correta);
                mediaPlayer.start();
                mediaPlayer.setLooping(false);
            }

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

            if (activityc_MenuPrincipal.usuario.getPreferencias().isSons()) {
                // Reproduz um som de incorreto
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.resposta_incorreta);
                mediaPlayer.start();
                mediaPlayer.setLooping(false);
            }

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

        //Bloqueia os controles do usuário
        congelaTela(true);

        // Soma a pontuação obtida nesta tentativa à pontuação da partida
        pontuacaoPartida += pontuacaoTentativa;

        // Exibe a pontuação da partida no menu
        itemPontuacao.setTitle("Pontuação: " + String.valueOf(pontuacaoPartida));

    }

    private void proximaQuestao(){

        alternativasEliminadas = 0;
        for (int i = 0; i < botoes.size(); i++) {
            botoes.get(i).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quadro_alternativa));
        }

        tempoRestante = TEMPOTOTAL;

        podeExibirNovaQuestao = true;

    }

    public void congelaTela(final boolean bloqueio){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!bloqueio && !podeExibirNovaQuestao) {
                    try {
                        Thread.sleep(100);
                        Log.d(getClass().toString(), "Ainda não pode exibir a nova questão");
                    } catch (Exception e) {
                        Log.d(getClass().toString(), e.getMessage().toString());
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<View> views = new ArrayList<>();
                        views.add(txtPergunta);
                        views.add(txtSecao);
                        views.add(txtDificuldade);
                        views.add(botoes.get(0));
                        views.add(botoes.get(1));
                        views.add(botoes.get(2));
                        views.add(botoes.get(3));

                        for (int i = 0; i < views.size(); i++) {
                            views.get(i).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), (bloqueio ? android.R.anim.slide_out_right : android.R.anim.slide_in_left)));
                            views.get(i).setClickable(!bloqueio);
                            views.get(i).setVisibility(bloqueio ? View.INVISIBLE : View.VISIBLE);

                        }

                        for (int i = 0; i < menu.size(); i++) {
                            menu.getItem(i).setEnabled(!bloqueio);
                        }

                        //Coloca/Retira tela de Loading e faz as devidas animações
                        if (bloqueio){
                            if (layoutPrincipal.getVisibility() == View.VISIBLE) {
                                layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                layoutPrincipal.setVisibility(View.INVISIBLE);
                            }

                            telaLoading.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                            telaLoading.setVisibility(View.VISIBLE);

                        }else{
                            if (telaLoading.getVisibility() == View.VISIBLE) {
                                telaLoading.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                telaLoading.setVisibility(View.INVISIBLE);
                            }

                            layoutPrincipal.setVisibility(View.VISIBLE);

                        }

                        if(!bloqueio)
                            executaThread = true;

                    }
                });
            }
        }).start();

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
                    txtSecao.setText(   question.getSecaoBiblia() +
                                        " (" + question.getTestamento().replace("Antigo", "A.T.").replace("Novo", "N. T.") + ") ".replace(" N. T(", "("));

                    switch (question.getLevelQuestion()){
                        case 1:{
                            txtDificuldade.setText(getString(R.string.dificuldades_facil));
                            break;
                        }
                        case 2:{
                            txtDificuldade.setText(getString(R.string.dificuldades_medio));
                            break;
                        }
                        case 3:{
                            txtDificuldade.setText(getString(R.string.dificuldades_dificil));
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
                    congelaTela(false);

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

        instanciaElementosInterface();
        setaElementosInterface();
        congelaTela(true);

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

        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));

        super.onStop();

        activityc_MenuPrincipal.usuario.setPontuacao(activityc_MenuPrincipal.usuario.getPontuacao() + pontuacaoPartida);
        FirebaseDB.getUsuarioReferencia().child(activityc_MenuPrincipal.usuario.getUid()).setValue(activityc_MenuPrincipal.usuario);

        executaThread = false;

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        layoutPrincipal.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));

    }
}