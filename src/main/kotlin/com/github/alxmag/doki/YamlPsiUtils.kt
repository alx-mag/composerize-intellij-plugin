package com.github.alxmag.doki

import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.parentOfType
import org.jetbrains.yaml.YAMLElementGenerator
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

val YAMLFile.firstDocument: YAMLDocument
    get() = this.documents[0]

val YAMLDocument.rootMapping
    get() = this.childrenOfType<YAMLMapping>()
        .firstOrNull()

fun YAMLFile.getDocumentAtOffset(offset: Int): YAMLDocument {
    return PsiUtilCore.getElementAtOffset(this, offset)
        .parentOfType<YAMLDocument>()
        ?: this.documents.first()
}

val YAMLMapping.usedKeys
    get() = keyValues.map { it.keyText }.toSet()

fun YAMLKeyValue.withUniqueKeyForMapping(yamlMapping: YAMLMapping): YAMLKeyValue {
    val uniqueKeyName = yamlMapping.createUniqueKeyName(this.keyText)
    return this.withKeyText(uniqueKeyName)
}

fun YAMLMapping.createUniqueKeyName(keyText: String): String {
    var index = 1
    var uniqueKeyText = keyText

    val usedKeys = this.usedKeys
    while (uniqueKeyText in usedKeys) {
        uniqueKeyText = keyText + index++
    }
    return uniqueKeyText
}

fun YAMLKeyValue.withKeyText(keyText: String): YAMLKeyValue {
    val newKeyPsiElement = YAMLElementGenerator.getInstance(project)
        .createYamlKeyValue(keyText, "Dummy")
        .key!!

    val copy = this.copy() as YAMLKeyValue
    copy.key!!.replace(newKeyPsiElement)
    return copy
}