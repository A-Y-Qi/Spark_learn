package com.sundogsoftware.spark

import org.apache.spark.sql.types.{FloatType, IntegerType, StringType, StructType}
import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object MaxTemperaturesDataset extends App{
  case class Temperature(stationID: String, date: Int, measure_type: String, temperature: Float)

  Logger.getLogger("org").setLevel(Level.ERROR)

  val spark = SparkSession
    .builder()
    .appName("MaxTemperatures")
    .master("local[*]")
    .getOrCreate()

  // Since there is no header in the data, we need to create a schema here
  val temperatureSchema = new StructType()
    .add("stationID", StringType, nullable = true)
    .add("date", IntegerType, nullable = true)
    .add("measure_type",StringType, nullable = true)
    .add("temperature", FloatType, nullable = true)

  import spark.implicits._
  val ds = spark.read
    .schema(temperatureSchema)
    .csv("data/1800.csv")
    .as[Temperature]

  // Filter out all but TMAX entries
  val maxTemps = ds.filter($"measure_type" === "TMAX")

  // Select only stationID and temperature)
  val stationTemps = maxTemps.select("stationID", "temperature")

  // Aggregate to find maximum temperature for every station
  val maxTempsByStation = stationTemps.groupBy("stationID").max("temperature")

  // Convert temperature to F and sort the dataset
  val maxTempsByStationF = maxTempsByStation
    .withColumn("temperature", round($"max(temperature)"*0.1f*(9.0f/5.0f)+32, 2))
    .select("stationID", "temperature").sort("temperature")

  // Collect, format, and print the results
  maxTempsByStationF.show()

  val results = maxTempsByStationF.collect()

  for (result <- results){
    println(s"${result(0)} maximum temperature: ${result(1).asInstanceOf[Float]}")
  }

  spark.stop()







}
