package cn.daniellee.plugin.se.model;

public class GemInfo {

    private String type;

    private int level;

    public GemInfo() {}

    public GemInfo(String type, int level) {
        this.type = type;
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
