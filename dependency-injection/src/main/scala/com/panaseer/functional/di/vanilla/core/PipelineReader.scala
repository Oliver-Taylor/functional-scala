package com.panaseer.functional.di.vanilla.core

import com.google.inject.Inject

trait PipelineReader {

  def read(): Pipeline

}

final case class PipelineReaderImpl @Inject() (pipeline: Pipeline) extends PipelineReader {

  override def read(): Pipeline = this.pipeline
}
