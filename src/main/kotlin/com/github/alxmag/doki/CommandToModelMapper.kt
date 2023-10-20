package com.github.alxmag.doki

import com.github.alxmag.doki.Kaml.DefaultKaml
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

fun DockerRunCommand.toDocumentModel(namer: ServiceNamer = ServiceNamer.DEFAULT): YamlDocumentModel {
    val service = Service(
        image = image,
        extra_hosts = add_host,
        cap_add = cap_add,
        cap_drop = cap_drop,
        cgroup_parent = cgroup_parent,
        devices = device,
        dns = dns,
        dns_search = dns_search,
        env_file = env_file,
        expose = expose,
        hostname = hostname,
        labels = label,
        links = link,
        logging = Service.Logging(
            log_driver,
            log_opt
        ),
        entrypoint = entrypoint,
        environment = env,
        container_name = name,
        network_mode = network,
        pid = pid,
        privileged = privileged,
        ports = publish,
        read_only = read_only,
        restart = restart,
        tmpfs = tmpfs,
        ulimits = ulimit,
        user = user,
        volumes = volume
    )

    val services = mapOf(namer.name(service) to service)
    return YamlDocumentModel(services)
}

fun YamlDocumentModel.encodeToString() = DefaultKaml.encodeToString(this)

fun interface ServiceNamer {
    fun name(service: Service): String

    companion object {
        val DEFAULT: ServiceNamer = ServiceNamer { service ->
            service.image
                .takeIf { it.isNotBlank() }
                ?: "service"
        }
    }
}

@Serializable
data class YamlDocumentModel(val services: Map<String, Service>)

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
    val tmpfs: String? = null,
    // TODO what is?
    val ulimits: String? = null,
    val user: String? = null,
    val volumes: List<String> = emptyList()
) {
    @Serializable
    data class Logging(
        val driver: List<String> = emptyList(),
        val options: String? = null
    )
}

