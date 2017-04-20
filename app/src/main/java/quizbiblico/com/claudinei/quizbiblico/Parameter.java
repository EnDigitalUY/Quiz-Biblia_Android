package quizbiblico.com.claudinei.quizbiblico;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Claudinei on 03/02/2017.
 */

public final class Parameter {

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

}