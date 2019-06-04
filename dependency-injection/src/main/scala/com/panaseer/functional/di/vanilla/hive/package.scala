package com.panaseer.functional.di.vanilla

import com.google.inject.Inject
import com.panaseer.functional.di.vanilla.core.{DataFrame, DataFrameImpl, DataReader, DataWriter}

package object hive {

  final class HiveDataReader @Inject() (dao: HiveDAO) extends DataReader {

    override def read: String => DataFrame = table => {
      if (dao.exists(table)) {
        dao.readDF(table)
      } else {
        throw new IllegalArgumentException("Table does not exist")
      }
    }
  }

  final class HiveDataWriter @Inject() (dao: HiveDAO) extends DataWriter {

    override def write(name: String): DataFrame => Unit = {
      if (dao.exists(name)) {
        dao.writeDF(name, _)
      } else {
        dao.createTable(name, _)
      }
    }
  }

  trait HiveDAO {

    def exists(table: String): Boolean

    def createTable(table: String, df: DataFrame): Unit

    def readDF(table: String): DataFrame

    def writeDF(table: String, df: DataFrame): Unit

  }

  final class HiveDAOImpl extends HiveDAO {

    override def exists(table: String): Boolean = {
      println(s"HiveDAO check if $table exists")
      true
    }

    override def createTable(table: String, df: DataFrame): Unit = {
      println(s"HiveDAO create table $table")
    }

    override def readDF(table: String): DataFrame = {
      println(s"HiveDAO reading table $table")
      DataFrameImpl(table)
    }

    override def writeDF(table: String, df: DataFrame): Unit = {
      println(s"HiveDAO writing to table $table")
    }
  }

}
