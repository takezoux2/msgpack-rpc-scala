package org.msgpack.scalarpc.builder

import java.util.Date

/**
 *
 * User: takeshita
 * Create: 12/06/15 17:06
 */

class VariousMethodObj extends Inherit {

  def this( bbb : String) = {this()}

  def noArgsNoReturn() : Unit = {}

  def noArgsReturnGeneric() : Option[Int] = { None }
  def noArgsReturnGeneric2() : Option[Date] = { None }

  def onePrimitiveArgReturnPrimitive( d : Double) : Int = { 1 }

  def someArgsReturnAnyRef( l : Long, s : String, fs : List[Float]) : List[String] = Nil

}

trait Inherit{
  def inheritMethod(a : Int, b : Int) : String = ""
}
