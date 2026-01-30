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

**📌 Collectors — Very Important**

Collectors are used to accumulate elements into collections or other data structures.

```java
List<String> names = stream.collect(Collectors.toList());

// Join strings with delimiter
String res = names.stream()
        .collect(Collectors.joining(", "));

// Grouping by department
Map<String, List<Employee>> byDept = employees.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment));

// Partitioning even and odd numbers
Map<Boolean, List<Integer>> evenOdd =
        nums.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));

// Mapping salaries by department
Map<String, List<Integer>> salaries =
        employees.stream().collect(
                Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(Employee::getSalary, Collectors.toList())
                )
        );
```

**📌 Parallel Streams**

Used for data that can be processed independently.

* Do NOT use with shared mutable state
* Overhead may make sequential faster
* Great for CPU-heavy computations

⚠ But parallel streams are NOT always faster.

They are best when:

* Data set is large
* CPU-bound operations
* No shared mutable state
* Multi-core CPU available
* No complex pipeline overhead

```java
nums.parallelStream()
    .map(n -> n * 2)
    .forEach(System.out::println);
```

**📌 Infinite Streams**

Created using `Stream.generate()` or `Stream.iterate()`.

```java
Stream.iterate(0, n -> n + 1)
        .limit(10)
        .forEach(System.out::println);

Stream.generate(Math::random)
        .limit(5)
        .forEach(System.out::println);
```

**📌 Short-Circuiting Operations**

These can stop early:

* limit()
* findFirst()
* anyMatch()
* noneMatch()

**📌 Primitive Streams**

More efficient versions: IntStream, LongStream, DoubleStream

```java
int sum = IntStream.range(1, 5).sum();
```

**❌Limitations:**

* Cannot modify the source during processing
* Streams can be consumed only once
* Debugging can be tricky due to lazy evaluation
* Avoid modifying external variables inside streams
* Don’t use parallel streams with Shared Mutability

**Exception Handling**

Handling checked exceptions in lambdas within streams can be tricky. You can wrap the lambda in a try-catch block or create a utility method to handle exceptions.

```java
list.stream().forEach(item -> {
    try {
        // code that may throw checked exception
    } catch (Exception e) {
        // handle exception
    }
});
```

**Streams vs Loops**

For many cases: **Loops are slightly faster** because:

* no lambda overhead
* no creation of pipeline objects
* no boxing/unboxing (loops can use primitive vars)

But the difference is often micro-seconds and negligible.

| Aspect               | Streams                                | Loops                                   |
|----------------------|----------------------------------------|-----------------------------------------|
| Style                | Declarative (“What to do”), functional | Imperative (“How to do it”), procedural |
| Readability          | Higher for complex operations          | Can become cluttered with nested loops  |
| Performance          | Can be optimized, parallelized         | Faster, generally sequential            |
| Mutability           | Encourages immutability                | Often involves mutable state            |
| Debugging            | Can be harder due to lazy evaluation. Must use peek() or logs | Easier to step through                  |
| Parallelism          | Built-in support with parallelStream() | Requires manual thread management       |
| Memory Usage        | May use more memory for intermediate objects | Generally lower memory footprint        |
| GC Overhead         | More frequent GC due to temporary objects | Less frequent GC                        |

✔ Use Streams when:

* You want concise, readable code
* Transforming/processing data
* Filtering, mapping, grouping
* Code readability matters
* You want immutability
* You want easy parallelism
* Working with collections or I/O
* You need short-circuiting (anyMatch etc.)

✔ Use Loops When:

* Mutating a data structure
* Performance is critical
* You need index-based access
* You need break/continue logic
* You need checked exceptions
* You must reuse iteration logic
* You're dealing with small/critical code paths

**🎯Questions**

**1. Can streams be reused? Why not?**

Stream is a consumable, one-time-use pipeline. A stream represents:

* A pipeline of computations
* That can be executed only one time
* And is closed after its terminal operation

Once a terminal operation runs (e.g., forEach, collect, reduce), the underlying stream is marked consumed.

Trying to reuse it throws: `IllegalStateException: stream has already been operated upon or closed`

Why not?

Streams use internal iterators, not external ones. When the pipeline executes:

