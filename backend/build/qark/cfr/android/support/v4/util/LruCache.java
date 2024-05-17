/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Iterator
 *  java.util.LinkedHashMap
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package android.support.v4.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = n;
        this.map = new LinkedHashMap(0, 0.75f, true);
    }

    private int safeSizeOf(K k, V v) {
        int n = this.sizeOf(k, v);
        if (n < 0) {
            throw new IllegalStateException("Negative size: " + k + "=" + v);
        }
        return n;
    }

    protected V create(K k) {
        return null;
    }

    public final int createCount() {
        synchronized (this) {
            int n = this.createCount;
            return n;
        }
    }

    protected void entryRemoved(boolean bl, K k, V v, V v2) {
    }

    public final void evictAll() {
        this.trimToSize(-1);
    }

    public final int evictionCount() {
        synchronized (this) {
            int n = this.evictionCount;
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final V get(K k) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        // MONITORENTER : this
        Object object = this.map.get(k);
        if (object != null) {
            ++this.hitCount;
            // MONITOREXIT : this
            return (V)object;
        }
        ++this.missCount;
        // MONITOREXIT : this
        object = this.create(k);
        if (object == null) {
            return null;
        }
        // MONITORENTER : this
        ++this.createCount;
        Object object2 = this.map.put(k, object);
        if (object2 != null) {
            this.map.put(k, object2);
        } else {
            this.size += this.safeSizeOf(k, object);
        }
        // MONITOREXIT : this
        if (object2 != null) {
            this.entryRemoved(false, k, object, object2);
            return (V)object2;
        }
        this.trimToSize(this.maxSize);
        return (V)object;
    }

    public final int hitCount() {
        synchronized (this) {
            int n = this.hitCount;
            return n;
        }
    }

    public final int maxSize() {
        synchronized (this) {
            int n = this.maxSize;
            return n;
        }
    }

    public final int missCount() {
        synchronized (this) {
            int n = this.missCount;
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final V put(K k, V v) {
        if (k == null) throw new NullPointerException("key == null || value == null");
        if (v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        // MONITORENTER : this
        ++this.putCount;
        this.size += this.safeSizeOf(k, v);
        Object object = this.map.put(k, v);
        if (object != null) {
            this.size -= this.safeSizeOf(k, object);
        }
        // MONITOREXIT : this
        if (object != null) {
            this.entryRemoved(false, k, object, v);
        }
        this.trimToSize(this.maxSize);
        return (V)object;
    }

    public final int putCount() {
        synchronized (this) {
            int n = this.putCount;
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final V remove(K k) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        // MONITORENTER : this
        Object object = this.map.remove(k);
        if (object != null) {
            this.size -= this.safeSizeOf(k, object);
        }
        // MONITOREXIT : this
        if (object == null) return (V)object;
        this.entryRemoved(false, k, object, null);
        return (V)object;
    }

    public final int size() {
        synchronized (this) {
            int n = this.size;
            return n;
        }
    }

    protected int sizeOf(K k, V v) {
        return 1;
    }

    public final Map<K, V> snapshot() {
        synchronized (this) {
            LinkedHashMap linkedHashMap = new LinkedHashMap(this.map);
            return linkedHashMap;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final String toString() {
        int n = 0;
        synchronized (this) {
            int n2 = this.hitCount + this.missCount;
            if (n2 == 0) return String.format((String)"LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", (Object[])new Object[]{this.maxSize, this.hitCount, this.missCount, n});
            n = this.hitCount * 100 / n2;
            return String.format((String)"LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", (Object[])new Object[]{this.maxSize, this.hitCount, this.missCount, n});
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void trimToSize(int n) {
        do {
            Object object;
            Object object2;
            synchronized (this) {
                if (this.size < 0 || this.map.isEmpty() && this.size != 0) {
                    throw new IllegalStateException(this.getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }
                if (this.size <= n || this.map.isEmpty()) {
                    return;
                }
                object2 = (Map.Entry)this.map.entrySet().iterator().next();
                object = object2.getKey();
                object2 = object2.getValue();
                this.map.remove(object);
                this.size -= this.safeSizeOf(object, object2);
                ++this.evictionCount;
            }
            this.entryRemoved(true, object, object2, null);
        } while (true);
    }
}

