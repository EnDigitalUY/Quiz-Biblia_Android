package quizbiblico.com.claudinei.quizbiblico;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String email;
    private String nome;
    private String senha;
    private String uid;
    private boolean keepConnected;
    private int bonusTime;
    private int bonusQuestion;
    private ArrayList<Integer> answered = new ArrayList<Integer>();

    public Usuario(){

    }

    public ArrayList<Integer> getAnswered() {
        return answered;
    }

    public void setAnswered(ArrayList<Integer> answered) {
        this.answered = answered;
    }

    public Usuario(String email, String nome, String senha, String uid, boolean keepConnected) {
        this.uid = uid;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.keepConnected = keepConnected;

        this.bonusTime = 5;
        this.bonusQuestion = 5;

    }

    public boolean getKeepConnected() {
        return keepConnected;
    }

    public void setKeepConnected(boolean keepConnected) {
        this.keepConnected = keepConnected;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}
