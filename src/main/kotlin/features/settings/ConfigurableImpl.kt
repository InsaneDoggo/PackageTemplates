package features.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import core.misc.Const
import net.miginfocom.layout.CC
import net.miginfocom.layout.LC
import net.miginfocom.swing.MigLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ConfigurableImpl(val project: Project?) : Configurable {

    override fun isModified(): Boolean {
        return false
    }

    override fun getDisplayName() = Const.PLUGIN_NAME

    override fun apply() {

    }


    //==================================================================================================================
    //  UI
    //==================================================================================================================
    lateinit var panel: JPanel

    override fun createComponent(): JComponent? {
        panel = JPanel(MigLayout(LC()))

        val tfHello = JBTextField("Hello World!")

        panel.add(tfHello, CC())
        return panel
    }
}