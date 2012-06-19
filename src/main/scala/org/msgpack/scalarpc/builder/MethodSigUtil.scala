package org.msgpack.scalarpc.builder

import java.lang.reflect.{Type => JType}
import org.msgpack.scalautil.ScalaSigUtil
import tools.scalap.scalax.rules.scalasig._
import reflect.ScalaSignature


/**
 *
 * User: takeshita
 * Create: 12/06/15 16:33
 */

object MethodSigUtil {

  def getMethodsIncludeInherits(clazz : Class[_]) : Seq[(String,MethodSignature)] = {
    if (clazz == null || clazz.getAnnotation(classOf[ScalaSignature]) == null) return Nil
    val selfMethods : Seq[(String,MethodSignature)] = getMethods(clazz)
    val superMethods : Seq[(String,MethodSignature)] = getMethodsIncludeInherits(clazz.getSuperclass)
    val interfaceMethods : Seq[(String,MethodSignature)] = clazz.getInterfaces.flatMap(getMethodsIncludeInherits(_)).toSeq

    selfMethods ++ superMethods ++ interfaceMethods
  }

  def getMethods(clazz : Class[_]) : Seq[(String,MethodSignature)] = {
    val sig = ScalaSigParser.parse(clazz)
    if(sig.isEmpty){
      return Nil
    }
    sig.get.symbols.withFilter( _ match {
      case ms : MethodSymbol => ms.name != "<init>" && ms.name != "$init$"  // exclude constructors
      case _ => false
    }).flatMap(s => toMethodSignature(s.asInstanceOf[MethodSymbol])).map(sig => sig.name -> sig)
  }

  def toMethodSignature( methodSymbol : MethodSymbol) = {
     methodSymbol.infoType match{
      case NullaryMethodType(returnType) => {
        Some(MethodSignature(methodSymbol.name,Nil, ScalaSigUtil.toJavaClass(returnType).get))
      }
      case MethodType(returnType,methodParams) => {
        Some(MethodSignature(methodSymbol.name,methodParams.map(
          mp => ScalaSigUtil.toJavaClass(mp.asInstanceOf[MethodSymbol].infoType).get
        ), ScalaSigUtil.toJavaClass(returnType).get))
      }
      case _ => None
    }
  }

}

case class MethodSignature(name : String, args : Seq[JType], returnType : JType)
