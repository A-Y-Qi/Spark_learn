package com.sundogsoftware.spark


import org.apache.log4j._
import org.apache.spark._
import org.apache.spark.sql.catalyst.expressions.Ascending

object TotalSpentByCustomerSelfExercise extends App{
  Logger.getLogger("org").setLevel(Level.ERROR)

  // Create a SparkContext using the local machine
  val sc = new SparkContext("local", "TotalSpentByCustomerSelfExercise")

  // Load each line of the data into an RDD
  val input = sc.textFile("data/customer-orders.csv")

  // Split using a regular expression that extracts words
  val stringOrders = input.map(x => x.split(",")) //each line is a List

  // transfer the third price into integers
  val orders = stringOrders.map(x=>(x(0).toInt,x(2).toFloat))

  // count the total money spent by each customer
  val perCustomer = orders.reduceByKey((x,y)=>x+y).sortBy(x=> -x._2)


  for (customer <- perCustomer){
    println(f"customer ${customer._1} spent total ${customer._2} dollar")
  }






}
