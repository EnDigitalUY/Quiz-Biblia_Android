package quizbiblico.com.claudinei.quizbiblico;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Random;

public final class QuestionDAO {
    // Referência para o banco de dados Firebase apontando para o nó de questões
    private static DatabaseReference questionReference = FirebaseDB.getDatabaseReference().child("question");

    //ArrayList de questões que conterá as questões vindas do banco de dados
    private static ArrayList<Question> questions = new ArrayList<Question>();

    //Listener que irá realizar a captura das questões
    private static ValueEventListener questionListener;

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
    public static Question getAleatoryQuestion(ArrayList<Integer> excludedQuestions){

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

        boolean randomOk = false;

        Random random = new Random();
        int randomizedQuestion = 0;

        while (randomOk == false){
            randomizedQuestion = random.nextInt(Parameter.getNextQuestionNum());

            if ((!excludedQuestions.contains(randomizedQuestion)) && (randomizedQuestion != 0)){
                randomOk = true;
            }
        }

        // Antes de tudo verifica se a base de dados já foi lida, caso sim, retorna as informações, caso não, ativa o Listener
        if (databaseReaded == true) {
            databaseReaded = false;
            return aleatoryQuestion;
        }
        else{
            questionListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren() ){
                        aleatoryQuestion = data.getValue(Question.class);
                        Log.d("onDataChange", "Question added");
                    }
                    Log.d("onDataChange", "All question added");
                    databaseReaded = true;

                    // Faz a remoção do listener de questões
                    questionReference.removeEventListener(questionListener);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }

        questionReference.orderByChild("idQuestion").equalTo(randomizedQuestion).addValueEventListener(questionListener);

        Log.d("Random", "ID: " + String.valueOf(randomizedQuestion));

        // Caso já tenha lido, retorna as questões, caso contrário, retorna nulo
        return ( databaseReaded == true ? aleatoryQuestion : null);
    }

    // Método que cria e já atribui o listener de questões e recebe as questões vindas do banco de dados
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
        }*/

        // Antes de tudo verifica se a base de dados já foi lida, caso sim, retorna as informações, caso não, ativa o Listener
        if (databaseReaded == true) {
            databaseReaded = false;
            return questions;
        }
        else{
            questionListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren() ){
                        Question question = data.getValue(Question.class);
                        questions.add(question);
                        Log.d("onDataChange", "Question added");
                    }
                    Log.d("onDataChange", "All question added");
                    databaseReaded = true;

                    // Faz a remoção do listener de questões
                    questionReference.removeEventListener(questionListener);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            questionReference.addValueEventListener(questionListener);

            // Caso já tenha lido, retorna as questões, caso contrário, retorna nulo
            return ( databaseReaded == true ? questions : null);
        }
    }
}