package com.sundogsoftware.spark


import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object LeastPopularSuperHeroSelfExercise {

  case class SuperHeroNames(id: Int, name: String)

  case class SuperHero(value: String)

  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkSession using every core of the local machine
    val spark = SparkSession
      .builder
      .appName("LeastPopularSuperhero")
      .master("local[*]")
      .getOrCreate()

    // Create schema when reading Marvel-names.txt
    val superHeroNamesSchema = new StructType()
      .add("id", IntegerType, nullable = true)
      .add("name", StringType, nullable = true)

    // Build up a hero ID -> name Dataset
    import spark.implicits._
    val names = spark.read
      .schema(superHeroNamesSchema)
      .option("sep", " ")
      .csv("data/Marvel-names.txt") //The CSV loader can also used to read txt
      .as[SuperHeroNames]

    val lines = spark.read
      .text("data/Marvel-graph.txt")
      .as[SuperHero]

    val connections = lines
      .withColumn("id", split(col("value"), " ")(0))
      .withColumn("connections", size(split(col("value"), " ")) - 1)
      .groupBy("id").agg(sum("connections").alias("connections"))

    val minConnectionCount = connections.agg(min("connections")).first().getLong(0)
    //getLong -> get any values as Long

    val obscureConnection = connections.filter($"connections" === minConnectionCount)

    //We do join after filter because join is very expensive
    val obscureConnectionWithNames = obscureConnection.join(names, usingColumn = "id")

    println("The following characters have only" + minConnectionCount + "connection(s):")
    obscureConnectionWithNames.select("name").show


    spark.stop()
  }





}
