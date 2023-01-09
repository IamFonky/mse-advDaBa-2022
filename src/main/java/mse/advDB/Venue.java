package mse.advDB;

public class Venue {
    private String id;
    private int type;
    private String raw;
    private String rawZh;

    // getters and setters for each field
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getRawZh() {
        return rawZh;
    }

    public void setRawZh(String rawZh) {
        this.rawZh = rawZh;
    }
}
