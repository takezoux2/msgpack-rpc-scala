package org.msgpack.scalarpc

import org.msgpack.rpc.loop.EventLoop
import org.msgpack.rpc.reflect.Reflect
import org.msgpack.rpc.Client
import org.msgpack.{MessagePack, ScalaMessagePack}
import reflect.{ScalapProxyBuilder, ScalapInvokerBuilder}

/**
 *
 * User: takeshita
 * Create: 12/06/15 15:49
 */

class ScalaClient(host : String,port : Int, loop : EventLoop,reflect : Reflect) {

  def this(host : String,port : Int , msgpack : MessagePack) = {
    this(host,port,EventLoop.start(msgpack),new Reflect(
      new ScalapInvokerBuilder(msgpack),
      new ScalapProxyBuilder(msgpack)
    ))
  }

  val client = new Client(host,port,loop,reflect)

  def withTimeoutSecs( secs : Int) = {
    client.setRequestTimeout(secs)
    this
  }

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

  def apply(host : String ,port : Int) = {
    val mp = ScalaMessagePack.messagePack
    new ScalaClient(host,port,mp)
  }
}
