package com.github.alxmag.doki

import com.charleskorn.kaml.SingleLineStringStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration

fun DockerRunCommand.toDockerComposeDocumentModel(namer: ServiceNamer = ServiceNamer.DEFAULT): DockerComposeDocumentModel {
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
        volumes = volume,
        tty = tty,
        stdin_open = interactive,
        command = startCommand.takeIf { it.isNotEmpty() }
            ?.joinToString(" ")
    )

    val services = mapOf(namer.name(service) to service)
    return DockerComposeDocumentModel(services)
}

fun interface ServiceNamer {
    fun name(service: Service): String

    companion object {
        val DEFAULT: ServiceNamer = ServiceNamer { service ->
            sequence {
                yield(service.container_name)
                yield(service.image.substringAfterLast('/'))
                yield("service")
            }
                .filterNotNull()
                .filter { it.isNotBlank() }
                .first()
        }
    }
}

object Kaml {
    private val defaultConfiguration = YamlConfiguration(
        encodeDefaults = false,
        singleLineStringStyle = SingleLineStringStyle.PlainExceptAmbiguous
    )

    val DefaultKaml = Yaml(configuration = defaultConfiguration)
}
