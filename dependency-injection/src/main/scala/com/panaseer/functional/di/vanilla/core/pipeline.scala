package com.panaseer.functional.di.vanilla.core

import com.panaseer.functional.di.vanilla.hive.{HiveDataReader, HiveDataWriter}

final case class Pipeline(stages: Seq[Stage] = Seq())

object Pipeline {

  def example: Pipeline = Pipeline(
    ReadStage("first", classOf[HiveDataReader].getCanonicalName, "firstTable", Map()) ::
      WriteStage("first", classOf[HiveDataWriter].getCanonicalName, "secondTable", Map()) :: Nil)

}

trait Stage {

  def id: String

  def className: String

  def props: Map[String, Any]
}

final case class ReadStage(id: String, className: String, table: String, props: Map[String, Any]) extends Stage

final case class WriteStage(id: String, className: String, table: String, props: Map[String, Any]) extends Stage
