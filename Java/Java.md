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
    * 基于哈希表的Map接口实现、允许null键/值、非同步、不保证有序(比如插入的顺序)、也不保证序不随时间变化
    * [HashMap实现原理中文参考](http://wiki.jikexueyuan.com/project/java-collection/hashmap.html)
  * **ConcurrentHashMap**
    * [实现原理](https://blog.csdn.net/justloveyou_/article/details/72783008)
    > **读操作不需要加锁？** 因为HashEntry中的key、hash和next指针都是final的。这意味着，我们不能把节点添加到链表的中间和尾部，也不能在链表的中间和尾部删除节点。这个特性可以保证：在访问某个节点时，这个节点之后的链接不会被改变，这个特性可以大大降低处理链表时的复杂性。与此同时，由于HashEntry类的value字段被声明是Volatile的，因此Java的内存模型就可以保证：某个写线程对value字段的写入马上就可以被后续的某个读线程看到。此外，由于在ConcurrentHashMap中不允许用null作为键和值，所以当读线程读到某个HashEntry的value为null时，便知道产生了冲突 —— 发生了重排序现象，此时便会加锁重新读入这个value值。这些特性互相配合，使得读线程即使在不加锁状态下，也能正确访问 ConcurrentHashMap。总的来说，ConcurrentHashMap读操作不需要加锁的奥秘在于以下三点：
      * 用HashEntry对象的不变性来降低读操作对加锁的需求；
      * 用Volatile变量协调读写线程间的内存可见性；
      * 若读时发生指令重排序现象，则加锁重读；

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
      > 描述Java方法执行的内存模型：每个方法被执行的时候都会同时创建一个栈桢用于存储局部变量表、操作栈、动态链接、方法出口等信息。方法从调用到退出，对应着一个栈桢在Java虚拟机栈中入栈到出栈的过程。

      > 如果线程请求的栈深度超过了JVM所允许的深度，将抛出StackOverflowError异常；如果虚拟机栈可以动态扩展，但是扩展是申请不到足够的内存就会抛出OutOfMemoryError异常。

    * ***程序计数器***
      > 本区域可以看作是当前线程执行的字节码的行号指示器。Java虚拟机的多线程是通过线程轮流切换并分配出机器的执行时间来实现的，在任何确定的时刻，一个处理器（多核处理器的一个内核）只会执行一条线程中的指令。因此，为了切换后能回到正确的位置，每条线程都必须有一个线程私有的程序计数器，各条线程之间的计数器互不影响，独立存储。__*Java虚拟机规范中唯一一个没有规定OutOfMemoryError的情况的区域*__

  * **直接内存（Direct Memory）**
    > NIO基于Channel和Buffer，可以使用Native函数直接访问堆外内存，然后通过一个存储在Java堆中DirectByteBuffer对象作为这块内存的引用进行操作。在某些场景中可以显著提高性能，因为避免了在Java堆和Native堆中来回复制数据；

    > 大小受到物理内存大小的限制。

### JVM GC
  > 判定对象是否还“活着”： 根搜索算法（Java）和引用计数算法（Python，Java中不好解决循环引用的问题，因此没有采用该方法）

#### 无用的类判定条件(方法区回收)
  >   * 类所有的实例都已被回收，也就是Java堆中不存在该类的任何实例；
  * 该类的ClassLoader已经被回收
  * 该类对应的java.lang.Class对象没有在任何地方被引用，无法在任何地方通过反射访问该类的方法。

#### Java中引用的类型
  >   * 强引用：代码之中普遍存在的Object o = new Object()之类的引用。只要引用还在，GC永远都不会回收被引用的对象；
  * 软引用：描述一些还有用但不是必须的对象。在系统将要发生内存溢出异常之前，这类对象会被列进回收范围之类并进行第二次回收；
  * 弱引用：也是用来描述非必须对象的，只是这种对象只能生存到下一次GC之前；
  * 虚引用：设置虚引用的目的只是为了在这个对象被垃圾回收器回收时收到一个系统通知。

#### 垃圾收集算法
  >   * 标记-清除算法
    >> 先标记要回收的对象，在标记完成后统一回收所有的对象。缺点时标记和清楚的效率低，以及会造成大量不连续的内存碎片；
  * 复制算法
    >> * 将内存按容量划分成大小相等的两块，每次只使用其中一块。当一块内存用完时，将还活着的对象复制到另一块上面，然后再把使用过的内存块一次清理掉。优点是实现简单，没有内存碎片；缺点是可以使用的内存大小只有原来的一半。
    * 可以使用该方法清理新生代。因新生代中的对象98%是朝生夕死，因此可以将内存按照8:1：1的比例划分成Eden，From Survivor和To Survivor三个区域。每次使用Eden和其中一个Survivor，当回收时将两个区域中还活着的额对象拷贝到另一个Survivor中，然后清理到Eden和刚用过的Survivor。这可以保证每次有90%的内存都是被使用的。
    * 当对象成活率较高时，会涉及到大量的复制操作，因此本算法就太适合了。
  * 标记-整理算法
    >> 同标记-清除算法，只不过标记完成后不直接回收，而是将存活的对象向一段移动，然后直接清理调边界以外的内存。
  * 分代收集算法
    >> * 新生代对象死亡率高，只有少量存户，可以选用复制算法；
    * 老年代中对象存活率高，没有足够的空间进行分配担保（Survivor无法容纳的对象字节进入老年代，所以需要老年代进行分配担保），就需要使用“标记-清理”活着“标记-整理算法”。

### JVM状态监控工具
  >   * jps：列举机上的jvm实例。
  * jinfo：查看主机上特定的jvm配置信息
  * jmap：查看主机特定jvm的堆信息
  * jhat：以http方式提供堆转储信息的快捷查看功能
  * jstack：查看特定jvm的thread dump相关信息
  * jstat：查看特定jvm的相关状态信息。如查看jvm的gc信息等等
  * jstatd：启动jmx监控服务
  * jvisualvm：图形界面的jvm监控工具，以上若干命令的集大成者。

### JVM调优
  > jvisialvm监控远程主机，需要首先在远程主机上开启jstatd监控服务。

  > 注意：jdk1.5以上的jvm启动时都已默开启JMX，1.4及以下版本的，需要使用启动参数-D显式开启JMX。被监控主机的主机名称需要与主机的ip地址对应。可以hostname查看当前主机名称，如果hostname映射的ip127.0.0.1，或者是其它ip地址，需要修改需要修改为指向本机IP地址。

  > 被监控机器需要显式指定jstatd监控权限。示例如下，创建一个policy文件，名称为`jstatd.all.policy`(随便起)，内容为
`grant codebase "file:${java_home}/../lib/tools.jar" {  
   permission java.security.AllPermission;  
}; ``
。注意${java_home}变量，如果本地没有定义，可以直接替换成JDK的全路径。


  > 启动jstatd服务，该服务是本地监控程序获取远程服务器jvm状态的桥梁。启动命令为`jstatd -J-Djava.security.policy=jstatd.all.policy`。特别注意一下启动该命令的权限即可。
如果系统中有使用root用户启动的服务，改jstatd服务也需要使用root启动。


### JVM栈溢出如何排错
  > 需要判断是StackOverflowError还是OutOfMemoryError。单线程的情况下无论是栈帧太大还是虚拟机栈容量太小，当内存无法分配时都会出现StackOverflowError。

  > 当由于创建线程过多导致的OutOfMemoryError时，在不能减少线程数或者更换64为虚拟机的情况下，可以通过减少最大堆和减少栈容量来换取更多线程。

### Class文件结构
`javap`: Oracle官方提供的用来分析Class文件字节码的工具，用法`javap -verbose TestClass`

### JVM类加载机制
* Java在运行期动态加载和链接，因此Java可以动态扩展。例如编写一个使用接口的应用程序，可以等到运行时再指定其实际的实现。

* 类在JVM内存中的生命周期：加载（Loading）-验证（Verification）-准备（Preparation）-解析（Resolution）-初始化（Initialization）-使用（Using）-卸载（Unloading）

* 类加载双亲委派模型：
自定义类加载器（User ClassLoader） - 应用程序类加载器（Application ClassLoader, 系统类加载器）- 扩展类加载器（Extension ClassLoader） - 启动类加载器（Bootstrap ClassLoader）。每个类加载器收到类加载的请求后，不会立刻自己去加载，而是把请求委派给父类加载器去完成，每个层次的类加载器都是如此。只有父加载器无法完成这个加载请求时，子加载器才会尝试自己去加载。

### [Excutor - Java线程池实现原理](https://www.jianshu.com/p/87bff5cc8d8c)
> * Executors.new...Pool()
* Executor.execute() 通过Executor.execute()方法提交的任务，必须实现Runnable接口，该方式提交的任务不能获取返回值，因此无法判断任务是否执行成功
* ExecutorService.submit() 通过ExecutorService.submit()方法提交的任务，可以获取任务执行完的返回值。

### [NIO](http://wiki.jikexueyuan.com/project/java-nio-zh/)
>   * Selector(read, write, connect, accept)
  * Buffer(IntBuffer, CharBuffer...)
  * Channel(FileChannel, SocketChannel, ServerSocketChannel)

### Spring MVC
![Spring MVC request handle model](https://github.com/buddyli/reading-thinking/blob/master/Java/pic/springMVC%20request%20handle%20model.png)
>   *	Spring MVC的核心是DispatchServlet。客户端发送的HTTP请求达到应用服务器后，DispatchServlet根据请求的信息以及HandlerMapping的配置找到处理请求的处理器（Handler）。
  *	DispatchServlet得到对应的处理当前请求的Handler之后，通过HandlerAdapter对Handler进行封装，再以统一的适配器接口调用真正的Handler。HandlerAdapter是一个适配器，用来用统一的接口对各种Handler方法进行调用。
  *	Handler完成业务逻辑的处理后将返回一个ModelAndView给DispatchServlet。ModelAndView中包含了模型数据信息和需要返回给客户端的逻辑视图名。
  *	DispatchServlet调用ViewResolver完成由逻辑视图到真实视图对象的解析工作。并使用这个真实的View对象对ModelAndView中的模型数据进行解析和渲染。
  *	DispatchServlet将上一步获取到的数据返回给客户端。最终结果可能是HTML页面，或者是一段JSON文本，或者是一个图片等等。

![对Spring MVC的扩展](https://github.com/buddyli/reading-thinking/blob/dev/Java/pic/Core模块对Spring%20MVC框架模型的扩展.png)

![Core模块流程图](https://github.com/buddyli/reading-thinking/blob/dev/Java/pic/Core工作流程.png)
