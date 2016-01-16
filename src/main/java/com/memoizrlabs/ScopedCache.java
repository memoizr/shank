package com.memoizrlabs;

import java.util.HashMap;
import java.util.Map;

import static com.memoizrlabs.Provider.createProvider;

public class ScopedCache {

    private Scope scope = Scope.scope(this);
    private String name = "";

    ScopedCache(Scope scope) {
        this.scope = scope;
        this.scope.subscribe(() -> Shank.clearNamedScope(scope));
    }

    ScopedCache(Scope scope, String name) {
        this.name = name;
    }

    ScopedCache(String name) {
        this.name = name;
    }

    public ScopedCache named(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see ScopedCache#provide(Class, Object, Object, Object, Object)
     */
    public <V> V provide(Class<V> desiredObjectClass) {
        return providerHelper(desiredObjectClass, createProvider(Shank.getFactory(desiredObjectClass, name)));
    }

    /**
     * @see ScopedCache#provide(Class, Object, Object, Object, Object)
     */
    public <A, V> V provide(Class<V> desiredObjectClass, A a) {
        return providerHelper(desiredObjectClass, createProvider(Shank.getFactory(desiredObjectClass, name), a));
    }

    /**
     * @see ScopedCache#provide(Class, Object, Object, Object, Object)
     */
    public <A, B, V> V provide(Class<V> desiredObjectClass, A a, B b) {
        return providerHelper(desiredObjectClass, createProvider(Shank.getFactory(desiredObjectClass, name), a, b));
    }

    /**
     * @see ScopedCache#provide(Class, Object, Object, Object, Object)
     */
    public <A, B, C, V> V provide(Class<V> desiredObjectClass, A a, B b, C c) {
        return providerHelper(desiredObjectClass, createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c));
    }

    /**
     * Provides the desired object. Throws a NoFactoryException if no
     * factory is registered for the class of the desired object. The object
     * returned will be created the first time this method is called and
     * all subsequent calls will return a cached instance of the same
     * object. When the lifetime observable emits an item, the object will
     * be removed from cache, and the next time this method is called a new
     * instance will be returned, which will be bound to the events from
     * the newly supplied observable.
     *
     * @param desiredObjectClass is the class of the desired object.
     * @param a                  is the first parameter to be passed to the registered factory.
     * @param b                  is the second parameter to be passed to the registered factory.
     * @param c                  is the third parameter to be passed to the registered factory.
     * @param d                  is the fourth parameter to be passed to the registered factory.
     * @return an instance of the desired object as provided by the
     * registered factory.
     */
    public <A, B, C, D, V> V provide(Class<V> desiredObjectClass, A a, B b, C c, D d) {
        return providerHelper(desiredObjectClass, createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c, d));
    }

    @SuppressWarnings("unchecked")
    private <V> V providerHelper(Class<V> desiredObjectClass, Provider provider) {
        final Map<Class, Map<String, Object>> currentScopeMap = Shank.scopedCache.get(scope);

        V desiredObject;
        if (currentScopeMap == null) {
            final Map<Class, Map<String, Object>> scopedMap = new HashMap<>();
            final Map<String, Object> namedMap = new HashMap<>();
            try {
                desiredObject = (V) provider.call();
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(Shank.getErrorMessage(desiredObjectClass, provider));
            }
            namedMap.put(name, desiredObject);
            scopedMap.put(desiredObjectClass, namedMap);
            Shank.scopedCache.put(scope, scopedMap);
        } else {
            Map<String, Object> stringObjectMap = currentScopeMap.get(desiredObjectClass);
            Object o = stringObjectMap.get(name);
            if (o != null) {
                desiredObject = (V) o;
            } else {
                try {
                    desiredObject = (V) provider.call();
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException(Shank.getErrorMessage(desiredObjectClass, provider));
                }
                stringObjectMap.put(name, desiredObject);
            }
        }

        return desiredObject;
    }
}
