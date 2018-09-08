import com.intellij.openapi.components.ApplicationComponent

class AppComponent: ApplicationComponent {

    override fun getComponentName() = "PackageTemplatesAppComponent"


    //==================================================================================================================
    //  LifeCycle
    //==================================================================================================================
    override fun initComponent() {
        //todo init Dagger
    }

    override fun disposeComponent() {
    }
}