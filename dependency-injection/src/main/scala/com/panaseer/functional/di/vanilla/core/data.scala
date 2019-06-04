package com.panaseer.functional.di.vanilla.core

import com.google.inject.Inject

trait DataFrame {

  def name: String
}

final case class DataFrameImpl(name: String) extends DataFrame

trait DataReader {

  def read: String => DataFrame

}

class FsDataReader @Inject()(fs: FileSystem) extends DataReader {

  override def read: String => DataFrame = fs.read
}

trait DataWriter {

  def write(name: String): DataFrame => Unit
}

class FsDataWriter @Inject()(fs: FileSystem) extends DataWriter {

  override def write(name: String): DataFrame => Unit = fs.write
}

trait FileSystem {

  def read(name: String): DataFrame

  def write(df: DataFrame): Unit
}

class FileSystemImpl extends FileSystem {

  def read(name: String): DataFrame = DataFrameImpl(name)

  def write(df: DataFrame): Unit = {
    println(s"Writing $df to FileSystem")
  }
}

class BasicController @Inject()(reader: DataReader, writer: DataWriter) {

  def exec(name: String): Unit = {
    (reader.read andThen writer.write(name)) (name)
  }
}