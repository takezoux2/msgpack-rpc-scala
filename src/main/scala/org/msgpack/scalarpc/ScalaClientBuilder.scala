package org.msgpack.scalarpc

import org.msgpack.rpc.config.ClientConfig
import org.msgpack.rpc.loop.EventLoop
import org.msgpack.rpc.reflect.Reflect
import reflect.{ScalapProxyBuilder, ScalapInvokerBuilder}
import org.msgpack.rpc.Client
import org.msgpack.MessagePack
import java.net.{InetSocketAddress, InetAddress}

/**
 *
 * User: takeshita
 * Create: 12/06/21 17:48
 */

case class SClientConfig[HasHostAndPort](
                                         host : String,
                                         port : Int,
                                         loop : EventLoop,
                                         config : ClientConfig,
                                         testOnConnect : Option[String] = None
                                         )

object SClientConfig{
  sealed trait Yes
}

class ScalaClientBuilder[HasHostAndPort](config : SClientConfig[HasHostAndPort]) {

  type This = ScalaClientBuilder[HasHostAndPort]
  type Config = SClientConfig[HasHostAndPort]

  import SClientConfig._

  def withTimeout( timeoutSec : Int) : This = {
    config.config.setRequestTimeout(timeoutSec)
    new ScalaClientBuilder(config)
  }
  def withClientConfig(c : ClientConfig) : This = {
    new ScalaClientBuilder(config.copy(config = c))
  }
  def withEventLoop(loop : EventLoop) : This = {
    new ScalaClientBuilder(config.copy(loop = loop))
  }
  def withHost(host : String, port : Int) : ScalaClientBuilder[Yes] = {
    new ScalaClientBuilder[Yes](config.copy(host = host,port = port))
  }

  def withMessagePack(msgPack : MessagePack) : This = {
    withEventLoop(EventLoop.start(msgPack))
  }

  def testOnConnect( methodName : String) = {
    new ScalaClientBuilder[Yes](config.copy(testOnConnect = Some(methodName)))
  }

  def connect()(implicit HAS_HOST_AND_PORT : Config =:= SClientConfig[Yes]) : ScalaClient = {
    val msgpack = config.loop.getMessagePack
    val reflect = new Reflect(
      new ScalapInvokerBuilder(msgpack),
      new ScalapProxyBuilder(msgpack)
    )
    val inetAdd = new InetSocketAddress(config.host,config.port)
    val client = new Client(inetAdd,config.config,config.loop,reflect)
    config.testOnConnect match{
      case Some(methodName) => client.callApply(methodName,Array())
      case None =>
    }
    new ScalaClient(client)
  }

  def connect(host : String,port : Int) : ScalaClient = {
    withHost(host,port).connect()
  }

}
