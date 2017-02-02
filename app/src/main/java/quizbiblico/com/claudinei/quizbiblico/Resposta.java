package quizbiblico.com.claudinei.quizbiblico;

public class Resposta {

    private int idPergunta;
    private String username;
    private boolean resposta;

    public Resposta(){

    }

    public Resposta(int idPergunta, String username, boolean resposta) {
        this.idPergunta = idPergunta;
        this.username = username;
        this.resposta = resposta;
    }

    public int getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isResposta() {
        return resposta;
    }

    public void setResposta(boolean resposta) {
        this.resposta = resposta;
    }
}
