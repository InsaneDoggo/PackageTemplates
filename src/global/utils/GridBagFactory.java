package global.utils;

import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBInsets;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created by CeH9 on 10.07.2016.
 */
public class GridBagFactory {

    private static Insets defaultInsets = new JBInsets(4, 0, 4, 0);

    public static GridBag getBagForPackageTemplate() {
        return new GridBag()
                .setDefaultInsets(defaultInsets)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultWeightX(0, 0)
                .setDefaultWeightX(1, 1);
    }


    public static GridBag getBagForDirectory() {
        return new GridBag()
                .setDefaultWeightX(0, 0)
                .setDefaultWeightX(1, 1)
                .setDefaultInsets(defaultInsets)
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
                .setDefaultInsets(new JBInsets(4, 4, 4, 4))
                .setDefaultWeightX(1);
    }

    @NotNull
    public static GridBag getGridBagForImpexDialog() {
        return new GridBag()
                .setDefaultWeightX(1, 1)
                .setDefaultInsets(defaultInsets)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

    public static GridBag getOptionsPanelGridBag() {
        return new GridBag()
                .setDefaultWeightX(0, 0)
                .setDefaultWeightX(1, 0)
                .setDefaultWeightX(2, 1)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

}
