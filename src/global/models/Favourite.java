package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 28.10.2016.
 */
public class Favourite {

    @Expose @SerializedName("path") private String path;
    @Expose @SerializedName("order") private int order;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
