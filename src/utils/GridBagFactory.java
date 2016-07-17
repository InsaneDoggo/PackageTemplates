package utils;

import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created by CeH9 on 10.07.2016.
 */
public class GridBagFactory {

    @NotNull
    private static Insets getDefaultInsets() {
        return new Insets(4, 0, 4, 0);
    }

    public static GridBag getBagForPackageTemplate() {
        return new GridBag()
                .setDefaultInsets(getDefaultInsets())
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultWeightX(0, 0)
                .setDefaultWeightX(1, 1);
    }


    public static GridBag getBagForDirectory() {
        return new GridBag()
                .setDefaultWeightX(0,0)
                .setDefaultWeightX(1,1)
                .setDefaultInsets(getDefaultInsets())
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

    public static GridBag getBagForSelectDialog() {
        return new GridBag()
                .setDefaultInsets(getDefaultInsets())
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

    public static GridBag getBagForConfigureDialog() {
        return new GridBag()
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultWeightX(1);
    }

    @NotNull
    public static GridBag getDefaultGridBag() {
        return new GridBag()
                .setDefaultWeightX(1, 1)
                .setDefaultInsets(getDefaultInsets())
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

    @NotNull
    public static GridBag getGridBagForGlobals() {
        return new GridBag()
//                .setDefaultWeightX(0, 0.02)
                .setDefaultWeightX(1, 0.5)
                .setDefaultWeightX(2, 0.5)
                .setDefaultInsets(getDefaultInsets())
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

    public static GridBag getOptionsPanelGridBag() {
        return new GridBag()
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }
}
