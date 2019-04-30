package life.shank;


public class Mapppp<V> {
    private static final int DEFAULT_CAPACITY = 64;
    private static final int MAXIMUM_CAPACITY = 1 << 29;
    transient int[] keys; // non-private to simplify nested class access
    transient Object[] values; // non-private to simplify nested class access
    int size;
    transient int modCount;
    static final int NULL_KEY = 0;

    private static int maskNull(int key) {
        return (key == 0 ? NULL_KEY : key);
    }

    public Mapppp() {
        keys = new int[DEFAULT_CAPACITY];
        values = new Object[DEFAULT_CAPACITY];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private static int hash(int h, int length) {
        // Multiply by -127, and left-shift to use least bit as part of hash
        return ((h << 1) - (h << 8)) & (length - 1);
    }

    private static int nextKeyIndex(int i, int len) {
        return (i + 1 < len ? i + 1 : 0);
    }

    public V get(Object key) {
        int h = System.identityHashCode(key);
        return get(h);
    }

    public V get(int key) {
        int k = maskNull(key);
        int[] tab = keys;
        Object[] vals = values;
        int len = tab.length;
        int i = hash(k, len);
        while (true) {
            int item = tab[i];
            if (item == k)
                return (V) vals[i];
            if (item == 0)
                return null;
            i = nextKeyIndex(i, len);
        }
    }

    public boolean containsKey(int key) {
        int k = maskNull(key);
        int[] tab = keys;
        int len = tab.length;
        int i = hash(k, len);
        while (true) {
            int item = tab[i];
            if (item == k)
                return true;
            if (item == NULL_KEY)
                return false;
            i = nextKeyIndex(i, len);
        }
    }

    public V put(Object key, V value) {
        int h = System.identityHashCode(key);
        return put(h, value);
    }

    public V put(int key, V value) {
        final int k = maskNull(key);

        retryAfterResize:
        for (; ; ) {
            final int[] kk = keys;
            final int len = kk.length;
            int i = hash(k, len);

            for (int item; (item = kk[i]) != NULL_KEY;
                 i = nextKeyIndex(i, len)) {
                if (item == k) {
                    @SuppressWarnings("unchecked")
                    V oldValue = (V) values[i];
                    values[i] = value;
                    return oldValue;
                }
            }

            final int s = size + 1;
            // Use optimized form of 3 * s.
            // Next capacity is len, 2 * current capacity.
            if (s + (s << 1) > len && resize(len))
                continue retryAfterResize;

            modCount++;
            keys[i] = k;
            values[i] = value;
            size = s;
            return null;
        }
    }

    private boolean resize(int newCapacity) {
        // assert (newCapacity & -newCapacity) == newCapacity; // power of 2
        int newLength = newCapacity * 2;

        int[] oldKeys = keys;
        Object[] oldValues = values;
        int oldLength = oldKeys.length;
        if (oldLength == 2 * MAXIMUM_CAPACITY) { // can't expand any further
            if (size == MAXIMUM_CAPACITY - 1)
                throw new IllegalStateException("Capacity exhausted.");
            return false;
        }
        if (oldLength >= newLength)
            return false;

        int[] newKeys = new int[newLength];
        Object[] newValues = new Object[newLength];

        for (int j = 0; j < oldLength; j += 1) {
            int key = oldKeys[j];
            if (key != NULL_KEY) {
                Object value = oldValues[j];
                oldKeys[j] = NULL_KEY;
                oldValues[j] = null;
                int i = hash(key, newLength);
                while (newKeys[i] != NULL_KEY)
                    i = nextKeyIndex(i, newLength);
                newKeys[i] = key;
                newValues[i] = value;
            }
        }
        keys = newKeys;
        values = newValues;
        return true;
    }

    /**
     * Removes the mapping for this key from this map if present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public V remove(int key) {
        int k = maskNull(key);
        int[] tab = keys;
        Object[] vals = values;
        int len = tab.length;
        int i = hash(k, len);

        while (true) {
            int item = tab[i];
            if (item == k) {
                modCount++;
                size--;
                @SuppressWarnings("unchecked")
                V oldValue = (V) vals[i];
                vals[i] = null;
                tab[i] = NULL_KEY;
                closeDeletion(i);
                return oldValue;
            }
            if (item == NULL_KEY)
                return null;
            i = nextKeyIndex(i, len);
        }
    }

    /**
     * Rehash all possibly-colliding entries following a
     * deletion. This preserves the linear-probe
     * collision properties required by get, put, etc.
     *
     * @param d the index of a newly empty deleted slot
     */
    private void closeDeletion(int d) {
        // Adapted from Knuth Section 6.4 Algorithm R
        int[] tab = keys;
        Object[] vals = values;
        int len = tab.length;

        // Look for items to swap into newly vacated slot
        // starting at index immediately following deletion,
        // and continuing until a null slot is seen, indicating
        // the end of a run of possibly-colliding keys.
        int item;
        for (int i = nextKeyIndex(d, len); (item = tab[i]) != NULL_KEY;
             i = nextKeyIndex(i, len)) {
            // The following test triggers if the item at slot i (which
            // hashes to be at slot r) should take the spot vacated by d.
            // If so, we swap it in, and then continue with d now at the
            // newly vacated i.  This process will terminate when we hit
            // the null slot at the end of this run.
            // The test is messy because we are using a circular table.
            int r = hash(item, len);
            if ((i < r && (r <= d || d <= i)) || (r <= d && d <= i)) {
                tab[d] = item;
                vals[d] = vals[i];
                tab[i] = NULL_KEY;
                vals[i] = null;
                d = i;
            }
        }
    }

    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    public void clear() {
        modCount++;
        int[] tab = keys;
        Object[] vals = values;
        for (int i = 0; i < tab.length; i++) {
            tab[i] = NULL_KEY;
            vals[i] = null;
        }
        size = 0;
    }
}
