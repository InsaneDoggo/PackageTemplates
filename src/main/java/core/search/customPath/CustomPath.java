package core.search.customPath;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.search.SearchAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Arsen on 11.09.2016.
 */
public class CustomPath {

    @Expose @SerializedName("listSearchAction") private ArrayList<SearchAction> listSearchAction;

    public CustomPath(@NotNull ArrayList<SearchAction> listSearchAction) {
        this.listSearchAction = listSearchAction;
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public ArrayList<SearchAction> getListSearchAction() {
        return listSearchAction;
    }

    public void setListSearchAction(ArrayList<SearchAction> listSearchAction) {
        this.listSearchAction = listSearchAction;
    }
}
