package practice

import kotlin.random.Random
import kotlin.properties.Delegates.observable

// A class in Kotlin is defined using the `class` keyword followed by the class name and a pair of parentheses.
// The primary constructor is defined within the parentheses, and it can have parameters that are used to initialize the properties of the class.
// The properties can be declared using `val` (for read-only properties) or `var` (for mutable properties).
class Contact(val id: Int, var email: String)

// We can also define properties with default values and initialize them in the class body.
class Contact1(val id: Int, var email: String) {
    val category: String = ""
}

// We can also provide default values for the constructor parameters, which allows us to create instances of the class without providing all the arguments.
class Contact2(val id: Int, var email: String = "example@gmail.com") {
    val category: String = "work"
}

// We can also define member functions inside the class to perform operations related to the class.
class Contact3(val id: Int, var email: String = "example@gmail.com") {
    fun printInfo() {
        println("Contact3: id=$id, email=$email")
    }
}

// Data Classes
// A data class is a special type of class that is used to hold data. It automatically generates useful methods
// like `toString()`, `equals()`, `hashCode()`, and `copy()` based on the properties defined in the primary constructor.
data class ContactData(val id: Int, var email: String)

data class Name(val firstName: String, val lastName: String)
data class City(val name: String, val country: String)
data class Address(val street: String, val city: City)
data class Person(val name: Name, val address: Address, val ownsAPet: Boolean = true)

data class Employee(var name: String, var salary: Double)
class RandomEmployeeGenerator(var minSalary: Double, var maxSalary: Double) {
    val potentialNames = listOf("John", "Jane", "Mary", "Michael", "Sarah")

    fun generateEmployee(): Employee {
        val name = potentialNames.random()
        val salary = Random.nextDouble(minSalary, maxSalary)
        return Employee(name, salary)
    }
}


// Class inheritance
// At the top of Kotlin's class hierarchy is the common parent class: Any. All classes ultimately inherit from the Any class
// and thus have access to its methods, such as `toString()`, `equals()`, and `hashCode()`.

// Abstract classes
// If you want to use inheritance to share some code between classes, first consider using abstract classes. Abstract classes
// can contain both functions and properties with implementation as well as functions and properties without implementation,
// known as abstract functions and properties. Use abstract keyword to declare an abstract class and its members.
// Use override keyword to implement the abstract members in the subclass.

abstract class Product(val name: String, var price: Double) {
    // Abstract property for the product category
    abstract val category: String

    // A function that can be shared by all products
    fun productInfo(): String {
        return "Product: $name, Category: $category, Price: $price"
    }
}

class Electronic(name: String, price: Double, val warranty: Int) : Product(name, price) {
    override val category = "Electronic"
}

// If you need to inherit from multiple sources, consider using interfaces. Interfaces support multiple inheritance so
// a class can implement multiple interfaces at once.

interface PaymentMethod {
    // Functions are inheritable by default
    fun initiatePayment(amount: Double): String
}

interface PaymentType {
    val paymentType: String
}

class CreditCardPayment(val cardNumber: String, val cardHolderName: String, val expiryDate: String) : PaymentMethod {
    override fun initiatePayment(amount: Double): String {
        // Simulate processing payment with credit card
        return "Payment of $$amount initiated using Credit Card ending in ${cardNumber.takeLast(4)}."
    }
}

class CreditCardPayment1(val cardNumber: String, val cardHolderName: String, val expiryDate: String) : PaymentMethod, PaymentType {
    override fun initiatePayment(amount: Double): String {
        // Simulate processing payment with credit card
        return "Payment of $$amount initiated using Credit Card ending in ${cardNumber.takeLast(4)}."
    }

    override val paymentType: String = "Credit Card"
}

// Object Declarations
// An object declaration defines a singleton, which is a class that has only one instance. The object is initialized
// lazily when it is accessed for the first time, and the same instance is used throughout the program.
// Object declarations are useful for creating utility classes, managing shared state, or implementing the singleton pattern.
object DoAuth {
    fun takeParams(username: String, password: String) {
        println("input Auth parameters = $username:$password")
    }
}

// Data Objects
// A data object is a special type of object declaration that is used to hold data. It automatically generates useful
// methods like `toString()`, `equals()`, and `hashCode()` based on the properties defined in the object declaration.
// Data objects are useful for representing simple data structures or constants that do not require any behavior or state management.
data object AppConfig {
    var appName: String = "My Application"
    var version: String = "1.0.0"
}

// Companion Objects
// A companion object is a special type of object declaration that is defined inside a class. It allows you to define
// members that are associated with the class rather than with instances of the class. Companion objects are useful for
// defining factory methods, constants, or utility functions that are related to the class but do not require an instance
// of the class to be accessed. You can only have one companion object per class. A companion object is created only
// when its class is referenced for the first time. A companion object doesn't have to have a name. If you don't define
// one, the default is Companion.
class BigBen {
    companion object Bonger {
        fun getBongs(nTimes: Int) {
            repeat(nTimes) { print("BONG ") }
        }
    }
}

