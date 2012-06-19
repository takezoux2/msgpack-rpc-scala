package org.msgpack.scalarpc

import builder.ScalaDispatcherBuilder
import org.msgpack.rpc.loop.EventLoop
import org.msgpack.rpc.config.{TcpClientConfig, ClientConfig}
import org.msgpack.rpc.Server
import org.msgpack.ScalaMessagePack
import org.msgpack.rpc.builder.DispatcherBuilder

/**
 *
 * User: takeshita
 * Create: 12/06/15 15:48
 */

class ScalaServer(config : ClientConfig,loop : EventLoop) {


  val server = new Server(config,loop)

  server.setDispatcherBuilder(new ScalaDispatcherBuilder())


  def withServeObject( serverObj : AnyRef) = {
    server.serve(serverObj)
    this
  }

  def withModifyDispatcherBuilder( modify : DispatcherBuilder => DispatcherBuilder) = {
    server.setDispatcherBuilder( modify(server.getDispatcherBuilder))
    this
  }

  def listen( host : String, port : Int) = {
    server.listen(host,port)
    this
  }

  def listen( port : Int) = {
    server.listen(port)
    this
  }

  def close() = {
    server.close()
    server.getEventLoop.shutdown()
  }


}
object ScalaServer{

  def apply() = {
    val eventLoop = EventLoop.start(ScalaMessagePack.messagePack)
    new ScalaServer(new TcpClientConfig(),eventLoop)
  }

  implicit def anyToServer( obj : AnyRef) = {
    val s = apply()
    s.withServeObject(obj)
  }

}
