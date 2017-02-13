package quizbiblico.com.claudinei.quizbiblico;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Claudinei on 03/02/2017.
 */

public final class Parameter {

    private static int nextQuestionNum = 6;

    public Parameter(){

    }

    public static int getNextQuestionNum() {
        return nextQuestionNum;
    }

    public static void setNextQuestionNumPP() {
        Parameter.nextQuestionNum = getNextQuestionNum()+1;
    }
    public static void setNextQuestionNumMM() {
        Parameter.nextQuestionNum = getNextQuestionNum()-1;
    }

    public  boolean verificaConexao(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }
}
