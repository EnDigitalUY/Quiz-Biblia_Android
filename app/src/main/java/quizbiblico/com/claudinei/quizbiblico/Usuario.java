package quizbiblico.com.claudinei.quizbiblico;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String email;
    private String nome;
    private String senha;
    private String uid;
    private boolean keepConnected;

    public Usuario(){

    }

    public Usuario(String email, String nome, String senha, String uid, boolean keepConnected) {
        this.uid = uid;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.keepConnected = keepConnected;
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
