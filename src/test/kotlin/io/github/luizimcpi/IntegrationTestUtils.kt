package io.github.luizimcpi

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

fun startMongo(host: String, port: Int): MongodProcess {
    val starter = MongodStarter.getDefaultInstance()
    val mongodConfig: MongodConfig = MongodConfig.builder()
        .version(Version.Main.V5_0)
        .net(Net(host, port, Network.localhostIsIPv6()))
        .build()
    val mongodExecutable: MongodExecutable = starter.prepare(mongodConfig)
    return mongodExecutable.start()
}