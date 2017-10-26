package kasper.android.cross_word_admin.models;

import java.io.Serializable;

public class WordInfo implements Serializable {

    private String question;
    private String answer;
    private int[] answerIndex;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int[] getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int[] answerIndex) {
        this.answerIndex = answerIndex;
    }
}