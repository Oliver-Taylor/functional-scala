package com.panaseer.functional.show

object PersonShowInstances {

  implicit val showPersonName: Show[Person] = Show.instance(p => s"Person[name: ${p.name}]")
}

case class Person(name: String, age: Int)
