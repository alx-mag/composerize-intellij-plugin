package com.github.alxmag.doki

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language
import org.jetbrains.yaml.YAMLFileType
import java.awt.datatransfer.StringSelection

@TestDataPath("\$CONTENT_ROOT/testData/pasteCommand")
class PasteDockerCommandTest : BasePlatformTestCase() {
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

    fun testContainerName() = doTestPaste(
        "docker run --name some-postgres -e POSTGRES_PASSWORD_FILE=/run/secrets/postgres-passwd -d postgres",
        "",
        """
        services:
          some-postgres:
            image: postgres
            environment:
              - POSTGRES_PASSWORD_FILE=/run/secrets/postgres-passwd
            container_name: some-postgres
        """.trimIndent()
    )

    fun testRowWrapping() = doTestPaste(
        """
        docker run -d --name kafka-server --hostname kafka-server \
            --network app-tier \
            -e KAFKA_CFG_NODE_ID=0 \
            -e KAFKA_CFG_PROCESS_ROLES=controller,broker \
            -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
            -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
            -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server:9093 \
            -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
            bitnami/kafka:latest
        """.trimIndent(),
        "",
        """
        services:
          kafka-server:
            image: bitnami/kafka:latest
            hostname: kafka-server
            environment:
              - KAFKA_CFG_NODE_ID=0
              - KAFKA_CFG_PROCESS_ROLES=controller,broker
              - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093
              - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
              - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server:9093
              - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
            container_name: kafka-server
            network_mode: app-tier
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
