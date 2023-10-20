package com.github.alxmag.doki

import com.charleskorn.kaml.SingleLineStringStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.encodeToString

class CommandToYamlMapper(
    private val dockerRunCommand: DockerRunCommand,
    private val yaml: Yaml = defaultYaml
) {
    fun getText(): String {
        val document = DockerCommandToModelMapper().map(dockerRunCommand)
        return yaml.encodeToString(document)
    }

    companion object {
        private val defaultConfiguration = YamlConfiguration(
            encodeDefaults = false,
            singleLineStringStyle = SingleLineStringStyle.PlainExceptAmbiguous
        )
        private val defaultYaml = Yaml(configuration = defaultConfiguration)
    }
}
