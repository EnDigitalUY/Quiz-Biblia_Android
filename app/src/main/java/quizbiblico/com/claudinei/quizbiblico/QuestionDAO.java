package quizbiblico.com.claudinei.quizbiblico;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.Random;

/**
 * Created by Claudinei on 03/02/2017.
 */

public final class QuestionDAO {

    private static DatabaseReference questionReference = FirebaseDB.getFirebaseReference().child("question");
    public static Question addQuestion (Question question){
        Question newQuestion = question;
        newQuestion.setIdQuestion(Parametro.getNextQuestionNum());

        questionReference.child(String.valueOf(question.getIdQuestion())).setValue(question);
        Parametro.setNextQuestionNumPP();

        return newQuestion;

    }
    public static Question getAleatoryQuestion(){
        return null;
    }
}
