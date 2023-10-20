package com.github.alxmag.doki

import kotlinx.serialization.Serializable

class DockerCommandToModelMapper {
    fun map(dockerRunCommand: DockerRunCommand): Document {
        val service = with(dockerRunCommand) {
            Service(
                image = dockerRunCommand.image,
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
        }
        return Document(mapOf("my-service" to service))
    }
}

@Serializable
data class Document(val services: Map<String, Service>)

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
//    val logging/val driver: List<String>,
//    val logging/val options: String,
    val entrypoint: List<String> = emptyList(),
    val environment: List<String> = emptyList(),
    val container_name: String? = null,
    val network_mode: String? = null,
    val pid: String? = null,
    val privileged: Boolean? = null,
    val ports: List<String> = emptyList(),
    val read_only: Boolean? = null,
    val restart: String? = null,
    val tmpfs: String? = null,
    // TODO what is?
    val ulimits: String? = null,
    val user: String? = null,
    val volumes: List<String> = emptyList()
)