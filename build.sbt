name := "functional-scala"

ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "2.12.8"

lazy val commonDependencies = Seq(
  "com.typesafe" % "config" % "1.3.3",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "test")

lazy val typeClass = (project in file("type-class"))
  .settings(libraryDependencies ++= commonDependencies)
  