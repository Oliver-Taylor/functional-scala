package com.panaseer.functional.show

import java.time.Instant

import org.scalatest.{FlatSpec, Matchers}

class ShowTest extends FlatSpec with Matchers {

  import Show.Instances._

  "Show" should "convert an Int to a String with a single decimal place" in {
    Show[Int].show(2) should equal("2.0")
  }

  it should "accept a String and return a String in UpperCase" in {
    Show[String].show("convert_to_upper") should equal("CONVERT_TO_UPPER")
  }

  it should "allowing creating instances from toString" in {
    val now = Instant.now()
    Show.fromToString[Instant].show(now) should equal(now.toString)
  }

  it should "accept a Person and return only their name" in {
    import PersonShowInstances._
    val person = Person(name = "John", age = 32)
    Show[Person].show(person) should equal(s"Person[name: ${person.name}]")
  }

  it should "provide syntax for easily creating show instances" in {
    import PersonShowInstances._
    import Show.Syntax._

    val person = Person(name = "John", age = 32)
    person.show should equal(s"Person[name: ${person.name}]")
  }

  it should "provide a method for formatting an Instance" in {
    import PersonShowInstances._
    val person = Person(name = "John", age = 32)
    Show.format(person) should equal(s"Person[name: ${person.name}]")
  }

  it should "provide a method for printing to the console" in {
    import PersonShowInstances._
    Show.print(Person(name = "John", age = 32))
  }

}
