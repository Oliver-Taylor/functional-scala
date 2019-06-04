package com.panaseer.functional.di.macwire

import com.panaseer.functional.di.vanilla.core.Resolver.ClassName
import com.panaseer.functional.di.vanilla.core._
import com.panaseer.functional.di.vanilla.hive.{HiveDAO, HiveDAOImpl, HiveDataReader, HiveDataWriter}
import com.softwaremill.macwire._

object MacWire extends App {

  val app = new ResolverModule with PipelineReaderModule with PipelineModule with ControllerModule with HiveModule

  app.controller.run()

}

trait HiveModule {

  def dao: HiveDAO = wire[HiveDAOImpl]

  def reader: DataReader = wire[HiveDataReader]

  def writer: DataWriter = wire[HiveDataWriter]
}

trait PipelineReaderModule {

  def pipelineReader: PipelineReader = wire[PipelineReaderImpl]
}

trait ResolverModule {

  def resolver: Resolver = wireWith[AnyRef, Resolver](MacWireResolver)

  final case class MacWireResolver(modules: AnyRef) extends Resolver {

    val wired: Wired = wiredInModule(modules)

    override def forReader(id: ClassName): DataReader =
      wired.lookupSingleOrThrow(Class.forName(id).asInstanceOf[Class[DataReader]])

    override def forWriter(id: ClassName): DataWriter =
      wired.lookupSingleOrThrow(Class.forName(id).asInstanceOf[Class[DataWriter]])
  }

}

trait ControllerModule {

  def controller: Controller = wire[ControllerImpl]
}

trait PipelineModule {

  def pipeline: Pipeline = wireWith[Unit, Pipeline](_ => Pipeline.example)
}


