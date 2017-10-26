package kasper.android.cross_word_admin.models;

import java.io.Serializable;
import java.util.ArrayList;

public class GameLevel implements Serializable {
    private int id;
    private int number;
    private int prize;
    private int tableSize;
    private String[] tableData;
    private ArrayList<WordInfo> words;
    private boolean hasQuestion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public String[] getTableData() {
        return tableData;
    }

    public void setTableData(String[] tableData) {
        this.tableData = tableData;
    }

    public ArrayList<WordInfo> getWords() {
        return words;
    }

    public void setWords(ArrayList<WordInfo> words) {
        this.words = words;
    }

    public boolean getHasQuestion() {
        return hasQuestion;
    }

    public void setHasQuestion(boolean hasQuestion) {
        this.hasQuestion = hasQuestion;
    }

}