### HashTable && HashMap
  *  **HashTable**
    * extends Dictionary implements HashMap
    * No null keys/values
    * put/get is synchronized
  * **HashMap**
    * extends AbstractMap implements HashMap
    * Permits null keys/values
    * put/get is unsynchronized
    * 4 constructors
    * 基于Map接口实现、允许null键/值、非同步、不保证有序(比如插入的顺序)、也不保证序不随时间变化

### wait(long time), notify(), notifyAll(), sleep()
  * wait() - Causes the current thread to wait until another thread invokes the notify() aor notifyAll() for this object. The current thread must own this object's monitor. __*The thread releases ownership of this monitor and waits until another thread notifies threads waiting on this object's monitor to wake up*__ either through a call to the {@code notify} method or the {@code notifyAll} method. The thread then waits until it can re-obtain ownership of the monitor and resumes execution.

  * notify() - Wakes up a single thread that is waiting on
   this object's monitor. If any threads are waiting on this object, one of them is chosen to be awakened. The choice is arbitrary and occurs at the discretion of the implementation.

  * notifyAll() - Wakes up all threads that are waiting on this object's monitor.

  * sleep() - Causes the currently executing thread to sleep (temporarily cease execution) for the specified number of milliseconds, subject to the precision and accuracy of system timers and schedulers. __*The thread does not lose ownership of any monitors.*__

### JVM内存
  * **线程共享数据区**
    * ***方法区***
      > 存储已被虚拟机加载的类信息（版本、字段、方法、接口等描述信息）、常量、静态变量和即时编译器编译后的代码等数据；

      > 无法满足内存分配需求时也会抛出OutOfMemoryError。

      > 运行时常量池（Runtime Constant Pool）是方法区的一部分，存储编译期生成的各种字面量和符号引用。

    * ***堆***
      > 被所有的线程共享，在虚拟机启动时创建。几乎所有的对象实例与数组都是在堆上创建。

      > 从内存回收的角度看，可以分为老年代和新生代；或者更为细致的划分为Eden空间，From Survivor空间和ToSurvivor空间。

      > 从内存分配的角度看，可能划分出多个线程私有的分配缓冲区（Thread Local Allocation Buffer, TLAB）。

      > 如果堆中没有内存可以分配，并且也无法扩展时，会抛出OutOfMemoryError。

  * **线程隔离的数据区**
    * ***本地方法(Native Method)栈***
      > 为虚拟机用到的本地方法服务。虚拟机规范中对如何实现本地方法栈没有强制规定，因此HotSpot JVM等直接把本栈与虚拟机栈合二为一，因此该区域抛出的异常与Java虚拟机栈相同。

    * ***Java虚拟机栈***
      > 描述Java方法执行的内存模型：每个方法被执行的时候都会同时创建一个栈桢用于存储局部变量表、操作栈、动态链接、方法出口等信息。方法从调用道退出，对应着一个栈桢在Java虚拟机栈中入栈到出栈的过程。

      > 如果线程请求的栈深度超过了JVM所允许的深度，将抛出StackOverflowError异常；如果虚拟机栈可以动态扩展，但是扩展是申请不到足够的内存就会抛出OutOfMemoryError异常。

    * ***程序计数器***
      > 本区域可以看作是当前线程执行的字节码的行号指示器。Java虚拟机的多线程是通过线程轮流切换并分配出机器的执行时间来实现的，在任何确定的时刻，一个处理器（多核处理器的一个内核）只会执行一条线程中的指令。因此，为了切换后能回到正确的位置，每条线程都必须有一个线程私有的程序计数器，各条线程之间的计数器互不影响，独立存储。__*Java虚拟机规范中唯一一个没有规定OutOfMemoryError的情况的区域*__

  * **直接内存（Direct Memory）**
    > NIO基于Channel和Buffer，可以使用Native函数直接访问对外内存，然后通过一个存储在Java堆中DirectByteBuffer对象作为这块内存的引用进行操作。在某些场景中可以显著提高性能，因为避免了在Java堆和Native堆中来回复制数据；

    > 大小受到物理内存大小的限制。

### JVM GC

### JVM调优
  > jvisialvm监控远程主机，需要首先在远程主机上开启jstatd监控服务。

  > 注意：jdk1.5以上的jvm启动时都已默开启JMX，1.4及以下版本的，需要使用启动参数-D显式开启JMX。被监控主机的主机名称需要与主机的ip地址对应。可以hostname查看当前主机名称，如果hostname映射的ip127.0.0.1，或者是其它ip地址，需要修改需要修改为指向本机IP地址。

  > 被监控机器需要显式指定jstatd监控权限。示例如下，创建一个policy文件，名称为`jstatd.all.policy`(随便起)，内容为
`grant codebase "file:${java_home}/../lib/tools.jar" {  
   permission java.security.AllPermission;  
}; ``
。注意${java_home}变量，如果本地没有定义，可以直接替换成JDK的全路径。


  > 启动jstatd服务，该服务是本地监控程序获取远程服务器jvm状态的桥梁。启动命令为`jstatd -J-Djava.security.policy=jstatd.all.policy`。特别注意一下启动改命令的权限即可。
如果系统中有使用root用户启动的服务，改jstatd服务也需要使用root启动。


### JVM栈溢出如何排错
  > 需要判断是StackOverflowError还是OutOfMemoryError。单线程的情况下无论是栈帧太大还是虚拟机栈容量太小，当内存无法分配时都会出现StackOverflowError。

  > 当由于创建线程过多导致的OutOfMemoryError时，在不能减少线程数或者更换64为虚拟机的情况下，可以通过减少最大堆和减少栈容量来换取更多线程。
