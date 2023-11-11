package com.github.alxmag.doki

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
data class DockerComposeDocumentModel(val services: Map<String, Service>) {
    fun asString() = Kaml.DefaultKaml.encodeToString(this)
}

@Suppress("PropertyName", "SpellCheckingInspection")
@Serializable
data class Service(
    val image: String,
    val extra_hosts: List<String> = emptyList(),
    val cap_add: List<String> = emptyList(),
    val cap_drop: List<String> = emptyList(),
    val cgroup_parent: String? = null,
    val devices: List<String> = emptyList(),
    val dns: List<String> = emptyList(),
    val dns_search: List<String> = emptyList(),
    val env_file: List<String> = emptyList(),
    val expose: List<String> = emptyList(),
    val hostname: String? = null,
    val labels: List<String> = emptyList(),
    val links: List<String> = emptyList(),
    val logging: Logging = Logging(),
    val entrypoint: List<String> = emptyList(),
    val environment: List<String> = emptyList(),
    val container_name: String? = null,
    val network_mode: String? = null,
    val pid: String? = null,
    val privileged: Boolean = false,
    val ports: List<String> = emptyList(),
    val read_only: Boolean = false,
    val restart: String? = null,
    val tmpfs: List<String> = emptyList(),
    // TODO what is?
    val ulimits: String? = null,
    val user: String? = null,
    val volumes: List<String> = emptyList(),
    val tty: Boolean = false,
    val stdin_open: Boolean = false,
    val command: String? = null
) {
    @Serializable
    data class Logging(
        val driver: String? = null,
        val options: List<String> = emptyList()
    )
}