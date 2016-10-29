package global.utils;

import global.models.Favourite;

import java.util.List;

/**
 * Created by Arsen on 28.10.2016.
 */
public class CollectionHelper {

    public static Favourite getFavouriteByPath(List<Favourite> list, String path) {
        for (Favourite favourite : list) {
            if (favourite.getPath().equals(path)) {
                return favourite;
            }
        }

        return null;
    }

}
