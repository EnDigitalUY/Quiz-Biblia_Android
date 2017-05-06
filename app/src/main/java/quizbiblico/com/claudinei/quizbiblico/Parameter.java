package quizbiblico.com.claudinei.quizbiblico;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Claudinei on 03/02/2017.
 */

public final class Parameter {

    /*  A pontuação funcionará da seguinte maneira:

        Quanto ao tempo
            Acertou a questão entre 19 e 15 segundos restantes:     + 3 pontos
            Acertou a questão entre 14 e 10 segundos restantes:     + 2 pontos
            Acertou a questão entre 9 e 5 segundos restantes:       + 1 pontos
            Acertou a questão com menos de 5 segundos restantes:    + 0 pontos
            Erro a questão:                                         - 2 pontos

        Quanto à dificuldade
            Acertou difícil:    + 15 pontos
            Acertou médio:      + 10 pontos
            Acertou fácil:      + 5 pontos

            Errou difícil:      - 1 pontos
            Errou médio:        - 2 ponto
            Errou fácil:        - 3 pontos
*/


    /*Constantes*/
    public static final int PONTOS_TEMPO_19_15 = 3;
    public static final int PONTOS_TEMPO_14_10 = 2;
    public static final int PONTOS_TEMPO_9_5 = 1;
    public static final int PONTOS_TEMPO_4_0 = 0;

    public static final int PONTOS_ACERTO_DIFICIL = 15;
    public static final int PONTOS_ACERTO_MEDIO = 10;
    public static final int PONTOS_ACERTO_FACIL = 5;

    public static final int PONTOS_ERRO = -2;
    public static final int PONTOS_ERRO_DIFICIL = -1;
    public static final int PONTOS_ERRO_MEDIO = -2;
    public static final int PONTOS_ERRO_FACIL = -3;

    public static final int MAIS_TEMPO = 5;

    public static final int POWER_UP_INICIAL = 5;
    public static final int POWER_UP_DIARIO = 3;

    private static int nextQuestionNum = -1;
    private static boolean buscaConcluida = false;

    public Parameter(){

    }

    public static int getNextQuestionNum() {
        if (!buscaConcluida)
            getNexQuestionNum_Aux();

        return nextQuestionNum;
    }

    

    public static void getNexQuestionNum_Aux(){
        FirebaseDB.getParametrosReferencia().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    nextQuestionNum = data.getValue(Integer.class);
                    buscaConcluida = true;
                    //activityc_MenuPrincipal.parametrosCarreados = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void setNextQuestionNumPP() {
        Parameter.nextQuestionNum = getNextQuestionNum()+1;
    }
    public static void setNextQuestionNumMM() {
        Parameter.nextQuestionNum = getNextQuestionNum()-1;
    }

    public static boolean isBuscaConcluida() {
        return buscaConcluida;
    }
}