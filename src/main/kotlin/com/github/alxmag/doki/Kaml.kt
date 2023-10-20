package com.github.alxmag.doki

import com.charleskorn.kaml.SingleLineStringStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration

object Kaml {
    private val defaultConfiguration = YamlConfiguration(
        encodeDefaults = false,
        singleLineStringStyle = SingleLineStringStyle.PlainExceptAmbiguous
    )

    val DefaultKaml = Yaml(configuration = defaultConfiguration)
}