* It binds the data source
* Iterates through it
* Uses spliterators internally
* And invalidates the pipeline afterward

Reusing would require rebuilding the entire pipeline, which violates the stream design: immutable, functional, non-replayable data flow

**2. When should parallel streams be avoided?**

1. Tasks involve shared mutable state
```java
list.parallelStream().forEach(n -> sharedList.add(n));  // ❌ unsafe
```

2. Data set is small (overhead outweighs benefits)
3. Operations are I/O-bound rather than CPU-bound
   
    Parallel streams work best for heavy CPU computations, not:
   * DB calls
   * File operations
   * Network calls

    These block threads → parallelism wasted.
4. Order matters and ordering is expensive. Example: `sorted().parallel()` can be slower than sequential.
5. You are in a server environment

    Parallel streams use `ForkJoinPool.commonPool()`, which:
        * Affects all parallel stream operations across your app
        * Can lead to thread starvation in server apps
        * Interferes with other parallel tasks makes performance unpredictable
6. Complex pipelines with many intermediate operations 
7. The operations are not independent. Parallelism requires pure functions (no side effects).
8. You need deterministic results. Parallelism may change: Order of execution, Timing, Thread usage

**3. Why do Streams cause boxing/unboxing overhead?**

Stream API operates on objects, not primitives. So `Stream<Integer>` involves:

* Integer → int (unboxing)
* int → Integer (boxing)

This has CPU cost + GC pressure. Java collections store objects, not primitives. Streams over a collection therefore produce Stream<T> = object stream.

Primitive streams (**IntStream**, **LongStream**…) were added to avoid boxing:

* They have specialized methods (sum, average, max)
* They use primitive arrays internally
* They reduce GC and CPU overhead

```java
Stream<Integer> s = List.of(1, 2, 3).stream();

int sum = s.map(n -> n * 2)   // n is Integer → needs unboxing
          .reduce(0, Integer::sum);  // arguments boxed/unboxed

// Optimized
int sum = IntStream.of(1, 2, 3)
        .map(n -> n * 2)
        .sum();
```

**4. Why not use parallel streams with shared mutable state?**

Parallel streams run your operations on multiple threads simultaneously (from the ForkJoinPool).
If these threads modify the same shared object, you get:

* Race conditions
* Data corruption
* Unexpected results
* Lost updates
* Thread-safety issues

Parallel streams assume your operations are stateless and side-effect-free.

Shared Mutable State:

* Shared → accessed by multiple threads
* Mutable → can be changed
* State → variables, lists, maps, counters, objects

If each thread modifies the same object:

* No synchronization
* No ordering
* No visibility guarantee
* No atomicity

So state gets corrupted.

Parallel streams=multiple threads → touching the same variable at the same time → unpredictability.

Examples:

```java
List<Integer> result = new ArrayList<>(); // shared mutable state

numbers.parallelStream()
       .forEach(n -> result.add(n));    // ❌ unsafe
```

🔥 Problem

* Multiple threads call add() simultaneously
* ArrayList is not thread-safe
* Internal array resizing can corrupt the array
* Final list may contain missing or duplicate values
* Program may even crash

```java
int[] counter = {0};  // shared mutable array

list.parallelStream()
    .forEach(e -> counter[0]++);  // ❌ race condition
```

🔥 Problem

counter[0]++ is not atomic (read → increment → write). Threads overwrite each other → incorrect final count.

```java
class Stats {
    int sum = 0;
}

Stats stats = new Stats(); // shared mutable

list.parallelStream()
    .forEach(n -> stats.sum += n);  // ❌ unsafe
```

🔥 Problem

Many threads writing to sum simultaneously → corrupted results.

```java
// Use concurrent collectors
Map<String, Long> map =
        list.parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        s -> s,
                        Collectors.counting()
                ));
```

**5. What are Internal Iterators?**

Internal iterators mean that the iteration logic is controlled by the API, not by you.

In simple words:

    👉 The library decides how and when to iterate over the elements.
    👉 You only describe what to do, not how to loop.

External Iterator (loop): 
You drive the car → control steering, brakes, acceleration.

Internal Iterator (stream): 
You sit in a cab → the driver (API) takes you where you need.




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

