package com.sundogsoftware.spark

import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object TotalSpentByCustomerDatasetSelfExercise extends App{
  Logger.getLogger("org").setLevel(Level.ERROR)

  case class Shopping (customerID: Int, itemID: Int, price: Float)

  val spark = SparkSession
    . builder().appName("TotalSpentByCustomerDatasetSelf")
    .master("local[*]")
    .getOrCreate()

  val shoppingSchema =  new StructType()
    .add("customerID", IntegerType, nullable = true)
    .add("itemID", IntegerType, nullable = true)
    .add("price", FloatType, nullable = true)

  // Read the file as dataset

  import spark.implicits._

  val ds = spark.read
    .schema(shoppingSchema)
    .csv("data/customer-orders.csv")
    .as[Shopping]

  val sponsers = ds.select("customerID","price")
  val results = sponsers
    .groupBy("CustomerID")
    .agg(round(sum("price"),2).alias("totalCost"))
    .sort($"totalCost".desc)

  val show = results.collect()

  for (row <- show){
    println(s"${row(0)} spent ${row(1)} totally")
  }


  spark.stop()

}
