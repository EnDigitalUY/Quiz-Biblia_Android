package quizbiblico.com.claudinei.quizbiblico;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Random;

import quizbiblico.com.claudinei.quizbiblico.FirebaseDB;
import quizbiblico.com.claudinei.quizbiblico.Parameter;
import quizbiblico.com.claudinei.quizbiblico.Question;

public final class QuestionDAO  {
    // Referência para o banco de dados Firebase apontando para o nó de questões
    private static DatabaseReference questionReference = FirebaseDB.getDatabaseReference().child("question");

    //Listener que irá realizar a captura das questões
    private static ValueEventListener questionListener;

    //ArrayList de questões que conterá as questões vindas do banco de dados
    private static ArrayList<Question> questions = null;
    private static boolean called = false;

    //Questão que será retornada
    private static Question aleatoryQuestion;

    // Variável que identifica se os dados já foram retornados do banco de dados
    private static boolean databaseReaded = false;

    // Método para adicionar questões a base de dados, e devolve a mesma questão agora com o devido ID
    public static Question addQuestion (Question question){
        Question newQuestion = question;
        newQuestion.setIdQuestion(Parameter.getNextQuestionNum());

        questionReference.child(String.valueOf(question.getIdQuestion())).setValue(question);
        Parameter.setNextQuestionNumPP();

        return newQuestion;

    }

    // Método que receberá uma questão aleatória que o usuário ainda não respondeu
    //public static Question getAleatoryQuestion(ArrayList<Integer> excludedQuestions){
    public static Question getAleatoryQuestion(){
        // Como recuperar uma questão aleatória
        /*
        ArrayList<Integer> answered = new ArrayList<>(); answered.add(1);answered.add(2);answered.add(3);answered.add(4);answered.add(5);answered.add(6);answered.add(7);answered.add(8);answered.add(9);answered.add(10);answered.add(11);answered.add(12);answered.add(13);answered.add(14);answered.add(15);answered.add(16);answered.add(17);answered.add(18);
        Question question = QuestionDAO.getAleatoryQuestion(answered);
        try {
            Log.d("Random", "Questão: " + String.valueOf(question.getIdQuestion()));
        } catch (Exception e){
            if (question == null)
                Toast.makeText(getApplicationContext(), "Tente novamente dentro de alguns instantes", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Erro: \n" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        */

        return aleatoryQuestion;
    }

    public static void getQuestions_aux(ArrayList<Integer> excludedQuestions){
        getQuestions_aux(2, excludedQuestions);
    }

    public static void getQuestions_aux(int modoDeRetorno){
        getQuestions_aux(modoDeRetorno, null);
    }

    public static void getQuestions_aux(int modoDeRetorno, ArrayList<Integer> excludedQuestions){
        /*
            modoDeRetorno
                1 - Todas as questões
                2 - Questão aleatória
        */

        if (modoDeRetorno == 1) {

            questions = new ArrayList<Question>();
            questionListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Question question = data.getValue(Question.class);
                        questions.add(question);
                    }

                    // Faz a remoção do listener de questões
                    questionReference.removeEventListener(questionListener);
                    databaseReaded = true;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            questionReference.addValueEventListener(questionListener);
        } else if (modoDeRetorno == 2){



        }
    }

    public static ArrayList<Question> getQuestions(){
        // Como recuperar todas as questões
        /*
        ArrayList<Question> questions = QuestionDAO.getQuestions();

        try {
            Toast.makeText(getApplicationContext(), "Existem " + questions.size() + " questões", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            if (questions == null)
                Toast.makeText(getApplicationContext(), "Tente novamente dentro de alguns instantes", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Erro: \n" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        */

        return questions;
    }
}