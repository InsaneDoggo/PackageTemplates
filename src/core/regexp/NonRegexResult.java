package core.regexp;

import java.util.regex.MatchResult;

/**
 * Created by Arsen on 17.03.2017.
 */
public class NonRegexResult implements MatchResult {

    private int start;
    private int end;
    private String group;

    @Override
    public int start() {
        return start;
    }

    @Override
    public int end() {
        return end;
    }

    @Override
    public String group() {
        return group;
    }


    //=================================================================
    //  G|S
    //=================================================================
    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setGroup(String group) {
        this.group = group;
    }


    //=================================================================
    //  Unused
    //=================================================================
    @Override
    public int start(int group) {
        return 0;
    }

    @Override
    public int end(int group) {
        return 0;
    }

    @Override
    public String group(int group) {
        return null;
    }

    @Override
    public int groupCount() {
        return 0;
    }
}
