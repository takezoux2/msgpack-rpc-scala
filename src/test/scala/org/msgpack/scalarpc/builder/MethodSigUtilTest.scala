package org.msgpack.scalarpc.builder

import org.specs2.mutable.SpecificationWithJUnit

/**
 *
 * User: takeshita
 * Create: 12/06/15 17:05
 */

class MethodSigUtilTest extends SpecificationWithJUnit {

  "getMethods@MethodSigUtil" should{
    "Enumerate all methods"  in{
      val methods = MethodSigUtil.getMethods(classOf[VariousMethodObj])

      methods.foreach(println(_))

      methods must haveSize(5)
    }
  }

  "getMethodsIncludeInherits@MethodSigUtil" should{
    "enumerate all methods include inherit classes" in{
      val methods = MethodSigUtil.getMethodsIncludeInherits(classOf[VariousMethodObj])

      methods.foreach(println(_))

      methods must haveSize(6)

    }
  }

}


