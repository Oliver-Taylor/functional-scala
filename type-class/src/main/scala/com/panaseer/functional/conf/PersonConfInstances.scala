package com.panaseer.functional.conf

object PersonConfInstances {

  implicit def personReader(implicit nameReader: ConfReader[String], ageReader: ConfReader[Int]): ConfReader[Person] =
    ConfReader.instance(config => path => {
      val personConf = config.getConfig(path)
      Person(
        name = nameReader.read(personConf, "name"),
        age = ageReader.read(personConf, "age")
      )
    })

}

case class Person(name: String, age: Int)
