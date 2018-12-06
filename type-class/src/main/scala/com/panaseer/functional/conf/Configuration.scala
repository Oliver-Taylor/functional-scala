package com.panaseer.functional.conf

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

trait Configuration {

  def get[A: ConfReader](path: String): A

  def getOrElse[A: ConfReader](path: String)(default: => A): A

}

object Configuration {

  import scala.collection.JavaConverters._

  def apply(underlying: Config): Configuration = new Configuration {

    override def get[A: ConfReader](path: String): A = ConfReader[A].read(underlying, path)

    override def getOrElse[A: ConfReader](path: String)(default: => A): A =
      Try(ConfReader[A].read(underlying, path)).getOrElse(default)

  }

  def parse(confString: String): Configuration = Configuration(ConfigFactory.parseString(confString))

  def empty: Configuration = Configuration(ConfigFactory.empty())

  implicit val stringReader: ConfReader[String] = ConfReader.instance(_.getString)

  implicit val intReader: ConfReader[Int] = ConfReader.instance(_.getInt)

  implicit def optionReader[A](implicit readable: ConfReader[A]): ConfReader[Option[A]] = ConfReader.instance(conf => path => {
    if (conf.getIsNull(path)) None else Some(readable.read(conf, path))
  })

  implicit def tryReader[A](implicit readable: ConfReader[A]): ConfReader[Try[A]] =
    ConfReader.instance(conf => path => Try(readable.read(conf, path)))

  implicit val stringsReader: ConfReader[Seq[String]] = ConfReader.instance(_.getStringList).map(_.asScala)

  implicit val intsReader: ConfReader[Seq[Int]] = ConfReader.instance(_.getIntList).map(_.asScala.map(_.toInt))

}

trait ConfReader[A] {
  self =>

  def read(config: Config, path: String): A

  def map[B](f: A => B): ConfReader[B] = ConfReader.instance(config => path => {
    f(self.read(config, path))
  })

}

object ConfReader {

  def apply[A: ConfReader]: ConfReader[A] = implicitly[ConfReader[A]]

  def instance[A](f: Config => String => A): ConfReader[A] = (config: Config, path: String) => f(config)(path)
}