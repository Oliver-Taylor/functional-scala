name := "functional-scala"

ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "2.12.8"

ThisBuild / scalacOptions += "-Ypartial-unification"

val circeVersion = "0.10.0"

lazy val commonDependencies = Seq(
  "com.typesafe" % "config" % "1.3.3",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "test")

lazy val typeClass = (project in file("type-class"))
  .settings(libraryDependencies ++= commonDependencies)

lazy val dependencyInjection = (project in file("dependency-injection"))
  .settings(libraryDependencies ++= commonDependencies)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "1.5.0",
    "net.codingwell" %% "scala-guice" % "4.2.2",
    "com.google.inject" % "guice" % "4.2.2",
    "com.softwaremill.macwire" %% "macros" % "2.3.1" % "provided",
    "com.softwaremill.macwire" %% "util" % "2.3.1",
    "com.softwaremill.macwire" %% "proxy" % "2.3.1"
  ))

lazy val effects = (project in file("effects"))
  .settings(libraryDependencies ++= commonDependencies)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.0.0-M1",
    "org.typelevel" %% "cats-effect" % "2.0.0-M1"
  ))