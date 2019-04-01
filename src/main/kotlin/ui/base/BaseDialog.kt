package ui.base

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel

abstract class BaseDialog(
        val project: Project,
        canBeParent: Boolean = true,
        title: String = "Unknown",
        private var width: Int = 600,
        private var height: Int = 400,
        protected val root: JComponent = JPanel()
) : DialogWrapper(project, canBeParent) {

    init {
        super.init()
        this.title = title
    }

    abstract fun initUI()

    override fun getPreferredFocusedComponent() = root

    override fun createCenterPanel(): JComponent? {
        root.minimumSize = Dimension(width, height)
        initUI()

        return root
    }
}