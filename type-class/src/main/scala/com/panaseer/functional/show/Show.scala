package com.panaseer.functional.show

import java.text.DecimalFormat

trait Show[A] {

  def show(a: A): String
}

object Show {

  def apply[A: Show]: Show[A] = implicitly[Show[A]]

  def instance[A](f: A => String): Show[A] = a => f(a)

  def fromToString[A]: Show[A] = Show.instance(_.toString)

  def format[A: Show](a: A): String = Show[A].show(a)

  def print[A: Show](a: A): Unit = println(format(a))

  object Instances {

    implicit val intWithSingleDecimalShow: Show[Int] = Show.instance(int => new DecimalFormat("#.0").format(int))

    implicit val stringToUpperCaseShow: Show[String] = Show.instance(_.toUpperCase)

  }

  object Syntax {

    implicit class ShowLike[A: Show](underlying: A) {

      def show: String = Show[A].show(underlying)
    }

  }

}
