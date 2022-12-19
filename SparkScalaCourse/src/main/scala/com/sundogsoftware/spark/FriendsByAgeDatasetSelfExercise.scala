package com.sundogsoftware.spark

import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object FriendsByAgeDatasetSelfExercise extends App{

  case class Person(id:Int, name:String, age:Int, friends:Int)
  // Set the log level to only print errors
  Logger.getLogger("org").setLevel(Level.ERROR)
  // Use new SparkSession interface in Spark 2.0
  val spark = SparkSession
    .builder
    .appName("SparkFakeFriends")
    .master("local[*]")
    .getOrCreate()

  // Convert our csv file to a DataSet, using our Person case
  // class to infer the schema.

  import spark.implicits._ //need it whenever a schema is being inferred

  val people = spark.read
    .option("header", "true") // since in the header we have the four attributes
    .option("inferSchema", "true") // infer the header as schema
    .csv("data/fakefriends.csv")
    .as[Person]

  val result = people.select("age","friends").groupBy("age").agg(round(avg("friends"), scale =0).alias("friends_avg")).sort("age")
  //We need to use this Aggregate function here, because we are aggregating the average (usually used after groupBy)
  result.show()
  people.select("friends", "age").groupBy("friends").agg(min("age").alias("test")).sort("test").show(10)
  //make sure your alias is in agg(), or you will not be able to use the alias name afterwards


  spark.stop()

}
