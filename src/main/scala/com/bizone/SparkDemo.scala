/*package com.bizone
import com.bizone.common.SparkManager.spark

import scala.io.Source
import com.bizone.common.SparkManager.spark.implicits._
object SparkDemo {
  def main(args : Array[String]) {

    //Load Json Mapper DF
    val source: String = Source.fromResource("spark-file/example.json").getLines.mkString
    val otherPeopleDataset = spark.createDataset(source :: Nil)
    val df = spark.read.json(otherPeopleDataset)
    df.show()

    df.select("name").show()

    df.createOrReplaceTempView("people")
    val sqlDF = spark.sql("SELECT * FROM people")
    sqlDF.show()

    //load json convert dataset
    val userJson: String = Source.fromResource("spark-file/user.json").getLines.mkString
    val userDataset = spark.createDataset(userJson :: Nil)
    val userDS = spark.read.json(userDataset).as[User]
    val filterDS = userDS.where("name like '%tin duc%' and password = 3")
    filterDS.show()
    val userDF = userDS.createOrReplaceTempView("user")
    val sqlUser =  spark.sql("SELECT name FROM user")
    sqlUser.show()

    //load data json from storage account
    val userDS = spark.read.json("/mnt/testg2/user.json").as[User]
    //load data text from storage account
    val UserTxTDF = spark.read.option("header",
      "true").csv("/mnt/testg2/user.txt")
    UserTxTDF.select("name").show()

    // foreach print col_0 => name
    UserTxTDF.collect().foreach(t => println(t(0)))

    // get 7th and return last row
    println(UserTxTDF.take(7).last)

    // get index of array
    val values = UserTxTDF.rdd.collect()
    println(values(0))

    spark.read.option("header",true).csv("dbfs:/mnt/testg2/user.csv").show()

  }
}
*/