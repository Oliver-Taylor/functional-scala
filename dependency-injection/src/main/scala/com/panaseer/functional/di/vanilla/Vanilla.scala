package com.panaseer.functional.di.vanilla

import com.panaseer.functional.di.vanilla.core._
import com.panaseer.functional.di.vanilla.hive.{HiveDAOImpl, HiveDataReader, HiveDataWriter}

object Vanilla extends App {

  val (reader, writer) = HiveModule()

  val resolver = ResolverModule(reader :: Nil, writer :: Nil)

  val pipelineReader = PipelineReaderModule(Pipeline.example)

  val app = AppModule(resolver, pipelineReader)

  app.run()

}

object HiveModule {

  def apply(): (HiveDataReader, HiveDataWriter) = {
    val dao = new HiveDAOImpl()
    (new HiveDataReader(dao), new HiveDataWriter(dao))
  }

}

object ResolverModule {

  def apply(readers: Seq[DataReader], writers: Seq[DataWriter]): Resolver = {

    ResolverImpl(
      readers.groupBy(_.getClass.getCanonicalName).mapValues(_.head),
      writers.groupBy(_.getClass.getCanonicalName).mapValues(_.head)
    )
  }
}

object PipelineReaderModule {

  def apply(pipeline: Pipeline): PipelineReader = PipelineReaderImpl(pipeline)
}

object AppModule {

  def apply(resolver: Resolver, reader: PipelineReader): Controller = new ControllerImpl(reader, resolver)
}




