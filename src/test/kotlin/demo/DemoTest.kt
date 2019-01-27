package demo

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class DemoTest : LightPlatformCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "testdata/demo"

    override fun setUp() {
        super.setUp()
    }

    fun testDemoHello() {
        myFixture.configureByFile("${input()}.txt")
        myFixture.type(" World!")
        myFixture.checkResultByFile("${output()}.txt")
    }

    fun testDemoFooBar() {
        myFixture.configureByFile("${input()}.txt")
        myFixture.type("Bar")
        myFixture.checkResultByFile("${output()}.txt")
    }

    override fun tearDown() {
        super.tearDown()
    }


    //==================================================================================================================
    //  Utils
    //==================================================================================================================
    private fun input() = "input/${getTestName(false)}"
    private fun output() = "output/${getTestName(false)}"
}