// Open classes
// By default, all classes in Kotlin are final, which means they cannot be inherited. If you want to allow a class to be
// inherited, you need to mark it with the open keyword. This allows other classes to inherit from it and override its members.
open class Vehicle(val make: String, val model: String) {
    open fun displayInfo() {
        println("Vehicle Info: Make - $make, Model - $model")
    }
}

open class Car(make: String, model: String, val numberOfDoors: Int) : Vehicle(make, model) {
    override fun displayInfo() {
        println("Car Info: Make - $make, Model - $model, Number of Doors - $numberOfDoors")
    }
}

interface EcoFriendly {
    val emissionLevel: String
}

interface ElectricVehicle {
    val batteryCapacity: Double
}

// New class that inherits from Car and implements two interfaces
class ElectricCar(
    make: String,
    model: String,
    numberOfDoors: Int,
    val capacity: Double,
    val emission: String
) : Car(make, model, numberOfDoors), EcoFriendly, ElectricVehicle {
    override val batteryCapacity: Double = capacity
    override val emissionLevel: String = emission
}

// Sealed classes
// A sealed class is a special type of class that restricts the inheritance hierarchy. It allows you to define a closed
// set of subclasses, which can be useful for representing a limited number of types or states. Sealed classes are useful
// for modeling algebraic data types, where you want to represent a value that can be one of several different types,
// but you want to ensure that all possible types are known at compile time. Sealed classes are declared using the
// sealed keyword, and their subclasses must be defined within the same package.
sealed class Mammal(val name: String)

class Cat(val catName: String) : Mammal(catName)
class Human(val humanName: String, val job: String) : Mammal(humanName)

fun greetMammal(mammal: Mammal): String {
    when (mammal) {
        is Human -> return "Hello ${mammal.name}; You're working as a ${mammal.job}"
        is Cat -> return "Hello ${mammal.name}"
    }
}

// Enum classes
// An enum class is a special type of class that represents a fixed set of constants. Each constant is an instance of
// the enum class, and you can define properties and functions for each constant. Enum classes are useful for
// representing a limited number of options or states, such as days of the week or directions.
enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF),
    YELLOW(0xFFFF00);

    fun containsRed() = (this.rgb and 0xFF0000 != 0)
}

// Inline Value Classes
// An inline value class is a special type of class that wraps a single value and is optimized for performance. It is
// defined using the value keyword and can only have one property. Inline value classes are useful for representing
// simple types, such as a wrapper around a primitive type or a type that has a single property, without the overhead
// of creating an additional object. Inline value classes are inlined at compile time, which means that they do not have
// the overhead of object allocation and can be more efficient than regular classes for certain use cases.
@JvmInline
value class Email(val address: String)

fun sendEmail(email: Email) {
    println("Sending email to ${email.address}")
}

// Backing Fields
// In Kotlin, properties can have custom getters and setters that allow you to control how the property is accessed and
// modified. When you define a custom getter or setter for a property, you can use a backing field to store the actual
// value of the property. The backing field is a hidden field that is automatically generated by the compiler and can be
// accessed using the field identifier within the getter and setter.

// this code has the category property that has no custom get() or set() functions and therefore uses the default implementations:
class ContactG(val id: Int, var email: String) {
    var category: String = ""
}
/*
Under the hood, this is equivalent to this pseudocode:
class ContactG(val id: Int, var email: String) {
    var category: String = ""
        get() = field
        set(value) {
            field = value
        }
}
*/
class PersonG {
    var name: String = ""
        set(value) {
            field = value.replaceFirstChar { firstChar -> firstChar.uppercase() }
        }
}

// Extension properties
// Extension properties allow you to add new properties to existing classes without modifying their source code.
data class PersonG1(val firstName: String, val lastName: String)

// Extension property to get the full name
val PersonG1.fullName: String
    get() = "$firstName $lastName"

// Observable properties
// Observable properties allow you to define a property that automatically notifies listeners when its value changes.
class Thermostat {
    var temperature: Double by observable(20.0) { _, old, new ->
        if (new > 25) {
            println("Warning: Temperature is too high! ($old°C -> $new°C)")
        } else {
            println("Temperature updated: $old°C -> $new°C")
        }
    }
}

// is and !is operators
// The is operator is used to check if an object is of a specific type, while the !is operator is used to check if an
// object is not of a specific type.
fun printObjectType(obj: Any) {
    when (obj) {
        is Int -> println("It's an Integer with value $obj")
        !is Double -> println("It's NOT a Double")
        else -> println("Unknown type")
    }
}

// as and as? operators
// The as operator is used to perform an explicit type cast, while the as? operator is used to perform a safe type cast
// that returns null if the cast is not possible.
fun castToString(obj: Any) {
    val str1: String = obj as String // This will throw an exception if obj is not a String
    println("Casted to String: $str1")
}

