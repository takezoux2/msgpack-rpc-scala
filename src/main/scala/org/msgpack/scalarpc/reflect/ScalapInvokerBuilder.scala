package org.msgpack.scalarpc.reflect

import org.msgpack.rpc.reflect.InvokerBuilder.ArgumentEntry
import org.msgpack.scalautil.ScalaSigUtil
import org.msgpack.scalarpc.builder.{MethodSignatureRepository, MethodSigUtil}
import java.lang.reflect.{Modifier, Method}
import org.msgpack.rpc.Request
import org.msgpack.rpc.reflect.{ReflectionInvokerBuilder, Invoker, InvokerBuilder}
import org.msgpack.template.{FieldOption, Template}
import org.msgpack.MessagePack

/**
 *
 * User: takeshita
 * Create: 12/06/15 15:49
 */

class ScalapInvokerBuilder(messagePack : MessagePack) extends ReflectionInvokerBuilder(messagePack) {


  override def buildInvoker(method : Method, args: Array[ArgumentEntry], async: Boolean): Invoker = {
    val methods = MethodSignatureRepository(method.getDeclaringClass)

    methods.get(method.getName) match{
      case Some( ms) => {
        val newArgs = ms.args.zipWithIndex.map({
          case (t , index) => {
            new ArgumentEntry(index,t,args(index).getOption)
          }
        })
        super.buildInvoker(method,newArgs.toArray,async)
      }
      case None => {
        super.buildInvoker(method,args,async)
      }
    }

  }
}