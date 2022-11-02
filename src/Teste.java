import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Teste {

    static Semaphore sem = new Semaphore(1);
    static Lock lock = new ReentrantLock();
    static Condition esperandoSem = lock.newCondition();

    public static void main(String[] args) {

        var t1 = new Thread(() -> work(1));
        var t2 = new Thread(() -> work(2));
        t1.start();
        t2.start();
    }

    static void work(int id) {
        lock.lock();
        System.out.println(id + " adiquiriu lock1");
        try {
            while(!sem.tryAcquire()) {
                esperandoSem.await();
            }
            System.out.println(id + " adiquiriu sem1");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(id + " liberou lock1");
            lock.unlock();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        lock.lock();
        System.out.println(id + " adiquiriu lock2");
        try {
            sem.release();
            esperandoSem.signalAll();
            System.out.println(id + " liberou sem2");
        } finally {
            System.out.println(id + " liberou lock2");
            lock.unlock();
        }
    }
}
