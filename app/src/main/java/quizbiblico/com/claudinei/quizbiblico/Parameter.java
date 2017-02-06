package quizbiblico.com.claudinei.quizbiblico;

/**
 * Created by Claudinei on 03/02/2017.
 */

public final class Parameter {

    private static int nextQuestionNum = 20;

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
}
