package com.panaseer.functional.di.vanilla.core

import javax.inject.Inject

trait Controller {

  def run(): Unit

}

final class ControllerImpl @Inject() (reader: PipelineReader, resolver: Resolver) extends Controller {

  type State = Map[String, DataFrame]

  val State: State = Map[String, DataFrame]()


  override def run(): Unit = {

    val pipeline = reader.read()

    pipeline.stages.foldLeft(State)((state, stage) => stage match {
      case ReadStage(id, className, table, _) =>
        val reader = resolver.forReader(className)

        println(s"Reading df: $id using Reader: $className")
        val df = reader.read(table)
        state + (id -> df)


      case WriteStage(id, className, table, _) =>
        val writer = resolver.forWriter(className)

        println(s"Writing df: $id using Writer: $className")
        val df: DataFrame = state(id)

        writer.write(table)(df)

        state
    })
  }
}
