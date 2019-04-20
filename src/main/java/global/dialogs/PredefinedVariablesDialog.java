package global.dialogs;

import base.BaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBScrollPane;
import global.utils.i18n.Localizer;
import global.wrappers.PackageTemplateWrapper;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * Created by Arsen on 19.07.2016.
 */
public class PredefinedVariablesDialog extends BaseDialog {

    private PackageTemplateWrapper ptWrapper;

    public PredefinedVariablesDialog(@Nullable Project project, PackageTemplateWrapper ptWrapper) {
        super(project);
        this.ptWrapper = ptWrapper;
    }


    //=================================================================
    //  UI
    //=================================================================
    @Override
    public void preShow() {
        setTitle(Localizer.get("title.AvailableVariables"));
        panel.setLayout(new MigLayout(new LC().gridGap("8pt", "4pt")));

        JLabel jlName = new JLabel(Localizer.get("label.Name"));
        JLabel jlExample = new JLabel(Localizer.get("label.Example"));
        panel.add(jlName, new CC().pushX().growX());
        panel.add(jlExample, new CC().pushX().growX().wrap());
        panel.add(new SeparatorComponent(10), new CC().pushX().growX().wrap().spanX());

        for (Map.Entry<String, String> entry : ptWrapper.getFakeProperties().entrySet()) {
            JTextField  key = new JTextField (entry.getKey());
            JTextField  value = new JTextField (entry.getValue());
            key.setEditable(false);
            value.setEditable(false);

            panel.add(key, new CC().pushX().growX());
            panel.add(value, new CC().pushX().growX().wrap());
        }

        pack();
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    @NotNull
    @Override
    protected Action[] createActions() {
        Action okAction = getOKAction();
        okAction.putValue(Action.NAME, Localizer.get("action.Close"));
        return new Action[]{okAction};
    }

    @Override
    protected JComponent createCenterPanel() {
        setModal(false);
        panel = new JPanel();
        JBScrollPane scrollPane = new JBScrollPane(panel);
        return scrollPane;
    }

}
