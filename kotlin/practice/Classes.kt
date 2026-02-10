package practice

import kotlin.random.Random

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
}