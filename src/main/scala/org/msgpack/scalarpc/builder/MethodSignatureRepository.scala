package org.msgpack.scalarpc.builder

import collection.mutable
import scala.collection.JavaConversions._

/**
 *
 * User: takeshita
 * Create: 12/06/19 11:26
 */

object MethodSignatureRepository {

  private val signatures : mutable.ConcurrentMap[Class[_], Map[String,MethodSignature]] = new java.util.concurrent.ConcurrentHashMap[Class[_], Map[String,MethodSignature]]()

  def apply(clazz : Class[_]) = {
    signatures.getOrElseUpdate(clazz , {
      MethodSigUtil.getMethodsIncludeInherits(clazz).toMap
    })
  }

}
