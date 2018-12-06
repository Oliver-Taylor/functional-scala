package com.panaseer.functional.conf

object PersonConfInstances {

  implicit def personReader(implicit nameReader: ConfReader[String], ageReader: ConfReader[Int]): ConfReader[Person] = ???

}

case class Person(name: String, age: Int)
