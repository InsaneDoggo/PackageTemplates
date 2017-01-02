package global.utils.factories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import global.models.BaseElement;
import global.models.serialization.BaseElementConverter;

/**
 * Created by Arsen on 16.08.2016.
 */
public class GsonFactory {

    private static Gson instance;

    public static Gson getInstance() {
        if (instance == null) {
            instance = new GsonBuilder()
                    .registerTypeAdapter(BaseElement.class, new BaseElementConverter())
                    .excludeFieldsWithoutExposeAnnotation()
                    .setPrettyPrinting()
                    .create();
        }
        return instance;
    }

    public static <T> T cloneObject(T object, Class<T> type){
        return getInstance().fromJson(getInstance().toJson(object, type), type);
    }

}
