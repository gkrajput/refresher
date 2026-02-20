package practice

/*
Scope functions allows you to create a temporary scope around an object and execute some code.
Scope functions make your code more concise because you don't have to refer to the name of your object within the
temporary scope. Depending on the scope function, you can access the object either by referencing it via the keyword
this or using it as an argument via the keyword it.
Each scope function takes a lambda expression and returns either the object or the result of the lambda expression.
 */

fun sendNotification(recipientAddress: String): String {
    println("Yo $recipientAddress!")
    return "Notification sent!"
}

class Client() {
    var token: String? = null
    fun connect() = println("connected!")
    fun authenticate() = println("authenticated!")
    fun getData() : String {
        println("getting data!")
        return "Mock data"
    }
}

class Canvas {
    fun rect(x: Int, y: Int, w: Int, h: Int): Unit = println("$x, $y, $w, $h")
    fun circ(x: Int, y: Int, rad: Int): Unit = println("$x, $y, $rad")
    fun text(x: Int, y: Int, str: String): Unit = println("$x, $y, $str")
}

fun main() {

    // Let scope function
    // Use the let scope function when you want to perform null checks in your code and later perform further actions with the returned object.
    println("Using let scope function:")
    val address: String? = "Alice@example.com"
    val confirmation = address?.let {
        println("The length of the name is ${it.length}")
        sendNotification(it)
    }

    println(confirmation)

    // Apply scope function
    // Use the apply scope function to initialize objects, like a class instance, at the time of creation rather than later on in your code.
    println("\nUsing apply scope function:")
    val client = Client().apply {
        token = "asdf"
        connect()         // connected!
        authenticate()        // authenticated!
    }
    client.getData()

    // Run scope function
    // Use the run scope function when you want to execute a block of code where you need to access the context of an object and return a result.
    println("\nUsing run scope function:")
    val client1: Client = Client().apply {
        token = "asdf"
    }
    val result: String = client1.run {
        connect()        // connected!
        authenticate()         // authenticated!
        getData()        // getting data!
    }
    println(result)

    // Also scope function
    // Use the also scope function to complete an additional action with an object and then return the object to continue
    // using it in your code, like writing a log.
    println("\nUsing also scope function:")
    val medals: List<String> = listOf("Gold", "Silver", "Bronze")
    val reversedLongUppercaseMedals: List<String> =
        medals
            .map { it.uppercase() }
            .also { println("Uppercased medals: $it") }
            .filter { it.length > 4 }
            .also(::println)
            .reversed()
    println(reversedLongUppercaseMedals)

    // With scope function
    // Unlike the other scope functions, with is not an extension function, so the syntax is different. You pass the receiver object to with as an argument.
    //Use the with scope function when you want to call multiple functions on an object.
    println("\nUsing with scope function:")
    val mainMonitorSecondaryBufferBackedCanvas = Canvas()
    with(mainMonitorSecondaryBufferBackedCanvas) {
        text(10, 10, "Foo")
        rect(20, 30, 100, 50)
        circ(40, 60, 25)
        text(15, 45, "Hello")
        rect(70, 80, 150, 100)
        circ(90, 110, 40)
        text(35, 55, "World")
        rect(120, 140, 200, 75)
        circ(160, 180, 55)
        text(50, 70, "Kotlin")
    }
}