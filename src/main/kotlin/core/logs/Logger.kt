package core.logs

object Logger {

    private val isLogsEnabled: Boolean = true

    fun i(msg: String, vararg scopes: LogScope) {
        if (!isLogsEnabled || !isScopesEnabled(*scopes)) {
            return
        }

        println(withPrefix(scopes, msg))
    }

    fun e(msg: String, throwable: Throwable, vararg scopes: LogScope) {
        if (!isLogsEnabled || !isScopesEnabled(*scopes)) {
            return
        }

        println(withPrefix(scopes, msg))
        throwable.printStackTrace()
    }


    //==============================================================================================
    // *** Utils ***
    //==============================================================================================
    private fun withPrefix(scopes: Array<out LogScope>, msg: String) = "[${scopesToPrefix(*scopes)}] $msg"

    private fun isScopesEnabled(vararg scopes: LogScope): Boolean {
        for (item in scopes) {
            if (item.isEnabled) {
                return true
            }
        }

        return false
    }

    private fun scopesToPrefix(vararg scopes: LogScope): String {
        val sb = StringBuilder()

        for (item in scopes) {
            if (item.isEnabled) {
                sb.append(if (sb.isEmpty()) item.tag else "." + item.tag)
            }
        }

        return sb.toString()
    }

}