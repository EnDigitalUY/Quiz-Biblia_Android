package quizbiblico.com.claudinei.quizbiblico;

public class Question {

    private int idQuestion;
    private String question;
    private int answer; /*0 - Alternativa A | 1 - Alternativa B | 2 - Alternativa C | 3 - Alternativa D*/
    private String alternative_A, alternative_B, alternative_C, alternative_D;
    private String textBiblical;
    private int levelQuestion;
    private String testamento; /* Antigo | Novo */
    private String secaoBiblia; /* Pentateuco | História 1 | Poesia | Profetas Maiores | Profetas Menores | Evangelhos | História 2 | Cartas | Profecia */
    private String referenciaBiblica;

    public Question(){

    }

    public Question(String question, int answer, String alternative_A, String alternative_B, String alternative_C, String alternative_D, String textBiblical, int levelQuestion, String testamento, String secaoBiblia ) {
        this.question = question;
        this.answer = answer;
        this.alternative_A = alternative_A;
        this.alternative_B = alternative_B;
        this.alternative_C = alternative_C;
        this.alternative_D = alternative_D;
        this.textBiblical = textBiblical;
        this.levelQuestion = levelQuestion;
        this.testamento = testamento;
        this.secaoBiblia = secaoBiblia;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getSecaoBiblia() {
        return secaoBiblia;
    }

    public void setSecaoBiblia(String secaoBiblia) {
        this.secaoBiblia = secaoBiblia;
    }

    public String getTestamento() {
        return testamento;
    }

    public void setTestamento(String testamento) {
        this.testamento = testamento;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getAlternative_A() {
        return alternative_A;
    }

    public void setAlternative_A(String alternative_A) {
        this.alternative_A = alternative_A;
    }

    public String getAlternative_B() {
        return alternative_B;
    }

    public void setAlternative_B(String alternative_B) {
        this.alternative_B = alternative_B;
    }

    public String getAlternative_C() {
        return alternative_C;
    }

    public void setAlternative_C(String alternative_C) {
        this.alternative_C = alternative_C;
    }

    public String getAlternative_D() {
        return alternative_D;
    }

    public void setAlternative_D(String alternative_D) {
        this.alternative_D = alternative_D;
    }

    public String getTextBiblical() {
        return textBiblical;
    }

    public void setTextBiblical(String textBiblical) {
        this.textBiblical = textBiblical;
    }

    public int getLevelQuestion() {
        return levelQuestion;
    }

    public void setLevelQuestion(int levelQuestion) {
        this.levelQuestion = levelQuestion;
    }

    public String getReferenciaBiblica() {
        return referenciaBiblica;
    }

    public void setReferenciaBiblica(String referenciaBiblica) {
        this.referenciaBiblica = referenciaBiblica;
    }
}
