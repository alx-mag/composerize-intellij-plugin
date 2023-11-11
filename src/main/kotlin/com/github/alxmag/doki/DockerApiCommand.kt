package com.github.alxmag.doki

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int

class DockerApiCommand : NoOpCliktCommand() {

    val dockerRunCommand = DockerRunCommand()
    @Suppress("MemberVisibilityCanBePrivate")
    val dockerCommand = DockerCommand().also {
        it.subcommands(dockerRunCommand)
    }

    init {
        subcommands(dockerCommand)
    }
}

class DockerCommand : NoOpCliktCommand(name = "docker")

@Suppress("unused", "PropertyName", "SpellCheckingInspection")
class DockerRunCommand : CliktCommand(name = "run", treatUnknownOptionsAsArgs = true) {

    val add_host by option("--add-host").multiple()
    val annotation by option("--annotation")
    val attach by option("--attach", "-a").multiple()
    val blkio_weight by option("--blkio-weight").int().default(0)
    val blkio_weight_device by option("--blkio-weight-device").multiple()
    val cap_add by option("--cap-add").multiple()
    val cap_drop by option("--cap-drop").multiple()
    val cgroup_parent by option("--cgroup-parent")
    val cgroupns by option("--cgroupns")
    val cidfile by option("--cidfile")
    val cpu_count by option("--cpu-count")
    val cpu_percent by option("--cpu-percent")
    val cpu_period by option("--cpu-period").int()
    val cpu_quota by option("--cpu-quota").int()
    val cpu_rt_period by option("--cpu-rt-period").int()
    val cpu_rt_runtime by option("--cpu-rt-runtime").int()
    val cpu_shares by option("--cpu-shares", "-c").int()
    val cpus by option("--cpus")
    val cpuset_cpus by option("--cpuset-cpus")
    val cpuset_mems by option("--cpuset-mems")
    val detach by option("--detach", "-d").flag()
    val detach_keys by option("--detach-keys")
    val device by option("--device").multiple()
    val device_cgroup_rule by option("--device-cgroup-rule").multiple()
    val device_read_bps by option("--device-read-bps").multiple()
    val device_read_iops by option("--device-read-iops").multiple()
    val device_write_bps by option("--device-write-bps").multiple()
    val device_write_iops by option("--device-write-iops").multiple()
    val disable_content_trust by option("--disable-content-trust").flag(default = true)
    val dns by option("--dns").multiple()
    val dns_opt by option("--dns-opt").multiple()
    val dns_option by option("--dns-option").multiple()
    val dns_search by option("--dns-search").multiple()
    val domainname by option("--domainname")
    val entrypoint by option("--entrypoint").multiple()
    val env by option("--env", "-e").multiple()
    val env_file by option("--env-file").multiple()
    val expose by option("--expose").multiple()
    val gpus by option("--gpus")
    val group_add by option("--group-add")
    val health_cmd by option("--health-cmd")
    val health_interval by option("--health-interval")
    val health_retries by option("--health-retries")
    val health_start_period by option("--health-start-period")
    val health_timeout by option("--health-timeout")
    val help by option("--help").flag()
    val hostname by option("--hostname", "-h")
    val init by option("--init").flag()
    val interactive by option("--interactive", "-i").flag()
    val io_maxbandwidth by option("--io-maxbandwidth")
    val io_maxiops by option("--io-maxiops")
    val ip by option("--ip")
    val ip6 by option("--ip6")
    val ipc by option("--ipc")
    val isolation by option("--isolation")
    val kernel_memory by option("--kernel-memory")
    val label by option("--label", "-l").multiple()
    val label_file by option("--label-file").multiple()
    val link by option("--link").multiple()
    val link_local_ip by option("--link-local-ip").multiple()
    val log_driver by option("--log-driver")
    val log_opt by option("--log-opt").multiple()
    val mac_address by option("--mac-address")
    val memory by option("--memory", "-m")
    val memory_reservation by option("--memory-reservation")
    val memory_swap by option("--memory-swap")
    val memory_swappiness by option("--memory-swappiness")
    val mount by option("--mount")
    val name by option("--name")
    val net by option("--net")
    val net_alias by option("--net-alias")
    val network by option("--network")
    val network_alias by option("--network-alias").multiple()
    val no_healthcheck by option("--no-healthcheck").flag()
    val oom_kill_disable by option("--oom-kill-disable").flag()
    val oom_score_adj by option("--oom-score-adj")
    val pid by option("--pid")
    val pids_limit by option("--pids-limit")
    val platform by option("--platform")
    val privileged by option("--privileged").flag()
    val publish by option("--publish", "-p").multiple()
    val publish_all by option("--publish-all", "-P").flag()
    val pull by option("--pull")
    val quiet by option("--quiet", "-q").flag()
    val read_only by option("--read-only").flag()
    val restart by option("--restart")
    val rm by option("--rm").flag()
    val runtime by option("--runtime")
    val security_opt by option("--security-opt").multiple()
    val shm_size by option("--shm-size")
    val sig_proxy by option("--sig-proxy").flag(default = true)
    val stop_signal by option("--stop-signal")
    val stop_timeout by option("--stop-timeout")
    val storage_opt by option("--storage-opt").multiple()
    val sysctl by option("--sysctl").multiple()
    val tmpfs by option("--tmpfs").multiple()
    val tty by option("--tty", "-t").flag()
    val ulimit by option("--ulimit")
    val user by option("--user", "-u")
    val userns by option("--userns")
    val uts by option("--uts")
    val volume by option("--volume", "-v").multiple()
    val volume_driver by option("--volume-driver")
    val volumes_from by option("--volumes-from").multiple()
    val workdir by option("--workdir", "-w")

    val image by argument("image")
    val startCommand: List<String> by argument().multiple()

    override fun run() = Unit
}