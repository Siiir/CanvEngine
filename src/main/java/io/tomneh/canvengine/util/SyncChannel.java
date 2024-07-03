package io.tomneh.canvengine.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

/** Synchronized MPSC channel. */
public class SyncChannel<E> {

    /** Internal lock used to synchronize {@linkplain SyncChannel} method invocations. */
    protected ReentrantLock reentrantLock= new ReentrantLock();

    /** Not-sync. collection that serves as an internal channel. */
    protected ArrayList<E> channel= new ArrayList<>();

    public int size(){
        this.reentrantLock.lock();
        try{
            return this.channel.size();
        }finally {
            this.reentrantLock.unlock();
        }
    }

    /** Pushes an element into the internal channel.
     * <br><br>
     * Pushed element can be then take-read by receiver.
     * {@linkplain #push(E)} is intended to be used by senders in MPSC model.
     * */
    public void push(E element){
        this.reentrantLock.lock();
        try{
            channel.add(element);
        }finally {
            this.reentrantLock.unlock();
        }
    }

    public void pushAll(Collection<E> collection){
        this.reentrantLock.lock();
        try{
            channel.addAll(collection);
        }finally {
            this.reentrantLock.unlock();
        }
    }

    /** Moves all the commands from internal channel to provided collection.
     * <br><br>
     * It moves elements in stable way – preserving their order.
     *
     * It is intended to be used by commands receiver in MPSC model.
     * @param into Collection to move commands into.
     * */
    public void takeAll(ArrayList<E> into){
        this.reentrantLock.lock();
        try{
            for (E e :
                    this.channel) {
                into.add(e);
            }
            this.channel.clear();
        }finally {
            this.reentrantLock.unlock();
        }
    }
}
