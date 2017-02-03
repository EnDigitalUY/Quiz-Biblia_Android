package quizbiblico.com.claudinei.quizbiblico;

/**
 * Created by Claudinei on 03/02/2017.
 */

public final class Parametro {

    private static int nextQuestionNum = 1;

    public static int getNextQuestionNum() {
        return nextQuestionNum;
    }

    public static void setNextQuestionNumPP() {
        Parametro.nextQuestionNum = getNextQuestionNum()+1;
    }
    public static void setNextQuestionNumMM() {
        Parametro.nextQuestionNum = getNextQuestionNum()-1;
    }
}
