package core.library.ui;

import com.intellij.openapi.util.Pair;
import core.library.models.lib.BinaryFileLibModel;

public class LibraryPresenter {

    private LibraryDialog view;

    public LibraryPresenter(LibraryDialog view) {
        this.view = view;
    }


    //=================================================================
    //  BinaryFiles
    //=================================================================
    public void onBinaryFileSelected(Pair<String, BinaryFileLibModel> value) {
        view.setBinaryFileTabContent(value);
    }

}
