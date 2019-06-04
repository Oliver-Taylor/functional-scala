package com.panaseer.functional.di.guice

import com.google.inject.{AbstractModule, Guice, Key}
import com.panaseer.functional.di.vanilla.core.Resolver.ClassName
import com.panaseer.functional.di.vanilla.core._
import com.panaseer.functional.di.vanilla.hive.{HiveDAO, HiveDAOImpl, HiveDataReader, HiveDataWriter}
import net.codingwell.scalaguice.ScalaModule

class HiveModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[HiveDAO].to[HiveDAOImpl]
    bind[DataReader].to[HiveDataReader]
    bind[DataWriter].to[HiveDataWriter]
  }
}

class ResolverModule extends AbstractModule with ScalaModule {

  import com.google.inject.{Injector, Provides}

  @Provides
  def resolver(injector: Injector): Resolver = {

    return new Resolver {

      override def forReader(id: ClassName): DataReader = {

        injector.getInstance(Key.get(Class.forName(id))).asInstanceOf[DataReader]
      }

      override def forWriter(id: ClassName): DataWriter = {
        injector.getInstance(Key.get(Class.forName(id))).asInstanceOf[DataWriter]
      }
    }

  }
}

class ControllerModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[Controller].to[ControllerImpl]
  }

}

class PipelineReaderModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[Pipeline].toInstance(Pipeline.example)
    bind[PipelineReader].to[PipelineReaderImpl]
  }
}

object GuiceApp extends App {

  val injector = Guice.createInjector(new HiveModule(), new ResolverModule(), new ControllerModule(), new PipelineReaderModule())

  import net.codingwell.scalaguice.InjectorExtensions._

  val controller = injector.instance[Controller]

  controller.run()

  val x = """ingest.modules += [hive, kafka, phoenix]"""
}

