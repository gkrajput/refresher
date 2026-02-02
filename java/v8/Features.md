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




### 3. **Streams API**

Streams API provides a functional, declarative way to process collections, arrays, and I/O data.

**📌 What is a Stream?**

A Stream is a sequence of data elements supporting sequential and parallel operations.

* Not a data structure
* Does **not** store elements
* Does **not** modify the underlying collection
* Operations can be lazy, pipelined, and parallelizable

Analogy: Think of a stream as a conveyor belt on which elements flow and operations process them.

**📌 Stream Pipeline**

A pipeline consists of three parts: Source, Intermediate Operations, and Terminal Operation.

1. Source:
```java
// Source
List<Integer> list = Arrays.asList(1, 2, 3);
Stream<Integer> stream = list.stream();
```

2. Intermediate Operations (return a stream):

* Lazy evaluation
* Don't execute until a terminal operation is called
* Examples: map(), filter(), sorted(), distinct(), limit(), skip()

3. Terminal Operations (produce a result):

* Triggers execution
* Returns non-stream value
* Examples: collect(), forEach(), reduce(), count(), findFirst(), findAny()


**Example:**

```java
List<String> result = list.stream()
                         .filter(s -> s.startsWith("A"))
                         .collect(Collectors.toList());

// Sorting employees by salary
employees.stream()
        .sorted(Comparator.comparing(Employee::getSalary))
        .toList();

// Paginating a list
List<Integer> page2 = nums.stream()
        .skip(5)
        .limit(5)
        .toList();

// Creating a Map from a List
Map<String, Integer> map = employees.stream()
        .collect(Collectors.toMap(
                Employee::getName,
                Employee::getSalary
        ));

// Reducing to a single value
int sum = nums.stream()
        .reduce(0, (a, b) -> a + b);

// Finding the top 3 highest paid employees
List<Employee> top3 = employees.stream()
        .sorted(Comparator.comparing(Employee::getSalary).reversed())
        .limit(3)
        .toList();

// Counting employees in each department
Map<String, Long> count =
        employees.stream().collect(
                Collectors.groupingBy(Employee::getDepartment, Collectors.counting())
        );

// Parsing CSV string to List
String csv = "apple, banana, cherry, date";
List<String> list = Arrays.stream(csv.split(","))
        .map(String::trim)
        .toList();

// Sum of squares of even numbers
int result = nums.stream()
        .filter(n -> n % 2 == 0)
        .map(n -> n * n)
        .reduce(0, Integer::sum);
```



### 4. **Default Methods in Interfaces**

Interfaces can now have default method implementations.

```java
default void log(String message) {
    System.out.println("Log: " + message);
}
```

### 5. **Method References**

Short-hand notation for lambda expressions.

```java
list.forEach(System.out::println);
```

### 6. **Optional Class**

A container object to avoid `NullPointerException`.

```java
Optional<String> name = Optional.of("John");
name.ifPresent(System.out::println);
```

### 7. **New Date & Time API (java.time)**

A modern, improved date/time API inspired by Joda-Time.

```java
LocalDate today = LocalDate.now();
LocalDate nextWeek = today.plusWeeks(1);
```

### 8. **Nashorn JavaScript Engine**

A lightweight JavaScript engine built into the JDK (later removed in newer versions).

### 9. **Parallel Streams**

Allows parallel execution of stream operations.

```java
list.parallelStream().forEach(System.out::println);
```

### 10. **Collectors API Enhancements**

Powerful utilities for collection transformations.

```java
Map<String, Long> countByName = list.stream()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
```

---

## Java 7 vs Java 8 Comparison

| Feature           | Java 7                              | Java 8                       |
| ----------------- | ----------------------------------- | ---------------------------- |
| Programming Style | Imperative                          | Functional + Imperative      |
| Lambda Support    | ❌ No                                | ✅ Yes                        |
| Stream API        | ❌ No                                | ✅ Yes                        |
| Date/Time API     | Old `java.util.Date` and `Calendar` | Modern `java.time` package   |
| Default Methods   | ❌ No                                | ✅ Yes                        |
| Method References | ❌ No                                | ✅ Yes                        |
| Optional Class    | ❌ No                                | ✅ Yes                        |
| Performance       | Good                                | Better with parallel streams |
| Nashorn JS Engine | ❌ No                                | ✅ Yes                        |

### Summary

Java 7 focused on stability and small improvements, while Java 8 brought a major paradigm shift by introducing functional programming, simplifying collections, and modernizing the API ecosystem.

---

## Java 8 Interview Questions

### 1. What are lambda expressions and why were they introduced?

Lambda expressions enable functional programming and help write cleaner, more concise code, especially for callbacks and collection operations.

### 2. What is the difference between a lambda expression and an anonymous class?

* Lambdas are lighter, faster, and focus on behavior.
* Anonymous classes create separate class files and are more verbose.

### 3. What is a functional interface? Give examples.

An interface with exactly one abstract method. Examples: `Runnable`, `Callable`, `Supplier`, `Function`.

### 4. What are streams in Java 8?

A pipeline-based API for processing data in a declarative way. Supports operations like map, filter, reduce.

### 5. Difference between intermediate and terminal stream operations?

* Intermediate: return a new stream (lazy). Example: `filter()`, `map()`
* Terminal: produce a result. Example: `collect()`, `count()`

### 6. What is Optional and why is it used?

A container object to prevent NullPointerException and encourage explicit null handling.

### 7. What are default methods in interfaces?

Methods with implementation inside interfaces. Allow backward compatibility.

### 8. What are method references?

Shortcuts to lambda expressions using `::`. Example: `System.out::println`.

### 9. What is the difference between Collection.stream() and Collection.parallelStream()?

* `stream()`: sequential execution
* `parallelStream()`: parallel execution using ForkJoinPool

### 10. Explain map() vs flatMap().

* `map()`: transforms each element
* `flatMap()`: flattens nested structures and then transforms

---

## Why Java 8 Was a Big Deal

* Introduced functional programming to Java
* Simplified collection processing
* Eliminated many common bugs (like null pointer issues)
* Improved performance with parallelism
* Standardized date & time handling

