package org.msgpack.scalarpc

import builder.ScalaDispatcherBuilder
import org.msgpack.rpc.loop.EventLoop
import org.msgpack.rpc.config.ClientConfig
import org.msgpack.rpc.builder.{StopWatchDispatcherBuilder, DispatcherBuilder}
import org.msgpack.MessagePack
import org.msgpack.rpc.Server

/**
 *
 * User: takeshita
 * Create: 12/06/21 17:45
 */

case class ServerConfig[HasServerObj](loop : EventLoop,
                                      config : ClientConfig,
                                      modifyBuilder : Option[DispatcherBuilder => DispatcherBuilder] = None,
                                      serverObject : AnyRef = null)

object ServerConfig{
  sealed trait Yes

}

class ScalaServerBuilder[HasServerObj](config : ServerConfig[HasServerObj]){

  import ServerConfig._

  type Conf = ServerConfig[HasServerObj]
  type This = ScalaServerBuilder[HasServerObj]

  def withServeObject( serverObj : AnyRef) : ScalaServerBuilder[Yes] = {
    new ScalaServerBuilder[Yes](config.copy(serverObject = serverObj))
  }

  def withModifyDispatcherBuilder( modify : DispatcherBuilder => DispatcherBuilder) : This = {
    new ScalaServerBuilder[HasServerObj](config.copy(modifyBuilder = Some(modify)))
  }

  def withLoop( eventLoop : EventLoop): This = {
    new ScalaServerBuilder[HasServerObj](config.copy(loop = eventLoop))
  }

  def withMessagePack(msgPack : MessagePack) : This = {
    withLoop(EventLoop.start(msgPack))
  }

  def withClientConfig(config : ClientConfig) : This = {
    new ScalaServerBuilder[HasServerObj](this.config.copy(config = config))
  }

  /**
   * Modify dispatcher builder to add StopWatchDispatcher
   * @return
   */
  def withStopWatch() : This = {
    withModifyDispatcherBuilder( b => new StopWatchDispatcherBuilder(b))
  }

  protected def initServer() = {
    val server = new Server(config.config,config.loop)
    config.modifyBuilder match{
      case Some(modify) => {
        server.setDispatcherBuilder(modify(new ScalaDispatcherBuilder()))
      }
      case None => {
        server.setDispatcherBuilder(new ScalaDispatcherBuilder())
      }
    }
    server.serve(config.serverObject)
    server
  }

  def listen( host : String, port : Int)(implicit SET_SERVER_OBJECT : Conf =:= ServerConfig[Yes]) : ScalaServer = {
    val server = initServer()
    server.listen(host,port)
    new ScalaServer(server)
  }

  def listen( port : Int)(implicit SET_SERVER_OBJECT : Conf =:= ServerConfig[Yes]) : ScalaServer = {
    val server = initServer()
    server.listen(port)
    new ScalaServer(server)
  }
}