fun calculateTotalStringLength(items: List<Any>): Int {
    return items.sumOf { (it as? String)?.length ?: 0 }
}

// Early returns using Elvis operator
// The Elvis operator (?:) is used to provide a default value when an expression evaluates to null. It can be used for
// early returns in functions when a required value is missing.
data class User(
    val id: Int,
    val name: String,
    // List of friend user IDs
    val friends: List<Int>
)
fun getNumberOfFriends(users: Map<Int, User>, userId: Int): Int {
    // Retrieves the user or return -1 if not found
    val user = users[userId] ?: return -1
    // Returns the number of friends
    return user.friends.size
}


fun main() {
    val contact = Contact(1, "abc@example.com")
    val contact1 = Contact1(2, "abc@ad.com")
    val contact2 = Contact2(3)

    println("Contact: id=${contact.id}, email=${contact.email}")
    println("Contact1: id=${contact1.id}, email=${contact1.email}, category=${contact1.category}")
    println("Contact2: id=${contact2.id}, email=${contact2.email}, category=${contact2.category}")

    contact2.email = "gk@example.com"
    println("Contact2 Updated: id=${contact2.id}, email=${contact2.email}")

    val contact3 = Contact3(3)
    contact3.printInfo()

    val contactData = ContactData(4, "data@example.com")
    println("ContactData: id=${contactData.id}, email=${contactData.email}")
    println(contactData) // toString() is automatically generated for data classes

    val contactDataCopy = contactData.copy() // copy() is automatically generated for data classes, it creates a new instance with the same property values
    println(contactDataCopy)

    println("Are contactData and contactDataCopy equal? ${contactData == contactDataCopy}") // equals() is automatically generated for data classes

    val contactDataModified = contactData.copy(email = "updated@gmail.com") // We can also create a copy with modified properties
    println(contactDataModified)

    val contactDataCopyModified = contactData.copy(id = 5)
    println(contactDataCopyModified)

    val emp = Employee("Mary", 20.0)
    println(emp)
    emp.salary += 10
    println(emp)

    val person = Person(
        Name("John", "Smith"),
        Address("123 Fake Street", City("Springfield", "US")),
        ownsAPet = false
    )
    println(person)


    val empGen = RandomEmployeeGenerator(10.0, 30.0)
    println(empGen.generateEmployee())
    println(empGen.generateEmployee())
    println(empGen.generateEmployee())
    empGen.minSalary = 50.0
    empGen.maxSalary = 100.0
    println(empGen.generateEmployee())

    val laptop = Electronic(name = "Laptop", price = 1000.0, warranty = 2)
    println(laptop.productInfo())

    val paymentMethod = CreditCardPayment("1234 5678 9012 3456", "John Doe", "12/25")
    println(paymentMethod.initiatePayment(100.0))

    val cardPayment = CreditCardPayment1("1234 5678 9012 3456", "John Doe", "12/25")
    println(cardPayment.initiatePayment(100.0))
    println("Payment is by ${cardPayment.paymentType}")

    DoAuth.takeParams("coding_ninja", "N1njaC0ding!")

    println(AppConfig)
    println(AppConfig.appName)

    BigBen.getBongs(12)

    val car = Car("Toyota", "Corolla", 4)
    println("\nCar Info: Make - ${car.make}, Model - ${car.model}, Number of doors - ${car.numberOfDoors}")
    car.displayInfo()

    println(greetMammal(Cat("Snowy")))

    val red = Color.RED
    println(red.containsRed())
    println(Color.BLUE.containsRed())

    val myEmail = Email("example@example.com")
    sendEmail(myEmail)

    val thermostat = Thermostat()
    thermostat.temperature = 22.5
    thermostat.temperature = 27.0

    val emails: List<String?> = listOf("alice@example.com", null, "bob@example.com", null, "carol@example.com")
    val validEmails = emails.filterNotNull()
    println(validEmails)

    val serverConfig = mapOf(
        "appConfig.json" to "App Configuration",
        "dbConfig.json" to "Database Configuration"
    )
    val requestedFile = "appConfig.json"
    val configFiles = listOfNotNull(serverConfig[requestedFile])
    println(configFiles)

    /*
    The maxOrNull(), minOrNull(), and singleOrNull() functions are designed to be used with collections that don't contain null values.
    Otherwise, you can't tell whether the function couldn't find the desired value or whether it found a null value.
     */
    val temperatures = listOf(15, 18, 21, 21, 19, 17, 16)
    val maxTemperature = temperatures.maxOrNull()
    println("Highest temperature recorded: ${maxTemperature ?: "No data"}")
    val minTemperature = temperatures.minOrNull()
    println("Lowest temperature recorded: ${minTemperature ?: "No data"}")
    val singleHotDay = temperatures.singleOrNull{ it == 30 }
    println("Single hot day with 30 degrees: ${singleHotDay ?: "None"}")
}