package com.panaseer.functional.json

object PersonJsonInstances {

  implicit def personWriter(implicit intWriter: JsonWriter[Double], stringWriter: JsonWriter[String]): JsonWriter[Person] =
    JsonWriter { person =>
      JsObject(Map(
        "name" -> stringWriter.write(person.name),
        "age" -> intWriter.write(person.age)
      ))
    }

}

case class Person(name: String, age: Int)
