package com.panaseer.functional.di.cake

import com.panaseer.functional.di.vanilla.core._
import com.panaseer.functional.di.vanilla.hive.{HiveDAO, HiveDAOImpl, HiveDataReader, HiveDataWriter}

object CakeApp extends App {

  object ControllerImpl extends ControllerComponent
    with HiveDataReaderComponent
    with HiveDataWriterComponent
    with HiveDAOComponentImpl
    with PipelineReaderComponent
    with PipelineComponent
    with ResolverComponent

  trait MockPipelineComponent extends PipelineComponent {
    override def pipeline: Pipeline = Pipeline()
  }

  object TestControllerImpl extends ControllerComponent
    with HiveDataReaderComponent
    with HiveDataWriterComponent
    with HiveDAOComponentImpl
    with PipelineReaderComponent
    with MockPipelineComponent
    with ResolverComponent

  TestControllerImpl.controller.run()

}

/**
  * Represents the abstract module of [[com.panaseer.functional.di.vanilla.core.DataReader]]
  *
  * @tparam DF
  */
trait DataReaderComponent {

  def dataReader: DataReader

}

/**
  * Represents the abstract module of [[com.panaseer.functional.di.vanilla.core.DataWriter]]
  *
  * @tparam DF
  */
trait DataWriterComponent {

  def dataWriter: DataWriter

}

/**
  * Represents the abstract module of the core [[BasicController]] relying on the abstract [[DataReaderComponent]] and
  * [[DataWriterComponent]].
  *
  * @tparam DF
  */
trait ControllerComponent {
  this: DataReaderComponent with DataWriterComponent with ResolverComponent with PipelineReaderComponent =>

  def controller: Controller = new ControllerImpl(this.pipelineReader, this.resolver)

}

/**
  * Represents the abstract module of [[FileSystem]]
  */
trait HiveDAOComponent {

  def dao: HiveDAO
}


/**
  * Specific implementation of [[HiveDAOComponent]] providing [[HiveDAO]] instances.
  */
trait HiveDAOComponentImpl extends HiveDAOComponent {

  override def dao: HiveDAO = new HiveDAOImpl()
}

/**
  * Represents the Hive base implementation of [[DataReader]]
  */
trait HiveDataReaderComponent extends DataReaderComponent {
  this: HiveDAOComponent =>

  override def dataReader: DataReader = new HiveDataReader(this.dao)

}

/**
  * Represents the Hive based implementation of [[DataWriter]]
  */
trait HiveDataWriterComponent extends DataWriterComponent {
  this: HiveDAOComponent =>

  override def dataWriter: DataWriter = new HiveDataWriter(this.dao)

}

trait ResolverComponent {
  this: DataReaderComponent with DataWriterComponent =>

  def resolver: Resolver = ResolverImpl(
    Map(dataReader.getClass.getCanonicalName -> dataReader),
    Map(dataWriter.getClass.getCanonicalName -> dataWriter)
  )
}

trait PipelineReaderComponent {
  this: PipelineComponent =>


  def pipelineReader: PipelineReader = PipelineReaderImpl(this.pipeline)

}

trait PipelineComponent {

  def pipeline: Pipeline = Pipeline(
    ReadStage("first", classOf[HiveDataReader].getCanonicalName, "firstTable", Map()) ::
      WriteStage("first", classOf[HiveDataWriter].getCanonicalName, "secondTable", Map()) :: Nil)
}


