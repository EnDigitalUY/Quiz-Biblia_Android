package quizbiblico.com.claudinei.quizbiblico;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class QuestionDAO {
    private DatabaseReference questionReference = FirebaseDB.getDatabaseReference().child("question");

    private ArrayList<Question> questions = new ArrayList<Question>();

    private ValueEventListener questionListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren() ){
                Question question = data.getValue(Question.class);
                questions.add(question);
                Log.d("onDataChange", "Comecei aqui");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private Question returnedQuestion;

    public Question addQuestion (Question question){
        Question newQuestion = question;
        newQuestion.setIdQuestion(Parameter.getNextQuestionNum());

        questionReference.child(String.valueOf(question.getIdQuestion())).setValue(question);
        Parameter.setNextQuestionNumPP();

        return newQuestion;

    }
    public Question getAleatoryQuestion(){

        return returnedQuestion;
    }

    public ArrayList<Question> getQuestion(){
        return questions;
    }

    public ValueEventListener getQuestionListener() {
        return questionListener;
    }

    public DatabaseReference getQuestionReference() {
        return questionReference;
    }
}