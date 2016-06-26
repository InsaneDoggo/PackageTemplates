package io;

import com.intellij.openapi.components.PersistentStateComponent;

/**
 * Created by CeH9 on 26.06.2016.
 */
public class TemplateSaver implements PersistentStateComponent<TemplateSaver.State> {
    static class State {
        public String value;
    }

    State myState;

    public State getState() {
        return myState;
    }

    public void loadState(State state) {
        myState = state;
    }
}