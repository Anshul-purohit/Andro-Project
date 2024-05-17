/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.ClassCastException
 *  java.lang.IllegalStateException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 *  java.lang.reflect.Array
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package android.support.v4.util;

import android.support.v4.util.ContainerHelpers;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class MapCollections<K, V> {
    MapCollections<K, V> mEntrySet;
    MapCollections<K, V> mKeySet;
    MapCollections<K, V> mValues;

    MapCollections() {
    }

    public static <K, V> boolean containsAllHelper(Map<K, V> map, Collection<?> iterator) {
        iterator = iterator.iterator();
        while (iterator.hasNext()) {
            if (map.containsKey(iterator.next())) continue;
            return false;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static <T> boolean equalsSetHelper(Set<T> set, Object object) {
        boolean bl = true;
        boolean bl2 = false;
        if (set == object) {
            return true;
        }
        if (!(object instanceof Set)) return bl2;
        object = (Set)object;
        try {
            if (set.size() != object.size()) return false;
            bl2 = set.containsAll((Collection)object);
            if (!bl2) return false;
            return bl;
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
        catch (ClassCastException classCastException) {
            return false;
        }
    }

    public static <K, V> boolean removeAllHelper(Map<K, V> map, Collection<?> iterator) {
        int n = map.size();
        iterator = iterator.iterator();
        while (iterator.hasNext()) {
            map.remove(iterator.next());
        }
        if (n != map.size()) {
            return true;
        }
        return false;
    }

    public static <K, V> boolean retainAllHelper(Map<K, V> map, Collection<?> collection) {
        int n = map.size();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            if (collection.contains(iterator.next())) continue;
            iterator.remove();
        }
        if (n != map.size()) {
            return true;
        }
        return false;
    }

    protected abstract void colClear();

    protected abstract Object colGetEntry(int var1, int var2);

    protected abstract Map<K, V> colGetMap();

    protected abstract int colGetSize();

    protected abstract int colIndexOfKey(Object var1);

    protected abstract int colIndexOfValue(Object var1);

    protected abstract void colPut(K var1, V var2);

    protected abstract void colRemoveAt(int var1);

    protected abstract V colSetValue(int var1, V var2);

    public Set<Map.Entry<K, V>> getEntrySet() {
        if (this.mEntrySet == null) {
            this.mEntrySet = new EntrySet();
        }
        return this.mEntrySet;
    }

    public Set<K> getKeySet() {
        if (this.mKeySet == null) {
            this.mKeySet = new KeySet();
        }
        return this.mKeySet;
    }

    public Collection<V> getValues() {
        if (this.mValues == null) {
            this.mValues = new ValuesCollection();
        }
        return this.mValues;
    }

    public Object[] toArrayHelper(int n) {
        int n2 = this.colGetSize();
        Object[] arrobject = new Object[n2];
        for (int i = 0; i < n2; ++i) {
            arrobject[i] = this.colGetEntry(i, n);
        }
        return arrobject;
    }

    public <T> T[] toArrayHelper(T[] arrT, int n) {
        int n2 = this.colGetSize();
        T[] arrT2 = arrT;
        if (arrT.length < n2) {
            arrT2 = (Object[])Array.newInstance((Class)arrT.getClass().getComponentType(), (int)n2);
        }
        for (int i = 0; i < n2; ++i) {
            arrT2[i] = this.colGetEntry(i, n);
        }
        if (arrT2.length > n2) {
            arrT2[n2] = null;
        }
        return arrT2;
    }

    final class ArrayIterator<T>
    implements Iterator<T> {
        boolean mCanRemove;
        int mIndex;
        final int mOffset;
        int mSize;

        ArrayIterator(int n) {
            this.mCanRemove = false;
            this.mOffset = n;
            this.mSize = MapCollections.this.colGetSize();
        }

        public boolean hasNext() {
            if (this.mIndex < this.mSize) {
                return true;
            }
            return false;
        }

        public T next() {
            Object object = MapCollections.this.colGetEntry(this.mIndex, this.mOffset);
            ++this.mIndex;
            this.mCanRemove = true;
            return (T)object;
        }

        public void remove() {
            if (!this.mCanRemove) {
                throw new IllegalStateException();
            }
            --this.mIndex;
            --this.mSize;
            this.mCanRemove = false;
            MapCollections.this.colRemoveAt(this.mIndex);
        }
    }

    final class EntrySet
    implements Set<Map.Entry<K, V>> {
        EntrySet() {
        }

        public boolean add(Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Map.Entry<K, V>> iterator) {
            int n = MapCollections.this.colGetSize();
            for (Map.Entry entry : iterator) {
                MapCollections.this.colPut(entry.getKey(), entry.getValue());
            }
            if (n != MapCollections.this.colGetSize()) {
                return true;
            }
            return false;
        }

        public void clear() {
            MapCollections.this.colClear();
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean contains(Object object) {
            int n;
            if (!(object instanceof Map.Entry) || (n = MapCollections.this.colIndexOfKey((object = (Map.Entry)object).getKey())) < 0) {
                return false;
            }
            return ContainerHelpers.equal(MapCollections.this.colGetEntry(n, 1), object.getValue());
        }

        public boolean containsAll(Collection<?> iterator) {
            iterator = iterator.iterator();
            while (iterator.hasNext()) {
                if (this.contains(iterator.next())) continue;
                return false;
            }
            return true;
        }

        public boolean equals(Object object) {
            return MapCollections.equalsSetHelper(this, object);
        }

        /*
         * Enabled aggressive block sorting
         */
        public int hashCode() {
            int n = 0;
            int n2 = MapCollections.this.colGetSize() - 1;
            while (n2 >= 0) {
                Object object = MapCollections.this.colGetEntry(n2, 0);
                Object object2 = MapCollections.this.colGetEntry(n2, 1);
                int n3 = object == null ? 0 : object.hashCode();
                int n4 = object2 == null ? 0 : object2.hashCode();
                n += n4 ^ n3;
                --n2;
            }
            return n;
        }

        public boolean isEmpty() {
            if (MapCollections.this.colGetSize() == 0) {
                return true;
            }
            return false;
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            return new MapIterator();
        }

        public boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return MapCollections.this.colGetSize();
        }

        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        public <T> T[] toArray(T[] arrT) {
            throw new UnsupportedOperationException();
        }
    }

    final class KeySet
    implements Set<K> {
        KeySet() {
        }

        public boolean add(K k) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            MapCollections.this.colClear();
        }

        public boolean contains(Object object) {
            if (MapCollections.this.colIndexOfKey(object) >= 0) {
                return true;
            }
            return false;
        }

        public boolean containsAll(Collection<?> collection) {
            return MapCollections.containsAllHelper(MapCollections.this.colGetMap(), collection);
        }

        public boolean equals(Object object) {
            return MapCollections.equalsSetHelper(this, object);
        }

        /*
         * Enabled aggressive block sorting
         */
        public int hashCode() {
            int n = 0;
            int n2 = MapCollections.this.colGetSize() - 1;
            while (n2 >= 0) {
                Object object = MapCollections.this.colGetEntry(n2, 0);
                int n3 = object == null ? 0 : object.hashCode();
                n += n3;
                --n2;
            }
            return n;
        }

        public boolean isEmpty() {
            if (MapCollections.this.colGetSize() == 0) {
                return true;
            }
            return false;
        }

        public Iterator<K> iterator() {
            return new ArrayIterator(0);
        }

        public boolean remove(Object object) {
            int n = MapCollections.this.colIndexOfKey(object);
            if (n >= 0) {
                MapCollections.this.colRemoveAt(n);
                return true;
            }
            return false;
        }

        public boolean removeAll(Collection<?> collection) {
            return MapCollections.removeAllHelper(MapCollections.this.colGetMap(), collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return MapCollections.retainAllHelper(MapCollections.this.colGetMap(), collection);
        }

        public int size() {
            return MapCollections.this.colGetSize();
        }

        public Object[] toArray() {
            return MapCollections.this.toArrayHelper(0);
        }

        public <T> T[] toArray(T[] arrT) {
            return MapCollections.this.toArrayHelper(arrT, 0);
        }
    }

    final class MapIterator
    implements Iterator<Map.Entry<K, V>>,
    Map.Entry<K, V> {
        int mEnd;
        boolean mEntryValid;
        int mIndex;

        MapIterator() {
            this.mEntryValid = false;
            this.mEnd = MapCollections.this.colGetSize() - 1;
            this.mIndex = -1;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public final boolean equals(Object object) {
            boolean bl = true;
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            if (!ContainerHelpers.equal((object = (Map.Entry)object).getKey(), MapCollections.this.colGetEntry(this.mIndex, 0))) return false;
            if (!ContainerHelpers.equal(object.getValue(), MapCollections.this.colGetEntry(this.mIndex, 1))) return false;
            return bl;
        }

        public K getKey() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return (K)MapCollections.this.colGetEntry(this.mIndex, 0);
        }

        public V getValue() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return (V)MapCollections.this.colGetEntry(this.mIndex, 1);
        }

        public boolean hasNext() {
            if (this.mIndex < this.mEnd) {
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         */
        public final int hashCode() {
            int n = 0;
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            Object object = MapCollections.this.colGetEntry(this.mIndex, 0);
            Object object2 = MapCollections.this.colGetEntry(this.mIndex, 1);
            int n2 = object == null ? 0 : object.hashCode();
            if (object2 == null) {
                return n ^ n2;
            }
            n = object2.hashCode();
            return n ^ n2;
        }

        public Map.Entry<K, V> next() {
            ++this.mIndex;
            this.mEntryValid = true;
            return this;
        }

        public void remove() {
            if (!this.mEntryValid) {
                throw new IllegalStateException();
            }
            MapCollections.this.colRemoveAt(this.mIndex);
            --this.mIndex;
            --this.mEnd;
            this.mEntryValid = false;
        }

        public V setValue(V v) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return MapCollections.this.colSetValue(this.mIndex, v);
        }

        public final String toString() {
            return this.getKey() + "=" + this.getValue();
        }
    }

    final class ValuesCollection
    implements Collection<V> {
        ValuesCollection() {
        }

        public boolean add(V v) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            MapCollections.this.colClear();
        }

        public boolean contains(Object object) {
            if (MapCollections.this.colIndexOfValue(object) >= 0) {
                return true;
            }
            return false;
        }

        public boolean containsAll(Collection<?> iterator) {
            iterator = iterator.iterator();
            while (iterator.hasNext()) {
                if (this.contains(iterator.next())) continue;
                return false;
            }
            return true;
        }

        public boolean isEmpty() {
            if (MapCollections.this.colGetSize() == 0) {
                return true;
            }
            return false;
        }

        public Iterator<V> iterator() {
            return new ArrayIterator(1);
        }

        public boolean remove(Object object) {
            int n = MapCollections.this.colIndexOfValue(object);
            if (n >= 0) {
                MapCollections.this.colRemoveAt(n);
                return true;
            }
            return false;
        }

        public boolean removeAll(Collection<?> collection) {
            int n = MapCollections.this.colGetSize();
            boolean bl = false;
            int n2 = 0;
            while (n2 < n) {
                int n3 = n;
                int n4 = n2;
                if (collection.contains(MapCollections.this.colGetEntry(n2, 1))) {
                    MapCollections.this.colRemoveAt(n2);
                    n4 = n2 - 1;
                    n3 = n - 1;
                    bl = true;
                }
                n2 = n4 + 1;
                n = n3;
            }
            return bl;
        }

        public boolean retainAll(Collection<?> collection) {
            int n = MapCollections.this.colGetSize();
            boolean bl = false;
            int n2 = 0;
            while (n2 < n) {
                int n3 = n;
                int n4 = n2;
                if (!collection.contains(MapCollections.this.colGetEntry(n2, 1))) {
                    MapCollections.this.colRemoveAt(n2);
                    n4 = n2 - 1;
                    n3 = n - 1;
                    bl = true;
                }
                n2 = n4 + 1;
                n = n3;
            }
            return bl;
        }

        public int size() {
            return MapCollections.this.colGetSize();
        }

        public Object[] toArray() {
            return MapCollections.this.toArrayHelper(1);
        }

        public <T> T[] toArray(T[] arrT) {
            return MapCollections.this.toArrayHelper(arrT, 1);
        }
    }

}

