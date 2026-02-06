# Advanced Concepts in Java

> **In high-performance GCP networking, we often use Direct Buffers (ByteBuffer.allocateDirect()). Why is this better than heap-based arrays for network I/O, and what is the danger regarding the OS kernel?"**
> Direct buffers are stored outside the Garbage Collected heap. They allow the OS to perform Zero-Copy I/O (the kernel can read directly from the memory without the JVM copying it to a heap array first). The danger is that they aren't governed by standard Xmx limits. You can have a "Memory Leak" that is invisible to JVM monitoring but triggers a Linux OOM crash.

> **If we are running a low-latency service on GCP Fabric with a 100GB heap, would you choose G1GC or ZGC? Explain the trade-off in terms of 'Stop-the-World' pauses.**
> ZGC (or Shenandoah). For a 100GB heap, G1GC pause times scale with the number of objects. ZGC pauses are sub-millisecond because it performs compacting concurrently while the application threads are running, using "Load Barriers" to ensure thread safety.

> **How do Java 21's Virtual Threads (Project Loom) change the way we think about thread-per-request models in a cloud-native environment compared to traditional Platform Threads?**
> Traditional threads map 1:1 to OS threads (expensive, limited to a few thousand). Virtual threads are "mounted" on OS threads by the JVM. You can run millions of them. If a virtual thread hits a synchronized block or a native call, it can "pin" the OS thread, potentially causing a deadlock in the carrier pool if not handled correctly.

> **When deploying a Java-based microservice to a Serverless environment like GCP Cloud Run, why might we use GraalVM Native Image instead of a standard OpenJDK JIT?**
> * **Cold Start performance:** Standard JVMs take time to "warm up" (JIT compiling hot code). GraalVM uses AOT (Ahead-of-Time) compilation to turn Java into a standalone binary.
> * **The Trade-off:** AOT binaries often have lower peak throughput than a fully warmed-up JIT compiler because they can't optimize code based on real-time execution data.

> **How does a memory leak in a long-running system process affect a shared-tenant cloud environment?**
> In a shared-tenant environment (like a GCP node running multiple containers/pods), a memory leak doesn't just crash one app; it triggers Resource Contention.
> * **The "OOM Killer" Ripple:** If the process is not properly cgroup-limited, the Linux kernel’s OOM (Out Of Memory) killer might wake up. It often picks the process with the highest memory usage to kill—which might be the leaking process, but could also be a critical sidecar or a neighboring "quiet" tenant.
> * **Memory Pressure & Paging:** Before the crash, the OS might start paging to disk (swap). In a cloud environment, disk I/O is often throttled. This causes "Latent Failures" where all tenants on that physical host experience massive latency spikes because the kernel is busy swapping memory instead of processing packets.
