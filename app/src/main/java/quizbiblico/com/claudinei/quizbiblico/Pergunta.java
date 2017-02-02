package quizbiblico.com.claudinei.quizbiblico;

public class Pergunta {

    private int idQuestao;
    private String questao;
    private char resposta;
    private String alternativa_A, alternativa_B, alternativa_C, alternativa_D;
    private String textoBiblico;
    private int nivelQuestao;

    public Pergunta(){

    }

    public Pergunta(int idQuestao, String questao, char resposta, String alternativa_A, String alternativa_B, String alternativa_C, String alternativa_D, String textoBiblico, int nivelQuestao) {
        this.idQuestao = idQuestao;
        this.questao = questao;
        this.resposta = resposta;
        this.alternativa_A = alternativa_A;
        this.alternativa_B = alternativa_B;
        this.alternativa_C = alternativa_C;
        this.alternativa_D = alternativa_D;
        this.textoBiblico = textoBiblico;
        this.nivelQuestao = nivelQuestao;
    }

    public int getNivelQuestao() {
        return nivelQuestao;
    }

    public void setNivelQuestao(int nivelQuestao) {
        this.nivelQuestao = nivelQuestao;
    }

    public int getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(int idQuestao) {
        this.idQuestao = idQuestao;
    }

    public String getQuestao() {
        return questao;
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public char getResposta() {
        return resposta;
    }

    public void setResposta(char resposta) {
        this.resposta = resposta;
    }

    public String getAlternativa_A() {
        return alternativa_A;
    }

    public void setAlternativa_A(String alternativa_A) {
        this.alternativa_A = alternativa_A;
    }

    public String getAlternativa_B() {
        return alternativa_B;
    }

    public void setAlternativa_B(String alternativa_B) {
        this.alternativa_B = alternativa_B;
    }

    public String getAlternativa_C() {
        return alternativa_C;
    }

    public void setAlternativa_C(String alternativa_C) {
        this.alternativa_C = alternativa_C;
    }

    public String getAlternativa_D() {
        return alternativa_D;
    }

    public void setAlternativa_D(String alternativa_D) {
        this.alternativa_D = alternativa_D;
    }

    public String getTextoBiblico() {
        return textoBiblico;
    }

    public void setTextoBiblico(String textoBiblico) {
        this.textoBiblico = textoBiblico;
    }
}
