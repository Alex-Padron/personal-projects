package ConsistencyTests;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicMessageCounter {
    private int message_count;
    private Callable<Void> check_consistency;
    private Lock lock;

    public AtomicMessageCounter(Callable<Void> check_consistency) {
	this.message_count = 0;
	this.lock = new ReentrantLock();
	this.check_consistency = check_consistency;
    }

    public void inc() throws Exception {
	lock.lock();
	message_count++;
	if (message_count % 500 == 0) {
		check_consistency.call();
	}
	lock.unlock();
    }
}
