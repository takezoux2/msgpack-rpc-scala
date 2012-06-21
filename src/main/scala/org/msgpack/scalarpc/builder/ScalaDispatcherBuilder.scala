package org.msgpack.scalarpc.builder

import org.msgpack.rpc.builder.DispatcherBuilder
import org.msgpack.MessagePack
import org.msgpack.rpc.dispatcher.{MethodDispatcher, Dispatcher}
import org.msgpack.rpc.reflect.Reflect
import org.msgpack.scalarpc.reflect.{ScalapProxyBuilder, ScalapInvokerBuilder}

/**
 *
 * User: takeshita
 * Create: 12/06/15 15:56
 */

class ScalaDispatcherBuilder extends DispatcherBuilder {
  def build( handler : Any, messagePack : MessagePack): Dispatcher = {
    new MethodDispatcher(
      new Reflect(
        new ScalapInvokerBuilder(messagePack),
        new ScalapProxyBuilder(messagePack)
      ),handler)
  }
}
