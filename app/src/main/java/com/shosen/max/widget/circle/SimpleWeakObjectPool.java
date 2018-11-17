package com.shosen.max.widget.circle;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;

public class SimpleWeakObjectPool<T> {

    private WeakReference<T>[] objectsPool;

    private int size;

    private int curPointer = -1;

    public SimpleWeakObjectPool() {
        this(5);
    }

    public SimpleWeakObjectPool(int size) {
        this.objectsPool = (WeakReference<T>[]) Array.newInstance(WeakReference.class, size);
        this.size = size;
    }

    public synchronized T get() {
        if (curPointer == -1 || curPointer > objectsPool.length) {
            return null;
        }
        T obj = objectsPool[curPointer].get();
        objectsPool[curPointer] = null;
        curPointer--;
        return obj;
    }


    public synchronized boolean put(T t) {
        if (curPointer == -1 || curPointer < objectsPool.length - 1) {
            curPointer++;
            objectsPool[curPointer] = new WeakReference<T>(t);
            return true;
        }
        return false;
    }

    public void clean() {
        for (int i = 0; i < objectsPool.length; i++) {
            objectsPool[i].clear();
            objectsPool = null;
        }
        curPointer = -1;
    }

    public int getSize() {
        return objectsPool == null ? 0 : objectsPool.length;
    }
}
