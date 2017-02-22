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

    private static int nextQuestionNum;
    public Parameter(){

    }

    public static int getNextQuestionNum() {
        return nextQuestionNum;
    }

    public static void getNexQuestionNum_Aux(){
        FirebaseDB.getParametrosReferencia().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    nextQuestionNum = data.getValue(Integer.class);
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