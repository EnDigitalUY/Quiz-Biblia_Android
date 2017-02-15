package quizbiblico.com.claudinei.quizbiblico;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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

}