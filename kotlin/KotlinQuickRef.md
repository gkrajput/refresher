# Kotlin Quickstart

## 🛠 Basic Syntax Comparison

### Variables & Nullability

| Feature | Java | Kotlin |
| --- | --- | --- |
| **Immutable** | `final String x = "Fixed";` | `val x = "Fixed"` |
| **Mutable** | `String y = "Changeable";` | `var y = "Changeable"` |
| **Null Safe** | `if (user != null) user.get();` | `user?.get()` |
| **Default Val** | `(val != null) ? val : "Def";` | `val ?: "Def"` (Elvis) |

### Classes & Data

**Java (POJO):**

```java
public class User {
    private final String name;
    public User(String name) { this.name = name; }
    public String getName() { return name; }
    // + equals, hashCode, toString...
}

```

**Kotlin (Data Class):**

```kotlin
data class User(val name: String) 
// Automatically generates getters, equals, hashCode, and toString.

```

**Singleton**

Java: `public static final MyService INSTANCE...`
Kotlin: `object MyService { ... }`

---

## 🏗 Control Flow

### When Expression (Replaces Switch)

`when` is more powerful than `switch` and can be used as an expression to return values.

```kotlin
val result = when (status) {
    200, 201 -> "Success"
    in 400..499 -> "Client Error"
    is String -> "Status is a String"
    else -> "Unknown"
}

```

### Smart Casts

No need to manually cast after checking a type.

```kotlin
if (obj is String) {
    println(obj.length) // obj is automatically treated as String here
}

```

---


## 🧭 Scope Functions

Scope functions allow to execute a block of code within the context of an object. 
They make the code more concise, improve readability, and help manage variable scoping and null-safety without the boilerplate of traditional Java if blocks.

### apply (The Configurator)

Commonly used for object initialization or builder-style patterns.

```kotlin
// Configuring a Database Entity
val user = User().apply {
    firstName = "John"  // same as this.firstName = ...
    lastName = "Doe"
    status = Status.ACTIVE
}
```

### also (The Interceptor)

Use this when you want to perform an action that doesn't change the object, like logging a result before returning it.

```kotlin
fun saveUser(user: User): User {
    return repository.save(user).also {
        logger.info("User saved with ID: ${it.id}")
    }
}
```

### let (The Transformer)

Replaces the `if (obj != null)` pattern. It safely executes the block only if the object exists.

```kotlin
// it is the non-null version of the nullable response
val result = serviceResponse?.let {
    transformToDto(it)
} ?: throw Exception("Empty Response")
```

### run (The Processor)

Use run when you have an object and you need to perform a complex initialization or calculation to produce a **new result**. 
It combines the power of `let` (returning a result) and `apply` (using `this`).

```kotlin
val userSession = sessionRepository.get()

// We 'run' logic on the session to compute a sensitive encrypted token
val authToken = userSession.run {
    // 'this' is the userSession
    val rawData = "$userId:$timestamp:$secretKey"
    encryptionService.encrypt(rawData) // This last line is what gets returned
}
```

### with (The Grouper)

Use `with` when you already have a non-null object and you need to call many different methods on it to build something else. 
It acts like a "context wrapper." It makes the mapping logic look like a configuration block rather than a series of repetitive getter calls.

```kotlin
val legacyUser = javaUserRepository.fetchOldUser()

// Instead of legacyUser.getFirstName(), legacyUser.getLastName()...
val userProfile = with(legacyUser) {
    UserProfileDto(
        fullName = "$firstName $lastName",
        contact = email ?: phone,
        isVerified = accountStatus == "VERIFIED",
        accessLevel = calculateAccess(permissionBitmask)
    )
}
```


### 🏗️Building pipelines

```kotlin
return repository.findById(id)
    ?.let { mapToDto(it) }             // 1. Transform if exists
    ?.apply { lastAccessed = now() }   // 2. Update a property
    ?.also { logger.debug("DTO: $it") }// 3. Log it
    ?: throw NotFoundException()       // 4. Handle missing
```

```kotlin
fun processTransfer(request: TransferRequest?): Receipt {
    // 1. 'let' - Handle the nullable request and transform it
    // If request is null, the whole block is skipped
    val transactionId = request?.let {
        validateRequest(it)
        it.id ?: "TEMP_${System.currentTimeMillis()}"
    } ?: throw IllegalArgumentException("Request cannot be null")

    // 2. 'with' - Group calls on a specific object (the request) 
    // to build a different object (the transaction)
    val transaction = with(request) {
        Transaction(
            id = transactionId,
            amount = amount,
            currency = currency
        )
    }

    // 3. 'apply' - Initialize/Configure the transaction object
    // 'this' is the Transaction. We return the Transaction itself.
    transaction.apply {
        status = "PENDING"
        timestamp = LocalDateTime.now()
    }

    // 4. 'also' - Perform a side effect (logging) without changing the object
    // Returns the transaction object to be stored in 'finalResult'
    val finalResult = repository.save(transaction).also {
        logger.info("Transaction ${it.id} saved to DB with status ${it.status}")
    }

    // 5. 'run' - Execute a block of logic and return a final computation (the Receipt)
    return finalResult.run {
        Receipt(
            transactionId = id,
            confirmationCode = "CONF-${id.takeLast(4)}"
        )
    }
}
```


### 🔍Decision Matrix

Use this table to choose the right function:

