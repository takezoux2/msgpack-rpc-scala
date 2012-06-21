package org.msgpack.scalarpc

import org.specs2.mutable.SpecificationWithJUnit
import org.msgpack.rpc.dispatcher.StopWatchDispatcher
import org.msgpack.rpc.builder.StopWatchDispatcherBuilder
import org.msgpack.ScalaMessagePack

/**
 *
 * User: takeshita
 * Create: 12/06/19 1:09
 */

class UsageTest extends SpecificationWithJUnit {

  "Server and client" should{
    "initialized and work" in{
      val server = ScalaServer().withModifyDispatcherBuilder(d => {
        new StopWatchDispatcherBuilder(d).withVerboseOutput(true)
      }).withServeObject(new SampleServer()).listen(12345)
      val client = ScalaClient("localhost",12345)
      val proxy = client.proxy[SampleInterface]

      try{
        proxy.ping must_== "ok"
        proxy.echo("wahoo") must_== "wahoo"
        proxy.sendGeneric(List(1,2,3),Map(1 -> 3L,3 -> 4L)) must_== Right(false)
        proxy.sendGeneric(List(1,2,3),Map(1 -> 3L,2 -> 2L,3 -> 4L)) must_== Left(9.0)
      }finally{
        server.close()
        client.close()
      }

      ok
    }


  }

}

trait SampleInterface{

  def ping() : String

  def echo( s : String) : String


  def sendGeneric( v : List[Int], m : Map[Int,Long]) : Either[Double,Boolean]

}

class SampleServer extends SampleInterface{
  def ping(): String = "ok"

  def echo(s: String): String = s


  def sendGeneric(v: List[Int], m: Map[Int, Long]): Either[Double, Boolean] = {
    v.forall(m.contains(_)) match{
      case true => Left(m.values.sum.toDouble)
      case false => Right(false)
    }
  }
}
