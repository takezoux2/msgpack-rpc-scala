package org.msgpack.scalarpc

import org.msgpack.rpc.loop.EventLoop
import org.msgpack.rpc.reflect.Reflect
import org.msgpack.rpc.Client
import org.msgpack.{MessagePack, ScalaMessagePack}
import reflect.{ScalapProxyBuilder, ScalapInvokerBuilder}
import org.msgpack.rpc.config.TcpClientConfig

/**
 *
 * User: takeshita
 * Create: 12/06/15 15:49
 */

class ScalaClient(client : Client) {


  def proxy[T](implicit m : Manifest[T]) = {
    client.proxy(m.erasure.asInstanceOf[Class[T]])
  }

  def call( method : String, args : AnyRef*) = {
    client.callApply(method,args.toArray)
  }

  def callAsync(method : String, args : AnyRef*) = {
    client.callAsyncApply(method,args.toArray)
  }


  def close() = {
    client.close()
    client.getEventLoop.shutdown()
  }

}

object ScalaClient{

  def apply() : ScalaClientBuilder[Nothing] = {
    new ScalaClientBuilder[Nothing](new SClientConfig[Nothing]("",0,EventLoop.start(ScalaMessagePack.messagePack),new TcpClientConfig()))
  }

  def apply(host : String ,port : Int) : ScalaClient = {
    apply().connect(host,port)
  }
}
