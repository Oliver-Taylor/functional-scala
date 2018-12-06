package com.panaseer.functional.conf

import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Success, Try}

class ConfigurationTest extends FlatSpec with Matchers {

  import com.panaseer.functional.conf.Configuration._

  "Configuration" should "extract String" in {
    Configuration.parse("path: Hello").get[String]("path") should equal("Hello")
  }

  it should "extract Int from Configuration" in {
    Configuration.parse("path: 1").get[Int]("path") should equal(1)
  }

  it should "extract Configuration null as Empty Option" in {
    Configuration.parse("path: null").get[Option[Int]]("path") should be(empty)
  }

  it should "extract Configuration value as Some" in {
    Configuration.parse("path: 1").get[Option[Int]]("path") should contain(1)
  }

  it should "extract sequence of elements" in {
    Configuration.parse("path: [1, 2, 3, 4, 5]").get[Seq[Int]]("path") should equal(Seq(1, 2, 3, 4, 5))
  }

  it should "extract Try Failure when error is thrown" in {
    Configuration.empty.get[Try[String]]("path").isFailure shouldBe true
  }

  it should "extract Try Success when error is not thrown" in {
    Configuration.parse("path: 1").get[Try[Int]]("path") should equal(Success(1))
  }

  it should "extract Person from Configuration" in {

    import PersonConfInstances._

    val conf = Configuration.parse {
      """person { name: John, age = 32 }"""
    }

    conf.get[Person]("person") should equal(Person(name = "John", age = 32))
  }

}
