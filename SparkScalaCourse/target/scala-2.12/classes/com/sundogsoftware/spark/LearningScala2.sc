// Flow control

// If / else:
if (1 > 3) println("Impossible!") else println("The world makes sense.")

if (1 > 3) {
  println("Impossible!")
  println("Really?")
} else {
  println("The world makes sense.")
  println("still.")
}

// Matching
val number = 2
number match {
  case 1 => println("One")
  case 2 => println("Two")
  case 3 => println("Three")
  case _ => println("Something else")
}

for (x <- 1 to 4) {
  val squared = x * x
  println(squared)
}

var x = 10
while (x >= 0) {
  println(x)
  x -= 1
}

x = 0
do { println(x); x+=1 } while (x <= 10)

// Expressions

{val x = 10; x + 20}

println({val x = 10; x + 20})

// EXERCISE
// Write some code that prints out the first 10 values of the Fibonacci sequence.
// This is the sequence where every number is the sum of the two numbers before it.
// So, the result should be 0, 1, 1, 2, 3, 5, 8, 13, 21, 34

def Fibo(n1: Int, n2: Int, count: Int, ind: Int): Unit= {
  if (count == ind){
    println(n1)
  }
  else{
    println(n1)
    Fibo(n2,n1+n2,count+1,ind)
  }
}
def printFibo(num:Int):Unit={
  Fibo(0,1,1,num)
}
printFibo(11)



