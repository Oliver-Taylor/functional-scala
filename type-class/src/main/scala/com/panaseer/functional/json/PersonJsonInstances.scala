package com.panaseer.functional.json

object PersonJsonInstances {

  implicit def personWriter(implicit intWriter: JsonWriter[Double], stringWriter: JsonWriter[String]): JsonWriter[Person] = ???

}

case class Person(name: String, age: Int)
