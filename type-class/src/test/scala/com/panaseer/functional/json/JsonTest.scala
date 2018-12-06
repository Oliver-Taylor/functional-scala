package com.panaseer.functional.json

import org.scalatest.{FlatSpec, Matchers}

class JsonTest extends FlatSpec with Matchers {

  import Json.Instances._
  import Json.Syntax._

  "Json" should "convert String to JsString" in {
    Json[String].write("some string") should equal(JsString("some string"))
  }

  it should "convert Double to JsDouble" in {
    Json[Double].write(2.0) should equal(JsNumber(2.0))
  }

  it should "provide simple syntax" in {
    "some string".toJson should equal(JsString("some string"))
  }

  it should "convert Option to JsNull when it is None" in {
    Json[Option[String]].write(None) should equal(JsNull)
  }

  it should "convert Option to underlying Json type with it is present" in {
    Json[Option[String]].write(Some("value")) should equal(JsString("value"))
  }

  it should "convert Seq of elements to JsArray of Json elements" in {
    Json[Seq[String]].write(Seq()) should equal(JsArray())

    val expected = JsArray(Seq(JsString("a"), JsString("b"), JsString("c")))
    Json[Seq[String]].write(Seq("a", "b", "c")) should equal(expected)
  }

  it should "convert a Map to a JsObject of Json elements" in {
    Json[Map[String, String]].write(Map()) should equal(JsObject())

    val expected = JsObject(Map("a" -> JsString("a"), "b" -> JsString("b")))
    Json[Map[String, String]].write(Map("a" -> "a", "b" -> "b")) should equal(expected)
  }

  it should "convert a Person to a JsObject" in {
    import PersonJsonInstances._

    val person = Person(name = "John", age = 32)

    val expected = JsObject(Map(
      "name" -> JsString("John"),
      "age" -> JsNumber(32)
    ))

    person.toJson should equal(expected)

  }

}
