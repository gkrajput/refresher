# Java 8 Features

Java 8 introduced several major enhancements to the Java programming language, focusing on functional programming, improved API usability, and performance optimizations.

## Key Features Introduced in Java 8

### 1. **Lambda Expressions**

Lambda expressions enable functional programming by allowing you to write cleaner and more concise code. A lambda is a syntactic shorthand for implementing a functional interface.

* Treat functions as values
* Write shorter, cleaner code
* Avoid boilerplate (especially anonymous classes)
* Work easily with Streams API
* Pass behavior as an argument

Before Java 8:
```java
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
};
```
📌With Lambda:

```java
Runnable r = () -> System.out.println("Running");
```

**Examples:**

```java
// no parameters
Runnable r = () -> System.out.println("Hello from lambda!");

// 1 parameter
Consumer<String> printer = msg -> System.out.println(msg);
printer.accept("Hello India!");

// multiple parameters
BiFunction<Integer, Integer, Integer> sum = (a, b) -> a + b;
System.out.println(sum.apply(10, 20));

// body with return
Function<Integer, Integer> square = x -> {
    System.out.println("Squaring: " + x);
    return x * x;
};

// comparator example
List<String> list = Arrays.asList("Bob", "Alice", "Charlie");
Collections.sort(list, (s1, s2) -> s1.compareTo(s2));

// with Stream API
List<String> filtered = list.stream()
                            .filter(s -> s.startsWith("A"))
                            .collect(Collectors.toList());

// Predicate
Predicate<Integer> isEven = n -> n % 2 == 0;
System.out.println(isEven.test(4)); // true

// Function
Function<String, Integer> strLength = s -> s.length();
System.out.println(strLength.apply("Hello")); // 5

// Supplier
Supplier<Double> randomValue = () -> Math.random();;
System.out.println(randomValue.get());

// Consumer
Consumer<String> greeter = name -> System.out.println("Hello, " + name);
greeter.accept("World");
```

**❌Limitations:**

* Cannot modify non-final local variables from the enclosing scope.
* Cannot throw checked exceptions unless declared in the functional interface.

**🚫Common Error: “Cannot infer type for lambda expression”**

A lambda expression does NOT have its own type. It borrows its type from the functional interface in context. When the compiler can’t determine that context → you get an ambiguity error.

* The context (target type) provides the parameter types
* Lambda expressions rely on type inference
* If the context is ambiguous → compiler cannot assign meaning to parameters

Error comes up when:

* Multiple functional interfaces could match
* The lambda does not specify parameter types
* The compiler cannot deduce which method the lambda is implementing

**Example:**

Ambiguity:

```java
interface A {
    void test(String s);
}
interface B {
    void test(Object o);
}

var obj = (x) -> System.out.println(x); // ❌ ERROR: Ambiguous
```

The compiler does not know:
* Should x be a String (matching A)?
* Or an Object (matching B)?

Fix

```java
A a = (String x) -> System.out.println(x);
// or
B b = (Object x) -> System.out.println(x);
```

Overloaded Methods Ambiguity:

```java
public void process(Function<String, Integer> fn) { ... }
public void process(Function<Object, Integer> fn) { ... }

process(x -> x.hashCode()); // ❌ ERROR: Ambiguous

// Fix
process((String x) -> x.hashCode());
// or
process((Object x) -> x.hashCode());
// or force overload resolution
process((Function<String, Integer>)(x -> x.hashCode()));
```

**🧠Bottom Line:** 

Lambda expressions depend on context to infer parameter types. If the context is unclear or overloaded → Java reports ambiguity.




### 2. **Functional Interfaces**

Interfaces with **exactly one abstract method**. This single abstract method represents a function (a behavior), which can be supplied using:

* Lambda Expressions
* Method References
* Anonymous Classes

Functional Interfaces enable functional programming. Without functional interfaces → lambdas would not work. 

🧠Key rules:
* Must have exactly one abstract method
* Can have any number of default methods
* Can have any number of static methods
* **Can override methods from Object** (not counted as abstract method)

**📌 Examples of Functional Interfaces**

Built-in Java functional interfaces include:

* Runnable → run()
* Callable → call()
* Comparator → compare()
* Function<T,R> → apply()
* Predicate<T> → test()
* Consumer<T> → accept()
* Supplier<T> → get()

User defined:

```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);

    default void show() {
        System.out.println("Calculator");
    }

    static void info() {
        System.out.println("Static Method");
    }
}
```

Using a lambda with it:

```java
Calculator c = (a, b) -> a + b;
System.out.println(c.add(5, 3)); // 8
```

🎯Overriding methods from Object (does not count)

```java
@FunctionalInterface
interface Vehicle {
    void drive();

    String toString();   // OK, not counted
}
```




