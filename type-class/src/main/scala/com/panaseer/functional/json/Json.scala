package com.panaseer.functional.json

sealed trait Json

final case class JsObject(json: Map[String, Json] = Map()) extends Json

final case class JsArray(json: Seq[Json] = Nil) extends Json

final case class JsString(json: String) extends Json

final case class JsNumber(json: Double) extends Json

case object JsNull extends Json

object Json {

  def apply[A: JsonWriter]: JsonWriter[A] = implicitly[JsonWriter[A]]

  object Instances {

    implicit val stringWriter: JsonWriter[String] = JsonWriter(JsString)

    implicit val doubleWriter: JsonWriter[Double] = JsonWriter(JsNumber)

    implicit def optionWriter[A: JsonWriter]: JsonWriter[Option[A]] = ???

    implicit def seqWriter[A: JsonWriter]: JsonWriter[Seq[A]] = ???

    implicit def mapWriter[A: JsonWriter]: JsonWriter[Map[String, A]] = ???

  }

  object Syntax {

    implicit class JsonLike[A: JsonWriter](underlying: A) {

      def toJson: Json = Json[A].write(underlying)
    }

  }

}

trait JsonWriter[A] {

  def write(value: A): Json
}

object JsonWriter {

  def apply[A](f: A => Json): JsonWriter[A] = (value: A) => f(value)
}


