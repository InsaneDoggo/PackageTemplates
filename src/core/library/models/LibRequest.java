package core.library.models;

public class LibRequest<ResultType> {

    private Alias alias;
    private ResultType value;

    public LibRequest(Alias alias) {
        this.alias = alias;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public ResultType getValue() {
        return value;
    }

    public void setValue(ResultType value) {
        this.value = value;
    }

}
