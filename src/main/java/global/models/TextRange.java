package global.models;

/**
 * Created by CeH9 on 21.06.2016.
 */
public class TextRange {
    private int begin;
    private int end;

    public TextRange(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
