package com.github.alxmag.doki

import com.intellij.codeInsight.editorActions.CopyPastePreProcessor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.RawText
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
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

        val document = file.getDocumentAtOffset(editor.caretModel.offset)
        val myModel = buildDocument(project, command.dockerRunCommand)

        WriteCommandAction.runWriteCommandAction(project) {
            merge(document, myModel)
        }
        return ""
    }

    private fun buildDocument(project: Project, command: DockerRunCommand): YAMLDocument {
        val yamlText = command.toDocumentModel()
            .encodeToString()
        return YAMLElementGenerator.getInstance(project)
            .createDummyYamlWithText(yamlText)
            .firstDocument
    }

    private fun merge(
        document: YAMLDocument,
        other: YAMLDocument
    ) {
        val rootMapping = document.rootMapping

        val otherRootMapping = other.rootMapping!!
        val otherServicesKeyValue = otherRootMapping.getKeyValueByKey("services")!!
        val otherServicesMapping = otherServicesKeyValue.value as YAMLMapping

        if (rootMapping == null) {
            document.add(otherRootMapping)
            return
        }

        val servicesKeyValue = rootMapping.getKeyValueByKey("services")
        if (servicesKeyValue == null) {
            rootMapping.putKeyValue(otherServicesKeyValue)
            return
        }

        val servicesMapping = servicesKeyValue.value?.asSafely<YAMLMapping>()
        if (servicesMapping == null) {
            servicesKeyValue.setValue(otherServicesMapping)
            return
        }

        otherServicesMapping.keyValues.asSequence()
            .map { serviceKeyValue -> serviceKeyValue.withUniqueKeyForMapping(servicesMapping) }
            .forEach(servicesMapping::putKeyValue)
    }
}