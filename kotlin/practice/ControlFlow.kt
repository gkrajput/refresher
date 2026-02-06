package practice

fun main() {

    // Use == for value comparison, === for reference comparison.
    // if statement
    val number = 10
    if (number > 0) {
        println("$number is positive")
    } else {
        println("$number is not positive")
    }

    // if as an expression
    val result = if (number % 2 == 0) "even" else "odd"
    println("$number is $result")

    // when statement
    val obj = "Hello"
    when (obj) {
        "1" -> println("One")
        "Hello" -> println("Greeting")
        else -> println("Unknown")
    }

    // when as an expression
    val day = 3
    val dayName = when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        7 -> "Sunday"
        else -> "Invalid day"
    }
    println("Day $day of the week is $dayName")

    // when can also be used without a subject
    val trafficLightState = "Red" // This can be "Green", "Yellow", or "Red"
    val trafficAction = when {
        trafficLightState == "Green" -> "Go"
        trafficLightState == "Yellow" -> "Slow down"
        trafficLightState == "Red" -> "Stop"
        else -> "Malfunction"
    }
    println(trafficAction)


    /*
    Ranges:
    1..4 is equivalent to 1, 2, 3, 4
    1..<4 is equivalent to 1, 2, 3
    4 downTo 1 is equivalent to 4, 3, 2, 1
    1..5 step 2 is equivalent to 1, 3, 5
    'a'..'d' is equivalent to 'a', 'b', 'c', 'd'
    'z' downTo 's' step 2 is equivalent to 'z', 'x', 'v', 't'
     */

    // for loop
    for (index in 1..5) {
        println("Index: $index")
    }

    val items = listOf("apple", "banana", "cherry")
    for (item in items) {
        println("Item: $item")
    }

    // while loop
    var count = 5
    while (count > 0) {
        println("Count: $count")
        count--
    }

    // do-while loop
    var doCount = 0
    do {
        println("Do Count: $doCount")
        doCount--
    } while (doCount > 0)

}