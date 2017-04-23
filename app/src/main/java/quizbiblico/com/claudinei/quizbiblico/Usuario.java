package quizbiblico.com.claudinei.quizbiblico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Usuario implements Serializable {

    private String email;
    private String nome;
    private String uid;
    private boolean manterConectado;
    private String linkImagem;
    private ArrayList<Integer> respondidas = new ArrayList<Integer>();
    private int pontuacao = 0;
    private int bonusTempo = 5;
    private int bonusAlternativa = 5;
    private int bonusTexto = 5;
    private Date ultimoAcesso = new Date();

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

    public String getLinkImagem() {
        return linkImagem;
    }

    public void setLinkImagem(String linkImagem) {
        this.linkImagem = linkImagem;
    }

    public ArrayList<Integer> getRespondidas() {
        return respondidas;
    }

    public void setRespondidas(ArrayList<Integer> respondidas) {
        this.respondidas = respondidas;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacaoAAcrescentar) {
        this.pontuacao += pontuacaoAAcrescentar;
    }

    public int getBonusTempo() {
        return bonusTempo;
    }

    public void setBonusTempo(int bonusTempoAAcrescentar) {
        this.bonusTempo += bonusTempoAAcrescentar;
    }

    public int getBonusAlternativa() {
        return bonusAlternativa;
    }

    public void setBonusAlternativa(int bonusAlternativaAAcrescentar) {
        this.bonusAlternativa += bonusAlternativaAAcrescentar;
    }

    public int getBonusTexto() {
        return bonusTexto;
    }

    public void setBonusTexto(int bonusTextoAAcrescentar) {
        this.bonusTexto += bonusTextoAAcrescentar;
    }

    public Date getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(Date ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }
}
