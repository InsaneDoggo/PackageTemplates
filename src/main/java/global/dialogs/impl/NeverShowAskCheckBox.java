package global.dialogs.impl;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Arsen on 06.02.2017.
 */
public class NeverShowAskCheckBox implements DialogWrapper.DoNotAskOption {
    @Override
    public boolean isToBeShown() {
        return false;
    }

    @Override
    public void setToBeShown(boolean toBeShown, int exitCode) {

    }

    @Override
    public boolean canBeHidden() {
        return false;
    }

    @Override
    public boolean shouldSaveOptionsOnCancel() {
        return false;
    }

    @NotNull
    @Override
    public String getDoNotShowMessage() {
        return "getDoNotShowMessage";
    }
}
