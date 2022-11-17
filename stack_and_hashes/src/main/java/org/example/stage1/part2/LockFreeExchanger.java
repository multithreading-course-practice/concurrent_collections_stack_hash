package org.example.stage1.part2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicStampedReference;

public class LockFreeExchanger<T> {

    private final int EMPTY = 0, WAITING = 1, BUSY = 2;
    private AtomicStampedReference<T> slot = new AtomicStampedReference<>(null, 0);

    public T exchange(T myItem, long timeout, TimeUnit unit) throws TimeoutException {
        long nanos = unit.toNanos(timeout);
        long timeBound = System.nanoTime() + nanos;
        int[] stampHolder = {EMPTY};
        while (true) {
            if (System.nanoTime() > timeBound) {
                throw new TimeoutException();
            }
            T yoursItem = slot.get(stampHolder);
            int stamp = stampHolder[0];
            switch (stamp) {
                case EMPTY:
                    if (slot.compareAndSet(yoursItem, myItem, EMPTY, WAITING)) {
                        while (System.nanoTime() < timeBound) {

                        }
                    }
                    if (slot.compareAndSet(myItem, null, WAITING, EMPTY)) {
                        throw new TimeoutException();
                    } else {

                    }
                case WAITING:

                case BUSY:
                    //noop
                default:
                    break;
            }
        }
    }
}
