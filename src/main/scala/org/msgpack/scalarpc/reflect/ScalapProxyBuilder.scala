package org.msgpack.scalarpc.reflect

import org.msgpack.rpc.reflect.ProxyBuilder.MethodEntry
import org.msgpack.rpc.reflect.{Proxy, ReflectionProxyBuilder}
import org.msgpack.scalarpc.builder.{MethodSignatureRepository, MethodSigUtil}
import org.msgpack.rpc.reflect.InvokerBuilder.ArgumentEntry
import org.msgpack.MessagePack

/**
 *
 * User: takeshita
 * Create: 12/06/15 17:54
 */

class ScalapProxyBuilder(messagePack : MessagePack) extends ReflectionProxyBuilder(messagePack) {


  override def buildProxy[T](iface: Class[T], entries: Array[MethodEntry]): Proxy[T] = {
    val methods = MethodSignatureRepository(iface)
    val newEntries = entries.map( e => {
      methods.get(e.getRpcName) match{
        case Some(ms) => {
          new MethodEntry(e.getMethod,e.getRpcName,ms.returnType,e.isAsync,e.getArgumentEntries.zip(ms.args).map( {
            case (ae,a) => new ArgumentEntry(ae.getIndex,a,ae.getOption)
          }))

        }
        case None => {
          e
        }
      }
    })
    super.buildProxy(iface,newEntries)
  }
}
