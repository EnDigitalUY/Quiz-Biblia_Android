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
    private int pontuacao;
    private Date ultimoAcesso = new Date();
    private Date primeiroAcesso = new Date();

    private Bonus bonus = new Bonus();
    private Preferencias preferencias = new Preferencias();

    public static class Bonus implements Serializable{
        private int bonusTempo;
        private int bonusAlternativa;
        private int bonusReferenciaBiblica;
        private Date ultimoBonusRecebido = new Date();

        public Bonus(){

        }

        public int getBonusTempo() {
            return this.bonusTempo;
        }

        public void setBonusTempo(int bonusTempo) {
            this.bonusTempo += bonusTempo;
        }

        public int getBonusAlternativa() {
            return this.bonusAlternativa;
        }

        public void setBonusAlternativa(int bonusAlternativa) {
            this.bonusAlternativa += bonusAlternativa;
        }

        public int getBonusReferenciaBiblica() {
            return this.bonusReferenciaBiblica;
        }

        public void setBonusReferenciaBiblica(int bonusReferenciaBiblica) {
            this.bonusReferenciaBiblica += bonusReferenciaBiblica;
        }

        public Date getUltimoBonusRecebido() {
            return ultimoBonusRecebido;
        }

        public void setUltimoBonusRecebido(Date ultimoBonusRecebido) {
            this.ultimoBonusRecebido = ultimoBonusRecebido;
        }
    }

    public static class Preferencias implements Serializable {
        private boolean sons;
        private boolean vibracao;

        public Preferencias(){

        }

        public boolean isSons() {
            return sons;
        }

        public void setSons(boolean sons) {
            this.sons = sons;
        }

        public boolean isVibracao() {
            return vibracao;
        }

        public void setVibracao(boolean vibracao) {
            this.vibracao = vibracao;
        }

    }

    public Usuario(){

    }

    public Usuario(String email, String nome, String uid, boolean manterConectado) {
        this.email = email;
        this.nome = nome;
        this.uid = uid;
        this.manterConectado = manterConectado;

        this.bonus = new Bonus();
        this.preferencias = new Preferencias();

    }

    public void addAnswered(Integer questionAnswered){
        respondidas.add(questionAnswered);
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

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Date getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(Date ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Date getPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void setPrimeiroAcesso(Date primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public Preferencias getPreferencias() {
        return preferencias;
    }

}
