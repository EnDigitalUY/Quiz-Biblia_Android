package quizbiblico.com.claudinei.quizbiblico;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String email;
    private String nome;
    private String uid;
    private boolean manterConectado;
    private int bonusTempo = 5;
    private int bonusQuestao = 5;
    private ArrayList<Integer> respondidas = new ArrayList<Integer>();

    public void addAnswered(Integer questionAnswered){
        respondidas.add(questionAnswered);
    }

    public Usuario(){

    }

    public Usuario(String email, String nome, String uid, boolean manterConectado) {
        this.email = email;
        this.nome = nome;
        this.uid = uid;
        this.manterConectado = manterConectado;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isManterConectado() {
        return manterConectado;
    }

    public void setManterConectado(boolean manterConectado) {
        this.manterConectado = manterConectado;
    }

    public int getBonusTempo() {
        return bonusTempo;
    }

    public void setBonusTempo(int bonusTempo) {
        this.bonusTempo = bonusTempo;
    }

    public int getBonusQuestao() {
        return bonusQuestao;
    }

    public void setBonusQuestao(int bonusQuestao) {
        this.bonusQuestao = bonusQuestao;
    }

    public ArrayList<Integer> getRespondidas() {
        return respondidas;
    }

    public void setRespondidas(ArrayList<Integer> respondidas) {
        this.respondidas = respondidas;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", uid='" + uid + '\'' +
                ", manterConectado=" + manterConectado +
                ", bonusTempo=" + bonusTempo +
                ", bonusQuestao=" + bonusQuestao +
                ", respondidas=" + respondidas +
                '}';
    }
}
