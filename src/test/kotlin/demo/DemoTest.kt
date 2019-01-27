package demo

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class DemoTest : LightPlatformCodeInsightFixtureTestCase() {

    override fun setUp() {
        super.setUp()
    }

    fun testMy() {
        myFixture.configureByText("DemoTest.txt", "Hello<caret>")
        myFixture.type(" World!")
        myFixture.checkResult("Hello World!")
    }

    override fun tearDown() {
        super.tearDown()
    }
}