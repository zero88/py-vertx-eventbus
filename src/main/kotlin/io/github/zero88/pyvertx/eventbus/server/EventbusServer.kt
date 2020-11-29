package io.github.zero88.pyvertx.eventbus.server

import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.json.JsonObject
import io.vertx.ext.bridge.BridgeOptions
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge

fun main(args: Array<String>) {
  val vertx = Vertx.vertx()
  val bridge = TcpEventBusBridge.create(
    vertx,
    BridgeOptions()
      .addInboundPermitted(PermittedOptions().setAddress("welcome"))
      .addOutboundPermitted(PermittedOptions().setAddress("welcome"))
  )
  bridge.listen(
    7000
  ) { res: AsyncResult<TcpEventBusBridge?> ->
    if (res.succeeded()) {
      println("Started")
    } else {
      println("failed")
    }
  }

  val eb: EventBus = vertx.eventBus()

  val consumer: MessageConsumer<JsonObject> = eb.consumer("welcome") { message ->
    System.out.println("Message body: " + message.body())
    val jsonString = "{\"msg\":\"welcome\"}"
    val `object` = JsonObject(jsonString)
    message.reply(`object`)
  }
}
