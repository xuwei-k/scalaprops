package scalaprops

import java.util.concurrent.atomic.AtomicBoolean

object PropertyTest extends Scalaprops{

  val `"forAll(result: => Boolean)" is lazy` = Property.forAll{
    var sideEffect = false
    val p = Property.forAll{
      sideEffect = true
      true
    }
    assert(sideEffect == false)
    p.check(Param.withCurrentTimeSeed(), new AtomicBoolean(), _ => ())
    sideEffect
  }

  val `Bool#implies is lazy` = Property.forAll{
    val f = Bool.bool(false)
    var flag = true
    def sideEffect(): Unit = flag = false
    assert(f.implies[Boolean]{ sideEffect(); true}.f(100, param.rand)._1.isNoResult)
    assert(f.implies[Bool]{ sideEffect(); f}.f(100, param.rand)._1.isNoResult)
    assert(f.implies[Property]{ sideEffect(); Property.prop(true)}.f(100, param.rand)._1.isNoResult)
    flag
  }
}
