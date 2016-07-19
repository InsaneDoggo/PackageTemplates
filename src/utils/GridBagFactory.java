package utils;

import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBInsets;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created by CeH9 on 10.07.2016.
 */
public class GridBagFactory {

    @NotNull
    private static Insets getDefaultInsets() {
        return new JBInsets(4, 0, 4, 0);
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
                .setDefaultInsets(new JBInsets(4, 6, 4, 6))
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

    public static GridBag getBagForConfigureDialog() {
        return new GridBag()
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultWeightX(1);
    }

    public static GridBag getBagForGroovyDialog() {
        return new GridBag()
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultInsets(4,4,4,4)
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
                .setDefaultWeightX(0,0)
                .setDefaultWeightX(1,1)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }
}
