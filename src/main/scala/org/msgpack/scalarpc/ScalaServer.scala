package org.msgpack.scalarpc

import builder.ScalaDispatcherBuilder
import org.msgpack.rpc.loop.EventLoop
import org.msgpack.rpc.config.{TcpClientConfig, ClientConfig}
import org.msgpack.rpc.Server
import org.msgpack.{MessagePack, ScalaMessagePack}
import org.msgpack.rpc.builder.DispatcherBuilder

/**
 * Builder class
 * User: takeshita
 * Create: 12/06/15 15:48
 */

class ScalaServer(val server : Server) {

  def close() = {
    server.close()
    server.getEventLoop.shutdown()
  }
}

object ScalaServer{

  def apply() = {
    val eventLoop = EventLoop.start(ScalaMessagePack.messagePack)
    val config = new TcpClientConfig
    new ScalaServerBuilder(new ServerConfig(eventLoop,config))
  }

  implicit def anyToServer( obj : AnyRef) = {
    val s = apply()
    s.withServeObject(obj)
  }

}
