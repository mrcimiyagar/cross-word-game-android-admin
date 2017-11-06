package kasper.android.cross_word_admin.models;

public class TourPlayer {

    private int id;
    private String name;
    private int levelsDoneCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevelsDoneCount() {
        return levelsDoneCount;
    }

    public void setLevelsDoneCount(int levelsDoneCount) {
        this.levelsDoneCount = levelsDoneCount;
    }
}