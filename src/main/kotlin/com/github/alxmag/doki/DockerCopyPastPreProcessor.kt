package com.github.alxmag.doki

import com.intellij.codeInsight.editorActions.CopyPastePreProcessor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.RawText
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.parentOfType
import com.intellij.util.asSafely
import org.jetbrains.yaml.YAMLElementGenerator
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLMapping

class DockerCopyPastPreProcessor : CopyPastePreProcessor {

    override fun preprocessOnCopy(
        file: PsiFile?,
        startOffsets: IntArray?,
        endOffsets: IntArray?,
        text: String?
    ): String? = null

    override fun preprocessOnPaste(
        project: Project,
        file: PsiFile,
        editor: Editor,
        text: String,
        rawText: RawText?
    ): String {
        if (file !is YAMLFile) return text

        val args = text
            .trim()
            .replace("\\\n", " ")
            .split(Regex("\\s+"))

        val command = DockerApiCommand()
        command.runCatching {
            parse(args)
        }.onFailure {
            return text
        }

        val document = PsiUtilCore.getElementAtOffset(file, editor.caretModel.offset)
            .parentOfType<YAMLDocument>()
            ?: file.documents.first()
        val myModel = buildDocument(project, command.dockerRunCommand)

        WriteCommandAction.runWriteCommandAction(project) {
            merge(document, myModel)
        }
        return ""
    }

    private fun buildDocument(project: Project, command: DockerRunCommand): YAMLDocument {
        val yamlText = CommandToYamlMapper(command).getText()
        return YAMLElementGenerator.getInstance(project)
            .createDummyYamlWithText(yamlText)
            .firstDocument
    }

    private fun merge(
        document: YAMLDocument,
        myModel: YAMLDocument
    ) {
        val rootMapping = document.rootMapping

        val modelRootMapping = myModel.rootMapping!!
        val modelServicesKeyValue = modelRootMapping.getKeyValueByKey("services")!!
        val modelServicesValue = modelServicesKeyValue.value as YAMLMapping

        if (rootMapping == null) {
            document.add(modelRootMapping)
            return
        }

        val servicesKeyValue = rootMapping.getKeyValueByKey("services")
        if (servicesKeyValue == null) {
            rootMapping.putKeyValue(modelServicesKeyValue)
            return
        }

        val servicesMapping = servicesKeyValue.value?.asSafely<YAMLMapping>()
        if (servicesMapping == null) {
            servicesKeyValue.setValue(modelServicesValue)
            return
        }

        servicesMapping.putKeyValue(modelServicesValue.getKeyValueByKey("postgres")!!)
        return
    }
}

val YAMLFile.firstDocument: YAMLDocument
    get() = this.documents[0]

val YAMLDocument.rootMapping
    get() = this.childrenOfType<YAMLMapping>()
        .firstOrNull()