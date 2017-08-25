package core.library.models.collections;

import core.library.models.LibRequest;

import java.util.HashMap;

public class LibCollection<K, V> extends HashMap<K, V> {

    public LibRequest get(LibRequest request) {
        request.setValue(get(request.getAlias().getKey()));
        return request;
    }

    public void put(LibRequest request) {
        put((K) request.getAlias().getKey(), (V) request.getValue());
    }

}
