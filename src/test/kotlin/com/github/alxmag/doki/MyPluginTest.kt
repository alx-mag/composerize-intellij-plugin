package com.github.alxmag.doki

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language
import org.jetbrains.yaml.YAMLFileType
import java.awt.datatransfer.StringSelection

@TestDataPath("\$CONTENT_ROOT/testData/pasteCommand")
class MyPluginTest : BasePlatformTestCase() {
    fun testEmpty() = doTestPaste(
        "docker run -v foo:bar postgres",

        """
        version: "3.8"
        <caret>
        """.trimIndent(),

        """
        version: "3.8"
        services:
          postgres:
            image: postgres
            volumes:
              - foo:bar
              
        """.trimIndent()
    )

    fun testDuplicated() = doTestPaste(
        "docker run -v bar:baz postgres",

        """
        version: "3.8"        
        <caret>services:
          postgres:
            image: postgres
            volumes:
              - foo:bar
              
        """.trimIndent(),

        """
        version: "3.8"
        services:
          postgres:
            image: postgres
            volumes:
              - foo:bar
          
          postgres1:
            image: postgres
            volumes:
              - bar:baz
        """.trimIndent()
    )

    private fun doTestPaste(
        command: String,
        @Language("yaml") before: String,
        @Language("yaml") after: String
    ) {
        myFixture.configureByText(YAMLFileType.YML, before)
        CopyPasteManager.getInstance().setContents(StringSelection(command))
        myFixture.performEditorAction(IdeActions.ACTION_EDITOR_PASTE)
        myFixture.checkResult(after, true)
    }

    override fun getTestDataPath() = "src/test/testData/pasteCommand"
}