|Function |Context Object | Return Value	    | Meaning                                                 |
|---------|---------------|--------------------|---------------------------------------------------------|
|apply	|this	| Object itself	    |"Initialize/Config this object and give it back to me." |
|also	|it	    |Object itself	    |"Save this object, but also do this side-task (like log it) first."|
|let	|it	    |Lambda result	    |"If this is not null, turn it into something else (Map/Transform)."|
|run	|this	|Lambda result	    |"Take this object, use its properties to compute a final value."|
|with	|this	|Lambda result	    |"Using this object's members, build or do the following..."|

---

## ⚠️ Common "Gotchas"

### Checked Exceptions

Kotlin ignores checked exceptions. You don't have to wrap everything in try-catch or add throws to your method signatures. 

### Public by Default
In Java, the default visibility is **package-private**. In Kotlin, it is **public**.

### No Primitive Types

You will use `Int`, `Double`, and `Boolean`. The compiler handles the conversion to `int`, `double`, etc., automatically.

### Semicolons: 

They are optional :)


---

## 🤝 Interop Best Practices

### 1. Annotate your Java!

To help the Kotlin compiler, add these to existing Java methods:

* `@NotNull`: Kotlin treats this as a non-optional type.
* `@Nullable`: Kotlin treats this as `Type?` (must handle null).

### 2. Calling Kotlin from Java

* **Statics:** Kotlin does not have a `static` keyword. To create "static-like" methods, you use a `companion object` inside the class.
```kotlin
class Calculator {
    companion object {
        @JvmStatic 
        fun add(a: Int, b: Int): Int = a + b
    }
}
```
```java
// Without @JvmStatic: Calculator.Companion.add(1, 2);
// With @JvmStatic:
Calculator.add(1, 2);
```

* **Fields:** Use `@JvmField` on Kotlin properties if you want to bypass getters/setters in Java performance-critical loops.

```kotlin
class MyData {
    @JvmField
    var price: Double = 0.0 // Java can now access this as a direct field: data.price
}
```

### 3. Calling Java from Kotlin

```java
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LegacyUserService {
    @NotNull
    public String getRequiredId() { return "123"; }

    @Nullable
    public String getOptionalMiddleName() { return null; }
}
```

```kotlin
val service = LegacyUserService()

val id: String = service.requiredId // Kotlin knows this is safe
val name: String? = service.optionalMiddleName // Kotlin forces you to handle null
```


### 4. Collection Safety

| Java Collection | Kotlin View |
| --- | --- |
| `List<T>` | `MutableList<T>` (Java lists are always mutable) |
| `Collections.unmodifiableList` | `List<T>` (Read-only view) |


### 5. Avoiding the "Java-in-Kotlin" Antipattern

Don't just write Java code with Kotlin syntax.

* **Avoid** `!!`: If you see the "double-bang" operator (which forces a non-null check), it’s a sign that the logic needs rethinking.
* **Use `val` by default**: Avoid `var` (mutable variables) unless absolutely necessary.
* **Extension Functions:** Use these to add functionality to legacy Java classes without modifying them. This is the **killer feature**.

```kotlin
// Add a method to the standard String class
fun String.isEmail(): Boolean {
    return this.contains("@")
}

// Usage
val myEmail = "test@company.com"
if (myEmail.isEmail()) { ... }
```

---

## 🎯 Unit test

Use **JUnit 5** alongside **MockK** (the Kotlin-native equivalent to **Mockito**). MockK is preferred over Mockito for Kotlin because it handles Kotlin’s "Final by default" classes and Coroutines much better.

### Dependencies

```kotlin
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.assertj:assertj-core:3.24.2") // Or use Kotlin's 'kotlin.test'
}
```

### Sample practice.Test

Here is how you would test a service. 
Notice the use of **Backticks** for method names—this allows you to write test names as full sentences.

```kotlin
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.practice.Test
import org.assertj.core.api.Assertions.assertThat

class UserServiceTest {

    // 1. Create a mock for the Java or Kotlin dependency
    private val userRepository = mockk<UserRepository>()
    private val userService = UserService(userRepository)

    @practice.Test
    fun `should return user when id exists in database`() {
        // GIVEN
        val stubUser = User(id = "123", name = "NCS")
        every { userRepository.findById("123") } returns stubUser

        // WHEN
        val result = userService.getUserDetails("123")

        // THEN
        assertThat(result.name).isEqualTo("NCS")
        verify(exactly = 1) { userRepository.findById("123") }
    }

    @practice.Test
    fun `should throw exception when user is not found`() {
        // GIVEN
        every { userRepository.findById(any()) } returns null

        // WHEN / THEN
        org.junit.jupiter.api.assertThrows<UserNotFoundException> {
            userService.getUserDetails("unknown")
        }
    }
}
```

---

## 🚀 Performance Tips

* **Use Primitive Arrays:** For loops, use `IntArray`, `DoubleArray`, or `LongArray` to avoid "Boxing" overhead.
* **Inline Functions:** Use `inline` for utility functions that take lambdas to avoid creating extra function objects.

```kotlin
// This will have zero overhead compared to a manual 'for' loop
inline fun performCalculation(data: IntArray, action: (Int) -> Unit) {
    for (element in data) action(element)
}
```

---

## 💡 Pro-Tips

1. **IntelliJ Converter:** `Ctrl + Alt + Shift + K` converts any Java file to Kotlin.
2. **Paste Java:** Copy Java code and paste it into a `.kt` file; the IDE will auto-convert it for you.
3. **No Semicolons:** You don't need them anymore!

---

[Learn More](https://kotlinlang.org/docs/kotlin-tour-welcome.html)