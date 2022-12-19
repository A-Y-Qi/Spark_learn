package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.log4j._
import scala.collection.immutable.ListMap


/** Count up how many of each word occurs in a book, using regular expressions. */
object WordCountBetter {
 
  /** Our main function where the action happens */
  def main(args: Array[String]) {
   
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
     // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "WordCountBetter")   
    
    // Load each line of my book into an RDD
    val input = sc.textFile("data/book.txt")
    
    // Split using a regular expression that extracts words
    val words = input.flatMap(x => x.split("\\W+"))
    // \\W is a pattern for non-word character, \\w is for word character
    
    // Normalize everything to lowercase
    val lowercaseWords = words.map(x => x.toLowerCase()) //In this operation lowercaseWords are still RDD item
    
    // Count of the occurrences of each word
    val wordCounts = lowercaseWords.countByValue() //This will transform RDD to Map

    
    // Print the results
    // wordCounts.foreach(println)
    val test=ListMap(wordCounts.toSeq.sortBy(-_._2):_*)

    // Collect, format, and print the results


    for (result <- test) {
      val word = result._1
      val num = result._2

      println(s"" +
        s"$word -> $num ")
    }
  }
  
}

