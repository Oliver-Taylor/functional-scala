package com.panaseer.functional.di.vanilla.core

import com.panaseer.functional.di.vanilla.core.Resolver.ClassName

trait Resolver {

  def forReader(id: ClassName): DataReader

  def forWriter(id: ClassName): DataWriter

}

object Resolver {

  type ClassName = String
}

final case class ResolverImpl(readers: Map[ClassName, DataReader], writers: Map[ClassName, DataWriter]) extends Resolver {

  override def forReader(id: ClassName): DataReader = readers(id)

  override def forWriter(id: ClassName): DataWriter = writers(id)
}
