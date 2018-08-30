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
