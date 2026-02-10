package practice

import kotlin.math.PI

fun hello() {
    println("Hello, World!")
    // `return Unit` or `return` is optional
}

// Using Default Arguments
fun printMsgWithPrefix(msg: String, prefix: String = "TRACE") {
    println("[$prefix] $msg")
}

fun sum(a: Int, b: Int): Int {
    return a + b
}

// We can remove the curly braces {} and declare the function body using the assignment operator =
// When we use the assignment operator =, Kotlin uses type inference, so we can also omit the return type
fun sum2(a: Int, b: Int) = a + b // Expression body syntax

fun circleArea(radius: Int) = PI * radius * radius

// Using named arguments and default values
fun intervalInSeconds(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): Int {
    return ((hours * 60) + minutes) * 60 + seconds
}

// Pass a lambda expression as a parameter to another function
fun performOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

// Return a lambda expression from a function
fun perform(operationType: String): (Int, Int) -> Int {
    return when (operationType) {
        "add" -> { x, y -> x + y }
        "subtract" -> { x, y -> x - y }
        "multiply" -> { x, y -> x * y }
        else -> { x, y -> x / y } // Default to division
    }
}

// Return a lambda that converts time units to seconds based on the input string
fun toSeconds(time: String): (Int) -> Int = when (time) {
    "hour" -> { value -> value * 60 * 60 }
    "minute" -> { value -> value * 60 }
    "second" -> { value -> value }
    else -> { value -> value }
}

// Return a lambda not taking any params and not returning anything
fun greet(): () -> Unit {
    return { println("Hello from the greet function!") }
}

fun trailingLambda(operation: (Int, Int) -> Int): Int {
    return operation(10, 5)
}

fun repeatN(n: Int, action: () -> Unit): Unit {
    for (i in 1..n) {
        action()
    }
}

fun main() {
    hello()

    println(sum(1, 2))

    printMsgWithPrefix("This is a message", "INFO")
    // Using named arguments in any order
    printMsgWithPrefix(prefix = "DEBUG", msg = "This is a debug message")
    // Using default prefix
    printMsgWithPrefix("This is a trace message")

    println("Circle area with radius 5: ${circleArea(5)}")

    println(intervalInSeconds(1, 20, 15))
    println(intervalInSeconds(minutes = 1, seconds = 25))
    println(intervalInSeconds(hours = 2))
    println(intervalInSeconds(minutes = 10))
    println(intervalInSeconds(hours = 1, seconds = 1))

    // Lambda expression
    val upperCaseString = { text: String -> text.uppercase() }
    println(upperCaseString("hello lambda"))

    // Lambda with no parameters
    val helloLambda = { println("Hello from lambda!") }
    helloLambda()

    println(performOperation(5, 3, { x, y -> x * y })) // Using a lambda to multiply two numbers

    val add = { x: Int, y: Int -> x + y }
    println(performOperation(5, 3, add)) // Using a lambda variable to add two numbers

    val operation = perform("subtract")
    println(operation(10, 4)) // Using the returned lambda to subtract two numbers

    val timesInMinutes = listOf(2, 10, 15, 1)
    val min2sec = toSeconds("minute")
    val totalTimeInSeconds = timesInMinutes.sumOf(min2sec)
    println("Total time is $totalTimeInSeconds secs")

    val greetFunction = greet()
    greetFunction() // Calling the returned lambda to print a greeting message

    // Invoke a lambda expression on its own
    println({ x: Int -> x * x }(5)) // Output: 25

    // Trailing lambda syntax when the lambda is the last argument of a function
    println(performOperation(5, 3) { x, y -> x - y }) // Using a lambda to subtract two numbers
    println(trailingLambda { x, y -> x * y })

    val actions = listOf("title", "year", "author")
    val prefix = "https://example.com/book-info"
    val id = 5
    val urls = actions.map { action: String -> "$prefix/$id/$action" }
    println(urls)

    repeatN(5, {println("Hello")}) // Using a lambda to repeat an action 5 times
    repeatN(10) { print("=") } // Using trailing lambda syntax to repeat an action 10 times
}